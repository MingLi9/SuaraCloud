package com.SuaraCloud.SongMetaService.dto;

import com.SuaraCloud.SongMetaService.model.ProcessingStatus;

import java.util.ArrayList;
import java.util.List;

public class SongMetaDto {
    private Long id;
    private Long artistId;
    private String originalUrl;
    private String title;
    private String hlsMasterPlaylistUrl;
    private ProcessingStatus processingStatus;
    private Double durationSeconds;
    private List<String> availableBitrates = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getArtistId() {
        return artistId;
    }
    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }
    public String getOriginalUrl() {
        return originalUrl;
    }
    public void setOriginalUrl(String url) {
        this.originalUrl = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }
    public void setProcessingStatus(ProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getHlsMasterPlaylistUrl() {
        return hlsMasterPlaylistUrl;
    }

    public void setHlsMasterPlaylistUrl(String hlsMasterPlaylistUrl) {
        this.hlsMasterPlaylistUrl = hlsMasterPlaylistUrl;
    }

    public Double getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Double durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public List<String> getAvailableBitrates() {
        return availableBitrates;
    }

    public void setAvailableBitrates(List<String> availableBitrates) {
        this.availableBitrates = availableBitrates;
    }

    public SongMetaDto() {}
    public SongMetaDto(Long id, Long artistId, String OriginalUrl, String title, ProcessingStatus processingStatus) {
        this.id = id;
        this.artistId = artistId;
        this.originalUrl = OriginalUrl;
        this.title = title;
        this.processingStatus = processingStatus;
    }
}