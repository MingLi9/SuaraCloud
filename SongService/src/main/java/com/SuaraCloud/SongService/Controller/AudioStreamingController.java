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