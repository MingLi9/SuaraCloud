package com.SuaraCloud.SongService.RabbitMQ;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private Long artistId;
    private String title;
    private String originalUrl;
    private String hlsMasterPlaylistUrl;
    private ProcessingStatus processingStatus;
    private Double durationSeconds;
    private List<String> availableBitrates = new ArrayList<>();

    // Constructor for backward compatibility
    public Payload(Long artistId, String title, String url) {
        this.artistId = artistId;
        this.title = title;
        this.originalUrl = url;
        this.processingStatus = ProcessingStatus.PENDING;
    }
}