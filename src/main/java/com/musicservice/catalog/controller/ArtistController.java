package com.musicservice.catalog.controller;

import com.musicservice.catalog.dto.get.artist.ArtistGetDto;
import com.musicservice.catalog.dto.get.song.PostedSongResponseDto;
import com.musicservice.catalog.dto.post.artist.ArtistPostDto;
import com.musicservice.catalog.dto.post.song.SongPostDto;
import com.musicservice.catalog.service.ArtistService;
import com.musicservice.user.dto.post.UserPostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping()
    public ResponseEntity<ArtistGetDto> postArtist(@RequestBody ArtistPostDto artistDto) {
        ArtistGetDto response = artistService.save(artistDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
