package com.SuaraCloud.SongService.Service;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BlobStorageService {

    @Autowired
    private BlobServiceClient blobServiceClient;

    private static final String CONTAINER_NAME = "suarasongs";

    // Initialize container if it doesn't exist
    private void ensureContainerExists() {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
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
}