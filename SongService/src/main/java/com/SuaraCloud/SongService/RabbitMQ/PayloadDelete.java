package com.SuaraCloud.SongService.RabbitMQ;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayloadDelete {
    private String url;
}
