package com.musicservice.musicservice;

import com.musicservice.dao.SongDao;
import com.musicservice.dao.UserDao;
import com.musicservice.dto.CommentDto;
import com.musicservice.dto.SongDto;
import com.musicservice.model.Comment;
import com.musicservice.model.User;
import com.musicservice.model.Song;
import com.musicservice.repository.RedisCommentsRepository;
import com.musicservice.repository.RedisSongRepository;
import com.musicservice.service.CommentMapperService;
import com.musicservice.service.MapperService;
import com.musicservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    private final MapperService mapperService;
    private final CommentMapperService commentMapperService;
    private final SongDao songDao;
    private final RedisSongRepository redisSongRepository;
    private final RedisCommentsRepository redisCommentsRepository;

    @Autowired
    public SongServiceImpl(MapperService mapperService, SongDao songDao, RedisSongRepository redisSongRepository
    , RedisCommentsRepository redisCommentsRepository, CommentMapperService commentMapperService) {
        this.mapperService = mapperService;
        this.songDao = songDao;
        this.redisSongRepository = redisSongRepository;
        this.redisCommentsRepository = redisCommentsRepository;
        this.commentMapperService = commentMapperService;
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
        redisSongRepository.delete(song.getId());
        redisSongRepository.add(song);
        return songDto;
    }

    @Override
    public void deleteSong(int id) {
        songDao.deleteSong(id);
        redisSongRepository.delete(id);
        redisCommentsRepository.delete(id);
    }

    @Override
    public SongDto getSongWithCommentsById(int id) {
        Song song = songDao.getSongByIdWithComments(id);
        return mapperService.songToSongDto(song);
    }

    public List<CommentDto> getCommentsBySongId(int songId) {
        List<Comment> comments = redisCommentsRepository.findComments(songId);

        if(comments != null) {
            return commentMapperService.commentsToCommentDtos(comments);
        }

        comments = songDao.findCommentsBySongId(songId);
        redisCommentsRepository.add(songId, comments);
        return commentMapperService.commentsToCommentDtos(comments);
    }
}
