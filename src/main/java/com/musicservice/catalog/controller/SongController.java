package com.musicservice.catalog.controller;

import com.musicservice.catalog.dto.get.CommentGetDto;
import com.musicservice.catalog.dto.get.song.PostedSongResponseDto;
import com.musicservice.catalog.dto.post.song.SongPostDto;
import com.musicservice.catalog.dto.post.song.SongUpdateDto;
import com.musicservice.catalog.dto.get.song.SongGetDto;
import com.musicservice.catalog.service.SongService;
import com.musicservice.catalog.dto.get.AudioFileMetadataResponse;
import com.musicservice.elasticsearch.service.SongSearchElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;
    private final SongSearchElasticService songSearchElasticService;

    @Autowired
    public SongController(SongService songService, SongSearchElasticService songSearchElasticService) {
        this.songService = songService;
        this.songSearchElasticService = songSearchElasticService;
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

    @Transactional
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PostedSongResponseDto> postSong(
            @RequestPart("songDto") SongPostDto songDto,
            @RequestPart("cover") MultipartFile cover,
            @RequestPart("audio") MultipartFile file) {
        PostedSongResponseDto response = songService.saveSongWithAudioFile(songDto, cover, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    @GetMapping("/search")
    public ResponseEntity<Page<SongGetDto>> search(
            @RequestParam("query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<SongGetDto> songs = songSearchElasticService.searchSongsViaElastic(query, page, size);
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }
}
