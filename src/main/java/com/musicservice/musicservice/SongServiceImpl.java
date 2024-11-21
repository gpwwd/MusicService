package com.musicservice.musicservice;

import com.musicservice.dao.SongDao;
import com.musicservice.dao.UserDao;
import com.musicservice.dto.SongDto;
import com.musicservice.model.Song;
import com.musicservice.model.User;
import com.musicservice.service.MapperService;
import com.musicservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    private MapperService mapperService;
    private SongDao songDao;

    @Autowired
    public SongServiceImpl(MapperService mapperService, SongDao songDao) {
        this.mapperService = mapperService;
        this.songDao = songDao;
    }

    @Override
    public List<SongDto> getSongs() {
        List<Song> songs = songDao.getSongs();
        return mapperService.songsToSongDtos(songs);
    }

    @Override
    public SongDto getSongById(int id) {
        Song song = songDao.getSongById(id);
        return mapperService.songToSongDto(song);
    }

    @Override
    public SongDto addSong(SongDto songDto) {
        Song song = mapperService.songDtoToSong(songDto);
        songDao.addSong(song);
        return songDto;
    }

    @Override
    public SongDto updateSong(SongDto songDto) {
        Song song = mapperService.songDtoToSong(songDto);
        songDao.updateSong(song.getId(), song);
        return songDto;
    }

    @Override
    public SongDto updateSong(int id, SongDto songDto) {
        Song song = mapperService.songDtoToSong(songDto);
        songDao.updateSong(id, song);
        return songDto;
    }

    @Override
    public void deleteSong(int id) {
        songDao.deleteSong(id);
    }
}
