package com.SuaraCloud.SongMetaService.RabbitMQ;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Payload {
    private Long id;
    private Long artistId;
    private String title;
    private String Url;

    public Payload(Long id, Long artistId, String title, String url) {
        this.id = id;
        this.artistId = artistId;
        this.title = title;
        this.Url = url;
    }
}