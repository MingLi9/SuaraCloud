package com.SuaraCloud.SongMetaService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongMetaRequest {
    @Size(max = 100, message = "Url cannot exceed 100 characters")
    private String url;

    @Size(max = 50, message = "Title cannot exceed 50 characters")
    private String title;
}
