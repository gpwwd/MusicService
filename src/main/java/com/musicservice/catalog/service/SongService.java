package com.musicservice.catalog.service;

import com.musicservice.catalog.dto.get.CommentGetDto;
import com.musicservice.catalog.dto.post.SongPostDto;
import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.catalog.dto.post.SongWithImageAndAudioIdPostDto;
import com.musicservice.exception.AudioNotFoundException;
import com.musicservice.exception.SongNotFoundException;
import com.musicservice.domain.model.Comment;
import com.musicservice.domain.model.Song;
import com.musicservice.domain.model.SongAudioMetadataEntity;
import com.musicservice.domain.repository.jpa.AudioFileMetadataRepository;
import com.musicservice.domain.repository.jpa.CommentRepository;
import com.musicservice.domain.repository.jpa.SongRepository;
import com.musicservice.media.service.DefaultSongStorageServiceImpl;
import com.musicservice.media.service.SongStorageService;
import com.musicservice.catalog.mapper.CommentMapperService;
import com.musicservice.catalog.mapper.SongMapperService;
import com.musicservice.media.dto.AudioFileMetadataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SongService {

    private final SongRepository songRepository;
    private final CommentRepository commentRepository;
    private final SongMapperService songMapper;
    private final CommentMapperService commentMapperService;
    private final AudioFileMetadataRepository audioFileMetadataRepository;
    private final SongStorageService songStorageService;

    @Autowired
    public SongService(SongRepository songRepository, SongMapperService songMapper,
                       CommentRepository commentRepository, CommentMapperService commentMapperService,
                       AudioFileMetadataRepository audioFileMetadataRepository, DefaultSongStorageServiceImpl songStorageService) {
        this.songRepository = songRepository;
        this.songMapper = songMapper;
        this.commentRepository = commentRepository;
        this.commentMapperService = commentMapperService;
        this.audioFileMetadataRepository = audioFileMetadataRepository;
        this.songStorageService = songStorageService;
    }

    public List<SongGetDto> getAll() {
        List<Song> songs = songRepository.findAll();
        return songMapper.songsToSongGetDtos(songs);
    }

    public SongGetDto getById(int id) {
        Optional<Song> foundSong = songRepository.findById(id);

        Song song;
        if(foundSong.isPresent()) {
            song = foundSong.get();
        } else {
            throw new SongNotFoundException(id);
        }

        return songMapper.songToSongDtoWithNoComments(song);
    }

    public AudioFileMetadataResponse getAudioFileMetadataBySongId(int id){
        Optional<Song> foundSong = songRepository.findById(id);
        if (foundSong.isEmpty()) {
            throw new SongNotFoundException(id);
        }
        Song existingSong = foundSong.get();
        SongAudioMetadataEntity metadata = existingSong.getAudioMetadata();
        return new AudioFileMetadataResponse(UUID.fromString(metadata.getId()), metadata.getPath());
    }

    public List<CommentGetDto> getCommentsBySongId(int id) {
        List<Comment> comments = commentRepository.findBySongId(id);
        List<CommentGetDto> commentGetDtos = commentMapperService.commentsToCommentDtos(comments);
        return commentGetDtos;
    }

    @Transactional
    public SongPostDto save(SongPostDto songDto) {
        Song song = songMapper.songDtoToSong(songDto);
        Song saved = songRepository.save(song);
        return songMapper.songToSongDto(saved);
    }

    @Transactional
    public SongPostDto save(SongWithImageAndAudioIdPostDto songDto, MultipartFile cover) {
        Song song = songMapper.SongWithImagePostDtoToSong(songDto);

        if(!cover.getOriginalFilename().isEmpty()) {
            song.getImageInfo().setPath(cover.getOriginalFilename());
        } else {
            song.setImageInfo(null);
        }

        SongAudioMetadataEntity audioMetadata = audioFileMetadataRepository.findById(songDto.getAudioMetadataId())
                .orElseThrow(() -> new AudioNotFoundException(songDto.getAudioMetadataId()));

        song.setAudioMetadata(audioMetadata);
        audioMetadata.setSong(song);

        Song saved = songRepository.save(song);
        return songMapper.songToSongDto(saved);
    }

    @Transactional
    public SongPostDto updateSong(int id, SongPostDto songDto) {
        Optional<Song> existingSongOpt = songRepository.findById(id);
        if (existingSongOpt.isEmpty()) {
            throw new SongNotFoundException(id);
        }
        Song existingSong = existingSongOpt.get();
        existingSong.setTitle(songDto.getTitle());
        Song song = songRepository.save(existingSong);
        return songMapper.songToSongDto(song);
    }

    @Transactional
    public void deleteSong(int id) {
        Optional<Song> songToDeleteOptional = songRepository.findById(id);
        if (songToDeleteOptional.isEmpty()) {
            throw new SongNotFoundException(id);
        }
        Song songToDelete = songToDeleteOptional.get();

        String audioMetadataId = songToDelete.getAudioMetadata().getId();
        songStorageService.delete(UUID.fromString(audioMetadataId));

        songRepository.deleteById(id);
    }


}
