package com.musicservice.catalog.controller;

import com.musicservice.catalog.dto.get.CommentGetDto;
import com.musicservice.catalog.dto.post.SongPostDto;
import com.musicservice.catalog.dto.post.SongUpdateDto;
import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.catalog.service.SongService;
import com.musicservice.media.dto.AudioFileMetadataResponse;
import com.musicservice.media.service.DefaultSongStorageServiceImpl;
import com.musicservice.media.service.SongStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;
    private final SongStorageService songStorageService;

    @Autowired
    public SongController(SongService songService,
                          DefaultSongStorageServiceImpl songStorageService) {
        this.songService = songService;
        this.songStorageService = songStorageService;
    }

    @GetMapping()
    public ResponseEntity<Page<SongGetDto>> getSongs(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Page<SongGetDto> songs = songService.getAll(page, size);
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongGetDto> getSongById(@PathVariable("id") int id) {
        SongGetDto song = songService.getById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @GetMapping("/{id}/metadata")
    public ResponseEntity<AudioFileMetadataResponse> getAudioFileMetadataBySongId(@PathVariable("id") int id) {
        AudioFileMetadataResponse metadata = songService.getAudioFileMetadataBySongId(id);
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Page<CommentGetDto>> getCommentsBySongId(@PathVariable("id") int id,
             @RequestParam(name = "page", defaultValue = "0") Integer page,
             @RequestParam(name = "size", defaultValue = "5") Integer size) {
        Page<CommentGetDto> comments = songService.getCommentsBySongId(id, page, size);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> postSong(
            @RequestPart("songDto") SongPostDto songDto,
            @RequestPart("cover") MultipartFile cover,
            @RequestPart("audio") MultipartFile file) {
        UUID fileUuid = songStorageService.save(file);
        SongUpdateDto song = songService.save(songDto, cover, fileUuid);
        return new ResponseEntity<>(song, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putSong(@PathVariable int id, @RequestBody SongUpdateDto songDto) {
        SongUpdateDto song = songService.updateSong(id, songDto);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable int id) {
        songService.deleteSong(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
