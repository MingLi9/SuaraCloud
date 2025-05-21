package com.SuaraCloud.SongMetaService.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage {
    private String name;
    private String actor;
    private JsonNode body;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timestamp;
}
