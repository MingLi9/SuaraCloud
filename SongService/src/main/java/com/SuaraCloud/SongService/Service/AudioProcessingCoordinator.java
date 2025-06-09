package com.SuaraCloud.SongService.Service;

import com.SuaraCloud.SongService.RabbitMQ.Payload;
import com.SuaraCloud.SongService.RabbitMQ.ProcessingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AudioProcessingCoordinator {

    @Autowired
    private BlobStorageService blobStorageService;

    @Autowired
    private MediaProcessingService mediaProcessingService;

    public String uploadAndProcessFile(MultipartFile file, String title, Long artistId) throws IOException {
        // Upload the file
        String blobUrl = blobStorageService.uploadFile(file, title, artistId);
        String blobName = extractBlobNameFromUrl(blobUrl);

        // Create payload
        Payload payload = new Payload();
        payload.setArtistId(artistId);
        payload.setTitle(title);
        payload.setOriginalUrl(blobName);
        payload.setProcessingStatus(ProcessingStatus.PENDING);

        // Send initial message
        blobStorageService.sendMQTTMessage(payload);

        // Start HLS processing
        mediaProcessingService.processAudioToHLS(payload);

        return blobUrl;
    }

    private String extractBlobNameFromUrl(String blobUrl) {
        return blobUrl.substring(blobUrl.lastIndexOf('/') + 1);
    }
}