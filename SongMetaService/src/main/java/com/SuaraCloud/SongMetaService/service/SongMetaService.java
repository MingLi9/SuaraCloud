package com.SuaraCloud.SongMetaService.service;

import com.SuaraCloud.SongMetaService.RabbitMQ.Payload;
import com.SuaraCloud.SongMetaService.RabbitMQ.PayloadDelete;
import com.SuaraCloud.SongMetaService.RabbitMQ.RabbitMQEvent;
import com.SuaraCloud.SongMetaService.dto.SongMetaDto;
import com.SuaraCloud.SongMetaService.dto.SongMetaRequest;
import com.SuaraCloud.SongMetaService.exception.NotDataOwnerException;
import com.SuaraCloud.SongMetaService.exception.ResourceNotFoundException;
import com.SuaraCloud.SongMetaService.exception.SongAlreadyExistsException;
import com.SuaraCloud.SongMetaService.model.SongMeta;
import com.SuaraCloud.SongMetaService.repository.SongMetaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongMetaService {

    private final SongMetaRepository songMetaRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SongMetaService(SongMetaRepository songMetaRepository, RabbitTemplate rabbitTemplate) {
        this.songMetaRepository = songMetaRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<SongMetaDto> getAllSongMetas() {
        return songMetaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SongMetaDto getSongMetaById(Long id) {
        SongMeta songMeta = songMetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SongMeta not found with id: " + id));
        return convertToDto(songMeta);
    }

    public List<SongMetaDto> getSongMetasByArtistId(Long artistId) {
        if (!songMetaRepository.existsByArtistId(artistId)) {
            throw new ResourceNotFoundException("SongMetas for artis id was not found: " + artistId);
        }

        return songMetaRepository.findSongMetaByArtistId(artistId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SongMetaDto getSongMetaByArtistIdAndOriginalUrl(Long artistId, String originalUrl){
        if (!songMetaRepository.existsByArtistId(artistId)) {
            throw new ResourceNotFoundException("SongMetas for artis id was not found: " + artistId);
        }
        SongMeta foundSongMeta = songMetaRepository.findSongMetaByArtistIdAndOriginalUrl(artistId, originalUrl);
        return convertToDto(foundSongMeta);
    }

    @Transactional
    public SongMetaDto createSongMeta(Long artistId, SongMetaRequest songMetaRequest) {
        if (songMetaRepository.existsByOriginalUrl(songMetaRequest.getUrl())) {
            throw new SongAlreadyExistsException("SongMeta url already in use: " + songMetaRequest.getUrl());
        }

        SongMeta songMeta = new SongMeta();
        songMeta.setArtistId(artistId);
        songMeta.setUrl(songMetaRequest.getUrl());
        songMeta.setTitle(songMetaRequest.getTitle());

        SongMeta savedSongMeta = songMetaRepository.save(songMeta);

        Payload payload = new Payload(savedSongMeta.getId(), savedSongMeta.getArtistId(), savedSongMeta.getTitle(), savedSongMeta.getUrl());
        sendMQTTMessage(payload, "create_songmeta");
        return convertToDto(savedSongMeta);
    }

    @Transactional
    public SongMetaDto updateSongMeta(Long artistId, Long id, SongMetaRequest songMetaRequest) {
        SongMeta songMeta = songMetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SongMeta not found with id: " + id));

        if(!songMeta.getArtistId().equals(artistId)) {
            throw new NotDataOwnerException("Only the artis can CRUD their own account");
        }

        songMeta.setArtistId(artistId);
        songMeta.setUrl(songMetaRequest.getUrl());
        songMeta.setHlsMasterPlaylistUrl(songMetaRequest.getHlsMasterPlaylistUrl());
        songMeta.setTitle(songMetaRequest.getTitle());
        songMeta.setProcessingStatus(songMetaRequest.getProcessingStatus());
        songMeta.setDurationSeconds(songMetaRequest.getDurationSeconds());
        songMeta.setAvailableBitrates(songMetaRequest.getAvailableBitrates());

        SongMeta savedSongMeta = songMetaRepository.save(songMeta);

        Payload payload = new Payload(savedSongMeta.getId(), savedSongMeta.getArtistId(), savedSongMeta.getTitle(), savedSongMeta.getUrl());
        sendMQTTMessage(payload, "update_songmeta");
        return convertToDto(savedSongMeta);
    }

    @Transactional
    public void deleteSongMeta(Long artistId, Long id) {
        SongMeta songMeta = songMetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SongMeta not found with id: " + id));

        if(!songMeta.getArtistId().equals(artistId)) {
            throw new NotDataOwnerException("Only the artis can CRUD their own account");
        }
        songMetaRepository.deleteById(id);
        sendMQTTMessageDelete(songMeta.getUrl());
    }

    private SongMetaDto convertToDto(SongMeta songMeta) {
        SongMetaDto songMetaDto = new SongMetaDto();
        songMetaDto.setId(songMeta.getId());
        songMetaDto.setArtistId(songMeta.getArtistId());
        songMetaDto.setTitle(songMeta.getTitle());
        songMetaDto.setOriginalUrl(songMeta.getUrl());
        songMetaDto.setDurationSeconds(songMeta.getDurationSeconds());
        songMetaDto.setAvailableBitrates(songMeta.getAvailableBitrates());
        songMetaDto.setProcessingStatus(songMeta.getProcessingStatus());
        songMetaDto.setHlsMasterPlaylistUrl(songMeta.getHlsMasterPlaylistUrl());
        return songMetaDto;
    }

    private void sendMQTTMessage(Payload payload, String eventName) {
        try{
            RabbitMQEvent event = new RabbitMQEvent<>(eventName, payload, "SongMetaService", ZonedDateTime.now());
            rabbitTemplate.convertAndSend("suara-broadcast-exchange", "", event);
        }
            catch (Exception e){
            System.err.println("Error sending message to MQTT: " + e.getMessage());
        }
    }

    private void sendMQTTMessageDelete(String url) {
        try{
            RabbitMQEvent event = new RabbitMQEvent<>("delete_songmeta", new PayloadDelete(url), "SongMetaService", ZonedDateTime.now());
            rabbitTemplate.convertAndSend("suara-broadcast-exchange", "", event);
        }
        catch (Exception e){
            System.err.println("Error sending message to MQTT: " + e.getMessage());
        }
    }
}