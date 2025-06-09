package com.SuaraCloud.SongService.Service;

import com.SuaraCloud.SongService.RabbitMQ.Payload;
import com.SuaraCloud.SongService.RabbitMQ.ProcessingStatus;
import com.SuaraCloud.SongService.RabbitMQ.RabbitMQEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MediaProcessingService {

    @Autowired
    private BlobStorageService blobStorageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${ffmpeg.path:/usr/bin/ffmpeg}")
    private String ffmpegPath;

    @Value("${ffprobe.path:/usr/bin/ffprobe}")
    private String ffprobePath;

    @Async
    public CompletableFuture<Void> processAudioToHLS(Payload originalPayload) {
        try {
            // Update status to PROCESSING
            sendStatusUpdate(originalPayload, ProcessingStatus.PROCESSING, null, null, null);

            // Download original MP3
            InputStream mp3Stream = blobStorageService.downloadFile(originalPayload.getOriginalUrl());
            File tempMp3 = File.createTempFile("audio_", ".mp3");
            Files.copy(mp3Stream, tempMp3.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Get audio duration
            double duration = getAudioDuration(tempMp3);

            // Create HLS variants with different bitrates
            List<String> bitrates = Arrays.asList("128k", "192k", "320k");

            // Process each bitrate variant
            for (String bitrate : bitrates) {
                processHLSVariant(tempMp3, bitrate, originalPayload);
            }

            // Create and upload master playlist
            String masterPlaylistContent = generateMasterPlaylist(bitrates, originalPayload);
            String masterPlaylistBlobName = String.format("hls/%s/master.m3u8",
                    extractBlobNameWithoutExtension(originalPayload.getOriginalUrl()));

            uploadTextFile(masterPlaylistContent, masterPlaylistBlobName, "application/vnd.apple.mpegurl");

            // Send completion update
            sendStatusUpdate(originalPayload, ProcessingStatus.COMPLETED,
                    masterPlaylistBlobName, duration, bitrates);

            // Cleanup
            tempMp3.delete();

        } catch (Exception e) {
            System.err.println("Error processing HLS: " + e.getMessage());
            e.printStackTrace();
            sendStatusUpdate(originalPayload, ProcessingStatus.FAILED, null, null, null);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void processHLSVariant(File inputFile, String bitrate, Payload payload) throws Exception {
        String baseName = extractBlobNameWithoutExtension(payload.getOriginalUrl());
        String outputDir = "/tmp/hls_" + baseName + "_" + bitrate;
        new File(outputDir).mkdirs();

        // FFmpeg command for HLS with 3-second segments (LL-HLS compatible)
        ProcessBuilder pb = new ProcessBuilder(
                ffmpegPath,
                "-i", inputFile.getAbsolutePath(),
                "-c:a", "aac",
                "-b:a", bitrate,
                "-hls_time", "3", // 3-second segments
                "-hls_list_size", "0", // Keep all segments
                "-hls_segment_filename", outputDir + "/segment_%03d.ts",
                "-hls_playlist_type", "vod", // Video on Demand
                "-f", "hls",
                outputDir + "/playlist.m3u8"
        );

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg failed with exit code: " + exitCode);
        }

        // Upload segments and playlist to Azure Blob Storage
        uploadHLSFiles(outputDir, baseName, bitrate);

        // Cleanup temp directory
        deleteDirectory(new File(outputDir));
    }

    private void uploadHLSFiles(String directory, String baseName, String bitrate) throws IOException {
        File dir = new File(directory);
        for (File file : dir.listFiles()) {
            String blobName = String.format("hls/%s/%s/%s", baseName, bitrate, file.getName());

            if (file.getName().endsWith(".m3u8")) {
                // Upload playlist file
                String content = Files.readString(file.toPath());
                uploadTextFile(content, blobName, "application/vnd.apple.mpegurl");
            } else if (file.getName().endsWith(".ts")) {
                // Upload segment file
                blobStorageService.uploadHLSFile(file, blobName);
            }
        }
    }

    private String generateMasterPlaylist(List<String> bitrates, Payload payload) {
        StringBuilder playlist = new StringBuilder();
        playlist.append("#EXTM3U\n");
        playlist.append("#EXT-X-VERSION:3\n\n");

        String baseName = extractBlobNameWithoutExtension(payload.getOriginalUrl());

        for (String bitrate : bitrates) {
            int bitrateNum = Integer.parseInt(bitrate.replace("k", "")) * 1000;
            playlist.append(String.format("#EXT-X-STREAM-INF:BANDWIDTH=%d\n", bitrateNum));
            playlist.append(String.format("%s/playlist.m3u8\n\n", bitrate));
        }

        return playlist.toString();
    }

    private double getAudioDuration(File audioFile) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
                ffprobePath,
                "-v", "quiet",
                "-show_entries", "format=duration",
                "-of", "csv=p=0",
                audioFile.getAbsolutePath()
        );

        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String durationStr = reader.readLine();
        process.waitFor();

        return Double.parseDouble(durationStr);
    }

    private void uploadTextFile(String content, String blobName, String contentType) {
        try {
            File tempFile = File.createTempFile("playlist", ".m3u8");
            Files.write(tempFile.toPath(), content.getBytes());
            blobStorageService.uploadHLSFile(tempFile, blobName, contentType);
            tempFile.delete();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload text file", e);
        }
    }

    private String extractBlobNameWithoutExtension(String blobName) {
        int lastDot = blobName.lastIndexOf('.');
        return lastDot > 0 ? blobName.substring(0, lastDot) : blobName;
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    private void sendStatusUpdate(Payload originalPayload, ProcessingStatus status,
                                  String masterPlaylistUrl, Double duration, List<String> bitrates) {
        try {
            Payload updatePayload = new Payload();
            updatePayload.setArtistId(originalPayload.getArtistId());
            updatePayload.setTitle(originalPayload.getTitle());
            updatePayload.setOriginalUrl(originalPayload.getOriginalUrl());
            updatePayload.setProcessingStatus(status);
            updatePayload.setHlsMasterPlaylistUrl(masterPlaylistUrl);
            updatePayload.setDurationSeconds(duration);
            updatePayload.setAvailableBitrates(bitrates);

            RabbitMQEvent event = new RabbitMQEvent<>("update_song_hls", updatePayload, "SongMetaService", ZonedDateTime.now());
            rabbitTemplate.convertAndSend("suara-broadcast-exchange", "", event);
        } catch (Exception e) {
            System.err.println("Error sending HLS update message: " + e.getMessage());
        }
    }
}