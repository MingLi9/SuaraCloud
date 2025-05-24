package com.SuaraCloud.SongMetaService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongMetaDto {
    private Long id;
    private Long artistId;
    private String url;
    private String title;
}