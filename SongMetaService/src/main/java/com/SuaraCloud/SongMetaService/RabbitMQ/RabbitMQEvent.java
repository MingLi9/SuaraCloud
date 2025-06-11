package com.SuaraCloud.SongMetaService.RabbitMQ;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
public class RabbitMQEvent<T> {
    private String name;
    private T body;
    private String actor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timestamp;

    public RabbitMQEvent(String name, T body, String actor, ZonedDateTime timestamp) {
        this.name = name;
        this.body = body;
        this.actor = actor;
        this.timestamp = timestamp;
    }
}
