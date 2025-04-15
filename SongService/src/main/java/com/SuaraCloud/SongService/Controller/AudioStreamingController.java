package com.SuaraCloud.SongService.Controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/songs")
public class AudioStreamingController {

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> streamAudio(@PathVariable String filename) throws IOException {
        // Get the audio file from the resources folder
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Africa.mp3");

        if (inputStream == null) {
            throw new IOException("File not found in resources folder: " + filename);
            
        }

        // Set the response headers for streaming the audio file
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "audio/mpeg");
        responseHeaders.add("Accept-Ranges", "bytes");

        // Return the audio as a stream
        return new ResponseEntity<>(
                new InputStreamResource(inputStream),
                responseHeaders,
                HttpStatus.OK
        );
    }
}
