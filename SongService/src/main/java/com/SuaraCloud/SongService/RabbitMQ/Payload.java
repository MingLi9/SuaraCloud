package com.SuaraCloud.SongService.RabbitMQ;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private Long artistId;
    private String title;
    private String Url;
}