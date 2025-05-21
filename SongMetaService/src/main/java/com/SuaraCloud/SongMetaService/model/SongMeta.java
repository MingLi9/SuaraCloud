package com.SuaraCloud.SongMetaService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "songmeta")
public class SongMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artist_id",  nullable = false)
    private Long artistId;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "title", nullable = false)
    private String title;
}
