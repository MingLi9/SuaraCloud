package com.SuaraCloud.SongMetaService.RabbitMQ;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private Long id;
    private Long artistId;
    private String title;
    private String Url;
}