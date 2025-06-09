package com.SuaraCloud.SongMetaService.RabbitMQ;

import lombok.*;

@Data
@Getter
@Setter
public class PayloadDelete {
    private String url;

    public PayloadDelete(String url) {
        this.url = url;
    }

    public PayloadDelete() {
    }
}
