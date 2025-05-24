package com.SuaraCloud.SongMetaService.RabbitMQ;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayloadDelete {
    private String url;
}
