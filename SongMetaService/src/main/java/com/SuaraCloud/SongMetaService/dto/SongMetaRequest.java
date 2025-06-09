package com.SuaraCloud.SongMetaService.dto;

import com.SuaraCloud.SongMetaService.model.ProcessingStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class SongMetaRequest {
    @Size(max = 100, message = "Url cannot exceed 100 characters")
    private String url;

    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;

    @Size(max = 300, message = "HLS Master Playlist URL cannot exceed 300 characters")
    private String hlsMasterPlaylistUrl;

    private ProcessingStatus processingStatus = ProcessingStatus.PENDING;

    private Double durationSeconds;

    private List<String> availableBitrates = new ArrayList<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHlsMasterPlaylistUrl() {
        return hlsMasterPlaylistUrl;
    }

    public void setHlsMasterPlaylistUrl(String hlsMasterPlaylistUrl) {
        this.hlsMasterPlaylistUrl = hlsMasterPlaylistUrl;
    }

    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(ProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
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
}
