package com.SuaraCloud.SongService.Service;

import com.SuaraCloud.SongService.RabbitMQ.Payload;
import com.SuaraCloud.SongService.RabbitMQ.ProcessingStatus;
import com.SuaraCloud.SongService.RabbitMQ.RabbitMQEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
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
            log.info("Starting HLS processing for: {}", originalPayload.getOriginalUrl());

            // Update status to PROCESSING
            sendStatusUpdate(originalPayload, ProcessingStatus.PROCESSING, null, null, null);

            // Download original MP3
            InputStream mp3Stream = blobStorageService.downloadFile(originalPayload.getOriginalUrl());
            File tempMp3 = File.createTempFile("audio_", ".mp3");
            Files.copy(mp3Stream, tempMp3.toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("Downloaded MP3 file, size: {} bytes", tempMp3.length());

            // Get audio duration
            double duration = getAudioDuration(tempMp3);
            log.info("Audio duration: {} seconds", duration);

            // Create HLS variants with different bitrates
            List<String> bitrates = Arrays.asList("128k", "192k", "320k");

            // Process each bitrate variant
            for (String bitrate : bitrates) {
                log.info("Processing bitrate: {}", bitrate);
                processHLSVariant(tempMp3, bitrate, originalPayload);
            }

            // Create and upload master playlist
            String masterPlaylistContent = generateMasterPlaylist(bitrates, originalPayload);
            String masterPlaylistBlobName = String.format("hls/%s/master.m3u8",
                    extractBlobNameWithoutExtension(originalPayload.getOriginalUrl()));

            uploadTextFile(masterPlaylistContent, masterPlaylistBlobName, "application/vnd.apple.mpegurl");
            log.info("Uploaded master playlist: {}", masterPlaylistBlobName);

            // Send completion update
            sendStatusUpdate(originalPayload, ProcessingStatus.COMPLETED,
                    masterPlaylistBlobName, duration, bitrates);

            // Cleanup
            tempMp3.delete();
            log.info("HLS processing completed successfully");

        } catch (Exception e) {
            log.error("Error processing HLS for: {}", originalPayload.getOriginalUrl(), e);
            sendStatusUpdate(originalPayload, ProcessingStatus.FAILED, null, null, null);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void processHLSVariant(File inputFile, String bitrate, Payload payload) throws Exception {
        String baseName = extractBlobNameWithoutExtension(payload.getOriginalUrl());
        String outputDir = "/tmp/hls_" + baseName + "_" + bitrate;
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdirs();

        log.info("Processing HLS variant - Input: {}, Output: {}, Bitrate: {}",
                inputFile.getAbsolutePath(), outputDir, bitrate);

        // Fixed FFmpeg command - only process audio stream, ignore video/cover art
        ProcessBuilder pb = new ProcessBuilder(
                ffmpegPath,
                "-y",                                   // Overwrite output files
                "-i", inputFile.getAbsolutePath(),
                "-map", "0:a",                          // Map ONLY the audio stream
                "-c:a", "aac",                          // Use AAC codec
                "-b:a", bitrate,                        // Set audio bitrate
                "-ar", "44100",                         // Set sample rate
                "-ac", "2",                             // Set to stereo
                "-avoid_negative_ts", "make_zero",      // Handle timestamp issues
                "-hls_time", "4",                       // 4-second segments
                "-hls_list_size", "0",                  // Keep all segments in playlist
                "-hls_allow_cache", "1",                // Allow caching
                "-hls_segment_type", "mpegts",          // Use MPEG-TS segments
                "-hls_playlist_type", "vod",            // Video on Demand
                "-hls_flags", "independent_segments",   // Each segment can be decoded independently
                "-hls_segment_filename", outputDir + "/segment_%03d.ts",
                "-f", "hls",                            // Force HLS format
                outputDir + "/playlist.m3u8"
        );

        log.info("FFmpeg command: {}", String.join(" ", pb.command()));

        pb.redirectErrorStream(false);
        Process process = pb.start();

        // Capture stderr (this is where FFmpeg outputs its progress info)
        StringBuilder stderrOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stderrOutput.append(line).append("\n");
                log.info("FFmpeg: {}", line);
            }
        }

        int exitCode = process.waitFor();
        log.info("FFmpeg finished with exit code: {}", exitCode);
        log.info("Full FFmpeg output:\n{}", stderrOutput.toString());

        if (exitCode != 0) {
            log.error("FFmpeg failed with exit code: {}. Output:\n{}", exitCode, stderrOutput.toString());
            throw new RuntimeException("FFmpeg failed with exit code: " + exitCode);
        }

        // Log the generated files and their content
        File[] generatedFiles = outputDirFile.listFiles();
        if (generatedFiles != null) {
            log.info("Generated {} files for bitrate {}", generatedFiles.length, bitrate);
            for (File file : generatedFiles) {
                log.info("Generated file: {} (size: {} bytes)", file.getName(), file.length());

                // If it's a playlist, log its content
                if (file.getName().endsWith(".m3u8")) {
                    try {
                        String content = Files.readString(file.toPath());
                        log.info("Playlist content:\n{}", content);
                    } catch (Exception e) {
                        log.warn("Could not read playlist content", e);
                    }
                }
            }
        } else {
            log.warn("No files generated for bitrate: {}", bitrate);
        }

        // Upload segments and playlist to Azure Blob Storage
        uploadHLSFiles(outputDir, baseName, bitrate);

        // Cleanup temp directory
        deleteDirectory(outputDirFile);
    }
    
    private void uploadHLSFiles(String directory, String baseName, String bitrate) throws IOException {
        File dir = new File(directory);
        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            log.warn("No files to upload in directory: {}", directory);
            return;
        }

        log.info("Uploading {} files for bitrate {}", files.length, bitrate);

        for (File file : files) {
            String blobName = String.format("hls/%s/%s/%s", baseName, bitrate, file.getName());
            log.info("Uploading file: {} -> {}", file.getName(), blobName);

            if (file.getName().endsWith(".m3u8")) {
                // Upload playlist file
                String content = Files.readString(file.toPath());
                log.info("Playlist content for {}:\n{}", bitrate, content);
                uploadTextFile(content, blobName, "application/vnd.apple.mpegurl");
            } else if (file.getName().endsWith(".ts")) {
                // Upload segment file
                blobStorageService.uploadHLSFile(file, blobName);
                log.info("Uploaded segment: {} (size: {} bytes)", file.getName(), file.length());
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

        String masterContent = playlist.toString();
        log.info("Generated master playlist:\n{}", masterContent);
        return masterContent;
    }

    private double getAudioDuration(File audioFile) throws Exception {
        log.info("Getting duration for file: {}", audioFile.getAbsolutePath());

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

        // Also capture any errors
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorOutput = new StringBuilder();
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            errorOutput.append(errorLine).append("\n");
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            log.error("FFprobe failed with exit code: {}. Error: {}", exitCode, errorOutput.toString());
            throw new RuntimeException("FFprobe failed: " + errorOutput.toString());
        }

        if (durationStr == null || durationStr.trim().isEmpty()) {
            log.error("Could not get duration from FFprobe output");
            throw new RuntimeException("Could not determine audio duration");
        }

        double duration = Double.parseDouble(durationStr.trim());
        log.info("Audio duration: {} seconds", duration);
        return duration;
    }

    private void uploadTextFile(String content, String blobName, String contentType) {
        try {
            File tempFile = File.createTempFile("playlist", ".m3u8");
            Files.write(tempFile.toPath(), content.getBytes());
            blobStorageService.uploadHLSFile(tempFile, blobName, contentType);
            tempFile.delete();
            log.info("Uploaded text file: {}", blobName);
        } catch (IOException e) {
            log.error("Failed to upload text file: {}", blobName, e);
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

            log.info("Sent status update: {} for {}", status, originalPayload.getOriginalUrl());
        } catch (Exception e) {
            log.error("Error sending HLS update message for: {}", originalPayload.getOriginalUrl(), e);
        }
    }
}