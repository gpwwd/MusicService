package com.musicservice.service;

import com.musicservice.dto.SongDto;
import java.util.List;

public interface SongService {
    List<SongDto> getSongs();
    SongDto getSongById(int id);
    SongDto addSong(SongDto songDto);
    SongDto updateSong(SongDto songDto);
    SongDto updateSong(int id, SongDto songDto);
    void deleteSong(int id);
}
