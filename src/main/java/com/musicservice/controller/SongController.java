package com.musicservice.controller;

import com.musicservice.dto.SongDto;
import com.musicservice.musicservice.SongServiceImpl;
import com.musicservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {
    private SongService songService;

    @Autowired
    public SongController(SongServiceImpl songService) {
        this.songService = songService;
    }

    @GetMapping()
    public ResponseEntity<?> getSongs() {
        try {
            List<SongDto> songs = songService.getSongs();
            return new ResponseEntity<>(songs, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable("id") int id) {
        SongDto song = songService.getSongById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> postSong(@RequestBody SongDto songDto) {
        try {
            SongDto song = songService.addSong(songDto);
            return new ResponseEntity<>(song, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<?> putSong(@RequestBody SongDto songDto) {
        try {
            SongDto song = songService.updateSong(songDto);
            return new ResponseEntity<>(song, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putSong(@PathVariable int id, @RequestBody SongDto songDto) {
        try {
            SongDto song = songService.updateSong(id, songDto);
            return new ResponseEntity<>(song, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable int id) {
        try {
            songService.deleteSong(id);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
