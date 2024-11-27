package com.musicservice.controller;

import com.musicservice.dto.CommentDto;
import com.musicservice.dto.SongDto;
import com.musicservice.model.Song;
import com.musicservice.musicservice.SongServiceImpl;
import com.musicservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {
    private SongService songService;
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public SongController(SongServiceImpl songService, RedisTemplate<String, String> redisTemplate) {
        this.songService = songService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/test-redis")
    @ResponseStatus(HttpStatus.CREATED)
    public Map.Entry<String, String> setString(@RequestBody Map.Entry<String, String> kvp) {
        redisTemplate.opsForValue().set(kvp.getKey(), kvp.getValue());

        return kvp;
    }

    @GetMapping()
    public ResponseEntity<?> getSongs() {
        List<SongDto> songs = songService.getSongs();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable("id") int id) {
        SongDto song = songService.getSongById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @GetMapping("/with-comments/{id}")
    public ResponseEntity<?> getSongWithCommentsById(@PathVariable("id") int id) {
        SongDto song = songService.getSongWithCommentsById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsBySongId(@PathVariable("id") int id) {
        List<CommentDto> comments = songService.getCommentsBySongId(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> postSong(@RequestBody SongDto songDto) {
        SongDto song = songService.addSong(songDto);
        return new ResponseEntity<>(song, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<?> putSong(@RequestBody SongDto songDto) {
        SongDto song = songService.updateSong(songDto);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putSong(@PathVariable int id, @RequestBody SongDto songDto) {
        SongDto song = songService.updateSong(id, songDto);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable int id) {
        songService.deleteSong(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
