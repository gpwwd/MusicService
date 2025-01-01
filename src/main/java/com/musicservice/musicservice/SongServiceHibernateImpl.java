package com.musicservice.musicservice;

import com.musicservice.configuration.MinioConfiguration;
import com.musicservice.dao.SongDao;
import com.musicservice.dto.get.CommentGetDto;
import com.musicservice.dto.post.SongPostDto;
import com.musicservice.dto.get.SongGetDto;
import com.musicservice.dto.post.SongWithImagePostDto;
import com.musicservice.dto.redis.SongRedisDto;
import com.musicservice.model.Comment;
import com.musicservice.model.Song;
import com.musicservice.repository.redis.RedisCommentsRepository;
import com.musicservice.repository.redis.RedisSongRepository;
import com.musicservice.service.mapper.CommentMapperService;
import com.musicservice.service.mapper.MapperService;
import com.musicservice.service.SongService;
import com.musicservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Deprecated
public class SongServiceHibernateImpl implements SongService {

    private final MapperService mapperService;
    @Qualifier("customCommentMapperServiceImpl")
    private final CommentMapperService commentMapperService;
    private final SongDao songDao;
    private final RedisSongRepository redisSongRepository;
    private final RedisCommentsRepository redisCommentsRepository;
    private final StorageService storageService;
    private final MinioConfiguration minioConfiguration;

    @Autowired
    public SongServiceHibernateImpl(MapperService mapperService, SongDao songDao, RedisSongRepository redisSongRepository
    , RedisCommentsRepository redisCommentsRepository, CommentMapperService commentMapperService
    , StorageService storageService, MinioConfiguration minioConfiguration) {
        this.mapperService = mapperService;
        this.songDao = songDao;
        this.redisSongRepository = redisSongRepository;
        this.redisCommentsRepository = redisCommentsRepository;
        this.commentMapperService = commentMapperService;
        this.storageService = storageService;
        this.minioConfiguration = minioConfiguration;
    }

    @Override
    public List<SongGetDto> getAll() {
        List<Song> songs = songDao.getSongs();
        return mapperService.songsToSongGetDtos(songs);
    }

    @Override
    public SongGetDto getById(int id) {
        SongRedisDto cachedSong = redisSongRepository.findSong(id);

        if(cachedSong != null) {
            Song cached = new Song();
            cached.setId(cachedSong.getId());
            cached.setTitle(cachedSong.getTitle());
            return mapperService.songToSongDtoWithNoComments(cached);
        }

        Song song = songDao.getSongById(id);
        redisSongRepository.add(song);
        return mapperService.songToSongDtoWithNoComments(song);
    }

    @Override
    public SongPostDto save(SongPostDto songDto) {
        Song song = mapperService.songDtoToSong(songDto);
        songDao.addSong(song);
        redisSongRepository.add(song);
        return songDto;
    }

    @Override
    public SongPostDto save(SongWithImagePostDto songDto, MultipartFile cover) {
        return null;
    }

    @Override
    public SongPostDto updateSong(int id, SongPostDto songDto) {
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

    public List<CommentGetDto> getCommentsBySongId(int songId) {
        List<Comment> comments = redisCommentsRepository.findComments(songId);

        if(comments != null) {
            return commentMapperService.commentsToCommentDtos(comments);
        }

        comments = songDao.findCommentsBySongId(songId);
        redisCommentsRepository.add(songId, comments);
        return commentMapperService.commentsToCommentDtos(comments);
    }
}
