package com.musicservice.controller;

import com.musicservice.dto.get.CommentGetDto;
import com.musicservice.dto.post.SongPostDto;
import com.musicservice.dto.get.SongGetDto;
import com.musicservice.dto.post.SongWithImagePostDto;
import com.musicservice.musicservice.SongServiceImpl;
import com.musicservice.service.SongService;
import com.musicservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;

    @Autowired
    public SongController(SongServiceImpl songService) {
        this.songService = songService;
    }

    @GetMapping()
    public ResponseEntity<?> getSongs() {
        List<SongGetDto> songs = songService.getAll();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable("id") int id) {
        SongGetDto song = songService.getById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsBySongId(@PathVariable("id") int id) {
        List<CommentGetDto> comments = songService.getCommentsBySongId(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> postSong(@RequestBody SongPostDto songDto) {
        SongPostDto song = songService.save(songDto);
        return new ResponseEntity<>(song, HttpStatus.CREATED);
    }

    @PostMapping(value = "/with-cover", consumes = {"multipart/form-data"})
    public ResponseEntity<?> postSong(@RequestPart SongWithImagePostDto songDto, @RequestPart("file") MultipartFile cover) {
        SongPostDto song = songService.save(songDto, cover);
        return new ResponseEntity<>(song, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putSong(@PathVariable int id, @RequestBody SongPostDto songDto) {
        SongPostDto song = songService.updateSong(id, songDto);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable int id) {
        songService.deleteSong(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
