package com.SuaraCloud.SongService.RabbitMQ;

import com.SuaraCloud.SongService.Service.BlobStorageService;
import com.SuaraCloud.SongService.dto.SongDeleteRabbitMQ;
import com.SuaraCloud.SongService.model.EventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private final BlobStorageService blobStorageService;

    private final ObjectMapper objectMapper;

    public Receiver(BlobStorageService blobStorageService) {
        this.blobStorageService = blobStorageService;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @RabbitListener(queues = "song-queue")
    public void receiveMessage(EventMessage event) {
        try {
            switch (event.getName()) {
                case "delete_songmeta":
                    handleDeleteSong(event);
                    break;
                default:
                    System.err.println("Unknown event name: " + event.getName());
            }
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }

    private void handleDeleteSong(EventMessage event) {
        try{
            SongDeleteRabbitMQ songMetaDeleteRabbitMQ = objectMapper.treeToValue(event.getBody(), SongDeleteRabbitMQ.class);

            String url = songMetaDeleteRabbitMQ.getUrl();

            try{
                blobStorageService.deleteFile(url);
            }
            catch(Exception e){
                System.err.println("Failed to delete song: " + e.getMessage());
            }
        }
        catch (Exception e){
            System.err.println("Failed to parse body: " + e.getMessage());
        }
    }
}
