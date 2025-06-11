package com.SuaraCloud.SongMetaService.model;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationResponse {
    private String id;
    private String tokenid;
    private String firstName;
    private String lastName;
    private String email;

    public String getId(){
        return id;
    }
}
