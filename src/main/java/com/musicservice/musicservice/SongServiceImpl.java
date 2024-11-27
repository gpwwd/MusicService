package com.musicservice.musicservice;

import com.musicservice.dao.SongDao;
import com.musicservice.dao.UserDao;
import com.musicservice.dto.SongDto;
import com.musicservice.model.Song;
import com.musicservice.model.User;
import com.musicservice.repository.RedisSongRepository;
import com.musicservice.service.MapperService;
import com.musicservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    private MapperService mapperService;
    private SongDao songDao;
    private RedisSongRepository redisSongRepository;

    @Autowired
    public SongServiceImpl(MapperService mapperService, SongDao songDao, RedisSongRepository redisSongRepository) {
        this.mapperService = mapperService;
        this.songDao = songDao;
        this.redisSongRepository = redisSongRepository;
    }

    @Override
    public List<SongDto> getSongs() {
        List<Song> songs = songDao.getSongs();
        return mapperService.songsToSongDtos(songs);
    }

    @Override
    public SongDto getSongById(int id) {
        Song cachedSong = redisSongRepository.findSong(id);

        if(cachedSong != null) {
            return mapperService.songToSongDto(cachedSong);
        }

        Song song = songDao.getSongById(id);
        redisSongRepository.add(song);
        return mapperService.songToSongDto(song);
    }

    @Override
    public SongDto addSong(SongDto songDto) {
        Song song = mapperService.songDtoToSong(songDto);
        songDao.addSong(song);
        redisSongRepository.add(song);
        return songDto;
    }

    @Override
    public SongDto updateSong(SongDto songDto) {
        Song song = mapperService.songDtoToSong(songDto);
        songDao.updateSong(song.getId(), song);
        redisSongRepository.delete(song.getId());
        redisSongRepository.add(song);
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
        redisSongRepository.delete(id);
    }
}
