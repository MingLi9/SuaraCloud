package com.SuaraCloud.SongService.Controller;

import com.SuaraCloud.SongService.RabbitMQ.Payload;
import com.SuaraCloud.SongService.RabbitMQ.ProcessingStatus;
import com.SuaraCloud.SongService.Service.AudioProcessingCoordinator;
import com.SuaraCloud.SongService.Service.BlobStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class AudioStreamingController {

    @Autowired
    private AudioProcessingCoordinator coordinator;
    @Autowired
    private BlobStorageService blobStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file,
            HttpServletRequest request) {

        System.out.println("Request received");
        System.out.println("Content-Type: " + request.getContentType());
        System.out.println("Content-Length: " + request.getContentLength());

        try {
            System.out.println("File is empty: " + file.isEmpty());
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());
            System.out.println("File content type: " + file.getContentType());

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file");
            }

            if (!file.getContentType().equals("audio/mpeg")) {
                return ResponseEntity.badRequest().body("Only MP3 files are supported");
            }

            // Extract title from filename
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf('.'));

            String userIdString = (String) request.getAttribute("userId");
            Long artistId = Long.parseLong(userIdString);

            String blobUrl = coordinator.uploadAndProcessFile(file, title, artistId);

            return ResponseEntity.ok("File uploaded successfully. HLS processing started: " + blobUrl);
        } catch (Exception e) {
            e.printStackTrace(); // Add this to see the full stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    private String extractBlobNameFromUrl(String blobUrl) {
        return blobUrl.substring(blobUrl.lastIndexOf('/') + 1);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> streamAudio(@PathVariable String filename) {
        try {
            // Get the audio file from Azure Blob Storage
            InputStream inputStream = blobStorageService.downloadFile(filename);

            if (inputStream == null) {
                return ResponseEntity.notFound().build();
            }

            // Set the response headers for streaming the audio file
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType("audio/mpeg"));
            responseHeaders.add("Content-Disposition", "inline; filename=" + filename);
            responseHeaders.add("Accept-Ranges", "bytes");

            // Return the audio as a stream
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Example: http://localhost:8085/songs/hls/bf141b6a-f341-4e1c-8e6c-4b3b8abd8510/128k/playlist.m3u8
    @GetMapping("/hls/{uuid}/{bitrate}/{playlist}")
    public ResponseEntity<Resource> streamAudioHLS(
            @PathVariable String uuid,
            @PathVariable String bitrate,
            @PathVariable String playlist) {
        try {
            // Construct the blob path
            String blobPath = "hls/" + uuid + "/" + bitrate + "/" + playlist;
            System.out.println("Requesting HLS file: " + blobPath);

            // Get the playlist file from Azure Blob Storage
            InputStream inputStream = blobStorageService.downloadFile(blobPath);

            if (inputStream == null) {
                System.out.println("HLS file not found: " + blobPath);
                return ResponseEntity.notFound().build();
            }

            // Set the response headers for streaming the playlist
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"));
            responseHeaders.add("Cache-Control", "no-cache");

            // Return the playlist as a stream
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            System.out.println("Error streaming HLS file: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/hls/{uuid}/master.m3u8")
    public ResponseEntity<Resource> getMasterPlaylist(@PathVariable String uuid) {
        try {
            // Construct the blob path
            String blobPath = "hls/" + uuid + "/master.m3u8";
            System.out.println("Requesting master playlist: " + blobPath);

            // Get the master playlist file from Azure Blob Storage
            InputStream inputStream = blobStorageService.downloadFile(blobPath);

            if (inputStream == null) {
                System.out.println("Master playlist not found: " + blobPath);
                return ResponseEntity.notFound().build();
            }

            // Set the response headers
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"));
            responseHeaders.add("Cache-Control", "no-cache");

            // Return the master playlist as a stream
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            System.out.println("Error streaming master playlist: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/hls/{uuid}/{bitrate}/segment_{segmentNumber}.ts")
    public ResponseEntity<Resource> getSegment(
            @PathVariable String uuid,
            @PathVariable String bitrate,
            @PathVariable String segmentNumber) {
        try {
            // Construct the blob path
            String blobPath = "hls/" + uuid + "/" + bitrate + "/segment_" + segmentNumber + ".ts";
            System.out.println("Requesting segment: " + blobPath);

            // Get the segment file from Azure Blob Storage
            InputStream inputStream = blobStorageService.downloadFile(blobPath);

            if (inputStream == null) {
                System.out.println("Segment not found: " + blobPath);
                return ResponseEntity.notFound().build();
            }

            // Set the response headers
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.parseMediaType("video/mp2t"));
            responseHeaders.add("Cache-Control", "public, max-age=31536000"); // Cache segments for 1 year

            // Return the segment as a stream
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            System.out.println("Error streaming segment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/hls/**")
    public ResponseEntity<Resource> getAnyHlsFile(HttpServletRequest request) {
        try {
            // Extract the path from the request
            String requestPath = request.getRequestURI();
            String blobPath = requestPath.substring(requestPath.indexOf("/hls/") + 1); // +1 to remove the leading slash

            System.out.println("Requesting any HLS file: " + blobPath);

            // Get the file from Azure Blob Storage
            InputStream inputStream = blobStorageService.downloadFile(blobPath);

            if (inputStream == null) {
                System.out.println("HLS file not found: " + blobPath);
                return ResponseEntity.notFound().build();
            }

            // Set the response headers based on file extension
            HttpHeaders responseHeaders = new HttpHeaders();
            if (blobPath.endsWith(".m3u8")) {
                responseHeaders.setContentType(MediaType.parseMediaType("application/vnd.apple.mpegurl"));
                responseHeaders.add("Cache-Control", "no-cache");
            } else if (blobPath.endsWith(".ts")) {
                responseHeaders.setContentType(MediaType.parseMediaType("video/mp2t"));
                responseHeaders.add("Cache-Control", "public, max-age=31536000");
            } else {
                responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            // Return the file as a stream
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            System.out.println("Error streaming HLS file: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        try {
            List<String> fileNames = blobStorageService.listFiles();
            return ResponseEntity.ok(fileNames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}