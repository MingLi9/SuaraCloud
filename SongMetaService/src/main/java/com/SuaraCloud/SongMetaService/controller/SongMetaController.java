package com.SuaraCloud.SongMetaService.controller;
import com.SuaraCloud.SongMetaService.dto.SongMetaDto;
import com.SuaraCloud.SongMetaService.dto.SongMetaRequest;
import com.SuaraCloud.SongMetaService.service.SongMetaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songmeta")
public class SongMetaController {

    private final SongMetaService songMetaService;

    @Autowired
    public SongMetaController(SongMetaService songMetaService) {
        this.songMetaService = songMetaService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public ResponseEntity<List<SongMetaDto>> getAllSongMeta() {
        return ResponseEntity.ok(songMetaService.getAllSongMetas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongMetaDto> getSongMetaById(@PathVariable Long id) {
        return ResponseEntity.ok(songMetaService.getSongMetaById(id));
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<List<SongMetaDto>> getSongMetasByArtistId(@PathVariable Long id) {
        return ResponseEntity.ok(songMetaService.getSongMetasByArtistId(id));
    }

    @PostMapping
    public ResponseEntity<SongMetaDto> createSongMeta(HttpServletRequest request, @Valid @RequestBody SongMetaRequest songMetaRequest) {
        String userIdString = (String) request.getAttribute("userId");
        Long artistId = Long.parseLong(userIdString);
        SongMetaDto createdUser = songMetaService.createSongMeta(artistId, songMetaRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongMetaDto> updateSongMeta(HttpServletRequest request, @PathVariable Long id, @Valid @RequestBody SongMetaRequest songMetaRequest) {
        String userIdString = (String) request.getAttribute("userId");
        Long artistId = Long.parseLong(userIdString);
        return ResponseEntity.ok(songMetaService.updateSongMeta(artistId, id, songMetaRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSongMeta(HttpServletRequest request, @PathVariable Long id) {
        String userIdString = (String) request.getAttribute("userId");
        Long artistId = Long.parseLong(userIdString);
        songMetaService.deleteSongMeta(artistId, id);
        return ResponseEntity.noContent().build();
    }
}
