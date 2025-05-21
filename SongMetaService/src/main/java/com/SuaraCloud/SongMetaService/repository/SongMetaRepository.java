package com.SuaraCloud.SongMetaService.repository;

import com.SuaraCloud.SongMetaService.model.SongMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongMetaRepository extends JpaRepository<SongMeta, Long> {
    boolean existsByUrl(String url);
    SongMeta findSongMetaByUrl(String url);
    boolean existsByArtistId(Long artistId);
    List<SongMeta> findSongMetaByArtistId(Long artistId);
}