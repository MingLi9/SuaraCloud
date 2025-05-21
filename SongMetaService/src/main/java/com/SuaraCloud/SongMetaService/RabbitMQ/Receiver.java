package com.SuaraCloud.SongMetaService.RabbitMQ;

import com.SuaraCloud.SongMetaService.dto.SongMetaDeleteRabbitMQ;
import com.SuaraCloud.SongMetaService.dto.SongMetaDto;
import com.SuaraCloud.SongMetaService.dto.SongMetaRequest;
import com.SuaraCloud.SongMetaService.model.EventMessage;
import com.SuaraCloud.SongMetaService.service.SongMetaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Receiver {
    private final SongMetaService songMetaService;

    private final ObjectMapper objectMapper;

    public Receiver(SongMetaService songMetaService) {
        this.songMetaService = songMetaService;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @RabbitListener(queues = "songmeta-queue")
    public void receiveMessage(EventMessage event) {
        try {
            switch (event.getName()) {
                case "create_song":
                case "update_song":
                    handleCreateOrUpdate(event);
                    break;
                case "delete_song":
                    handleDeleteSong(event);
                    break;
                case "delete_user":
                    handleDeleteUser(event);
                    break;
                default:
                    System.err.println("Unknown event name: " + event.getName());
            }
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }

    private void handleCreateOrUpdate(EventMessage event) {
        try{
            SongMetaDto songMetaDto = objectMapper.treeToValue(event.getBody(), SongMetaDto.class);
            if(songMetaDto.getId() == null) {
                Long artistId = songMetaDto.getArtistId();

                SongMetaRequest songMetaRequest = new SongMetaRequest();
                songMetaRequest.setUrl(songMetaDto.getUrl());
                songMetaRequest.setTitle(songMetaDto.getTitle());

                try{
                    songMetaService.createSongMeta(artistId, songMetaRequest);
                }
                catch(Exception e){
                    System.err.println("Failed to create song: " + e.getMessage());
                }
            }
            else {
                Long artistId = songMetaDto.getArtistId();
                Long id = songMetaDto.getId();

                SongMetaRequest songMetaRequest = new SongMetaRequest();
                songMetaRequest.setUrl(songMetaDto.getUrl());
                songMetaRequest.setTitle(songMetaDto.getTitle());

                try{
                    songMetaService.updateSongMeta(artistId, id, songMetaRequest);
                }
                catch(Exception e){
                    System.err.println("Failed to update song: " + e.getMessage());
                }
            }
        }
        catch(Exception e){
            System.err.println("Failed to parse body: " + e.getMessage());
        }
    }

    private void handleDeleteSong(EventMessage event) {
        try{
            SongMetaDeleteRabbitMQ songMetaDeleteRabbitMQ = objectMapper.treeToValue(event.getBody(), SongMetaDeleteRabbitMQ.class);

            Long artistId = songMetaDeleteRabbitMQ.getArtistId();
            Long id = songMetaDeleteRabbitMQ.getId();

            try{
                songMetaService.deleteSongMeta(artistId, id);
            }
            catch(Exception e){
                System.err.println("Failed to delete song: " + e.getMessage());
            }
        }
        catch (Exception e){
            System.err.println("Failed to parse body: " + e.getMessage());
        }
    }

    private void handleDeleteUser(EventMessage event) {
        try{
            Long artistId = event.getBody().get("id").asLong();
            try{
                List<SongMetaDto> songMetaDtoList = songMetaService.getSongMetasByArtistId(artistId);
                for (SongMetaDto songMetaDto : songMetaDtoList) {
                    songMetaService.deleteSongMeta(artistId, songMetaDto.getId());
                }
            }
            catch(Exception e){
                System.err.println("Failed to delete all songs for user: " + e.getMessage());
            }
        }
        catch (Exception e){
            System.err.println("Failed to parse body: " + e.getMessage());
        }
    }
}
