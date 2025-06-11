package com.SuaraCloud.SongMetaService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Table(name = "songmeta")
public class SongMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artist_id", nullable = false)
    private Long artistId;

    @Column(name = "original_url", nullable = false)
    private String originalUrl; // Original MP3 URL

    @Column(name = "hls_master_playlist_url")
    private String hlsMasterPlaylistUrl; // Master m3u8 URL

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "processing_status")
    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus = ProcessingStatus.PENDING;

    @Column(name = "duration_seconds")
    private Double durationSeconds;

    @ElementCollection
    @CollectionTable(name = "song_bitrates")
    private List<String> availableBitrates = new ArrayList<>();

    public SongMeta() {}
    public SongMeta(Long artistId, String title, String url, Double durationSeconds) {
        this.artistId = artistId;
        this.title = title;
        this.originalUrl = url;
        this.durationSeconds = durationSeconds;
        this.processingStatus = ProcessingStatus.PENDING;
        this.availableBitrates = new ArrayList<>();
    }
    public SongMeta(Long id, Long artistId, String title, String url) {
        this.id = id;
        this.artistId = artistId;
        this.title = title;
        this.originalUrl = url;
        this.processingStatus = ProcessingStatus.PENDING;
    }

    public long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getUrl() {
        return this.originalUrl;
    }

    public void setUrl(String url) {
        this.originalUrl = url;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public void setOriginalUrl(String url) {
        this.originalUrl = url;
    }

    public String getHlsMasterPlaylistUrl() {
        return this.hlsMasterPlaylistUrl;
    }

    public void setHlsMasterPlaylistUrl(String hlsMasterPlaylistUrl) {
        this.hlsMasterPlaylistUrl = hlsMasterPlaylistUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProcessingStatus getProcessingStatus() {
        return this.processingStatus;
    }

    public void setProcessingStatus(ProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }

    public Double getDurationSeconds() {
        return this.durationSeconds;
    }

    public void setDurationSeconds(Double durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public List<String> getAvailableBitrates() {
        return this.availableBitrates;
    }

    public void setAvailableBitrates(List<String> availableBitrates) {
        this.availableBitrates = availableBitrates;
    }
}