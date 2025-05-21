package com.SuaraCloud.SongService.Service;

import com.SuaraCloud.SongService.RabbitMQ.Payload;
import com.SuaraCloud.SongService.RabbitMQ.PayloadDelete;
import com.SuaraCloud.SongService.RabbitMQ.RabbitMQEvent;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BlobStorageService {

    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private BlobServiceClient blobServiceClient;

    private static final String CONTAINER_NAME = "suarasongs";

    @Autowired
    public BlobStorageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void ensureContainerExists() {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    public String uploadFile(MultipartFile file, String title, Long artistId) throws IOException {
        ensureContainerExists();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);

        // Create a unique blob name to avoid conflicts
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String blobName = UUID.randomUUID().toString() + fileExtension;

        // Create a BlobClient to interact with the specific blob
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        // Set content type for MP3 files
        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType("audio/mpeg");

        // Upload the file to the blob with appropriate headers
        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
            blobClient.setHttpHeaders(headers);
        }

        sendMQTTMessage(new Payload(artistId, title, blobName));
        // Return the blob's URI
        return blobClient.getBlobUrl();
    }

    public InputStream downloadFile(String blobName) {
        ensureContainerExists();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            return null;
        }

        return blobClient.openInputStream();
    }

    public List<String> listFiles() {
        ensureContainerExists();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        PagedIterable<BlobItem> blobs = containerClient.listBlobs();

        List<String> fileNames = new ArrayList<>();
        for (BlobItem blob : blobs) {
            fileNames.add(blob.getName());
        }

        return fileNames;
    }

    // fileName = "{UUID}.mp3"
    public boolean deleteFile(String fileName) {
        ensureContainerExists();

        try {
            // Get the container client
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);

            // Get the blob client for the specific file
            BlobClient blobClient = containerClient.getBlobClient(fileName);

            // Check if the blob exists before attempting to delete
            if (!blobClient.exists()) {
                System.err.println("File " + fileName + " does not exist in the container.");
                return false;
            }

            // Delete the blob
            blobClient.delete();

            // Send MQTT message for deletion after successful deletion
            sendMQTTMessageDelete(fileName);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting file from Azure Blob Storage: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void sendMQTTMessage(Payload payload) {
        try{
            RabbitMQEvent event = new RabbitMQEvent<>("create_song", payload, "SongMetaService", ZonedDateTime.now());
            rabbitTemplate.convertAndSend("suara-broadcast-exchange", "", event);
        }
        catch (Exception e){
            System.err.println("Error sending message to MQTT: " + e.getMessage());
        }
    }

    private void sendMQTTMessageDelete(String url) {
        try{
            RabbitMQEvent event = new RabbitMQEvent<>("delete_song", new PayloadDelete(url), "SongMetaService", ZonedDateTime.now());
            rabbitTemplate.convertAndSend("suara-broadcast-exchange", "", event);
        }
        catch (Exception e){
            System.err.println("Error sending message to MQTT: " + e.getMessage());
        }
    }
}