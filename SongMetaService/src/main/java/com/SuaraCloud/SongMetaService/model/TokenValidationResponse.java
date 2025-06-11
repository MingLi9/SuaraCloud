package com.SuaraCloud.SongMetaService.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private String userId;
    public boolean isValid() {
        return valid;
    }
    public String getUserId() {
        return userId;
    }
}
