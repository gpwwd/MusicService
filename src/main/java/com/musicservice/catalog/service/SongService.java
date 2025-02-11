package com.musicservice.catalog.service;

import com.musicservice.catalog.dto.get.CommentGetDto;
import com.musicservice.catalog.dto.get.song.PostedSongResponseDto;
import com.musicservice.catalog.dto.post.song.SongPostDto;
import com.musicservice.catalog.dto.post.song.SongUpdateDto;
import com.musicservice.catalog.dto.get.song.SongGetDto;
import com.musicservice.domain.model.ImageInfo;
import com.musicservice.elasticsearch.service.SongIndexingService;
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
import com.musicservice.catalog.dto.get.AudioFileMetadataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final SongIndexingService songIndexingService;

    @Autowired
    public SongService(SongRepository songRepository, SongMapperService songMapper,
                       CommentRepository commentRepository, CommentMapperService commentMapperService,
                       AudioFileMetadataRepository audioFileMetadataRepository, DefaultSongStorageServiceImpl songStorageService,
                       SongIndexingService songIndexingService) {
        this.songRepository = songRepository;
        this.songMapper = songMapper;
        this.commentRepository = commentRepository;
        this.commentMapperService = commentMapperService;
        this.audioFileMetadataRepository = audioFileMetadataRepository;
        this.songStorageService = songStorageService;
        this.songIndexingService = songIndexingService;
    }

    public Page<SongGetDto> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Song> songs = songRepository.findAll(pageRequest);
        return songs.map(songMapper::songToSongGetDto);
    }

    public SongGetDto getById(int id) {
        Song foundSong = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));
        return songMapper.songToSongGetDto(foundSong);
    }

    public AudioFileMetadataResponse getAudioFileMetadataBySongId(int id){
        Song foundSong = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));
        SongAudioMetadataEntity metadata = foundSong.getAudioMetadata();
        return new AudioFileMetadataResponse(UUID.fromString(metadata.getId()), metadata.getPath());
    }

    public Page<CommentGetDto> getCommentsBySongId(int id, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findBySongId(id, pageRequest);
        return comments.map(commentMapperService::commentToCommentDto);
    }

    @Transactional
    protected PostedSongResponseDto save(SongPostDto songDto, MultipartFile cover, UUID audioMetadataId) {
        Song song = songMapper.SongPostDtoToEntity(songDto);

        if (!cover.isEmpty()) {
            if (song.getImageInfo() == null) {
                song.setImageInfo(new ImageInfo());
            }
            song.getImageInfo().setPath(cover.getOriginalFilename());
        } else {
            song.setImageInfo(null);
        }

        SongAudioMetadataEntity audioMetadata = audioFileMetadataRepository.findById(audioMetadataId.toString())
                .orElseThrow(() -> new AudioNotFoundException(audioMetadataId.toString()));

        song.setAudioMetadata(audioMetadata);
        audioMetadata.setSong(song);

        Song saved = songRepository.save(song);

        SongGetDto songResponseDto =  songMapper.songToSongGetDto(saved);
        Integer coverId = (saved.getImageInfo() != null) ? saved.getImageInfo().getId() : null;
        return new PostedSongResponseDto(
                songResponseDto,
                audioMetadataId,
                coverId,
                cover.getOriginalFilename());
    }

    @Transactional
    public PostedSongResponseDto saveSongWithAudioFile(SongPostDto songDto, MultipartFile cover, MultipartFile file) {
        UUID fileUuid = null;
        try {
            fileUuid = songStorageService.save(file);
            PostedSongResponseDto songResponseDto = this.save(songDto, cover, fileUuid);
            songIndexingService.index(songMapper.SongPostDtoToEntity(songDto));
            return songResponseDto;
        } catch (Exception e) {
            if (fileUuid != null) {
                songStorageService.delete(fileUuid);
                /* Откатываем загрузку файла, если произошла проблема в вызове второго метода return this.save(songDto, cover, fileUuid);
                    , так как rollback @Transactional метода fileUuid = songStorageService.save(file)
                    не способен откатить изменения в blob storage, так как это не бд
                */
            }
            throw e;
        }
    }

    @Transactional
    public SongUpdateDto updateSong(int id, SongUpdateDto songDto) {
        Song existingSong = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));

        existingSong.setTitle(songDto.getTitle());
        Song song = songRepository.save(existingSong);
        songIndexingService.index(song);
        return songMapper.songToSongDto(song);
    }

    @Transactional
    public void deleteSong(int id) {
        Song songToDelete = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));

        String audioMetadataId = songToDelete.getAudioMetadata().getId();
        songStorageService.delete(UUID.fromString(audioMetadataId));
        songIndexingService.removeFromIndex(id);
        songRepository.deleteById(id);
    }
}
