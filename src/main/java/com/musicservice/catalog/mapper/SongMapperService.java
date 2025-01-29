package com.musicservice.catalog.mapper;

import com.musicservice.catalog.dto.post.SongPostDto;
import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.catalog.dto.post.SongWithImageAndAudioIdPostDto;
import com.musicservice.domain.model.Song;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongMapperService {
    Song songDtoToSong(SongPostDto source);
    SongPostDto songToSongDto(Song destination);

    List<SongPostDto> songsToSongDtos(List<Song> songs);
    List<Song> songDtosToSongs(List<SongPostDto> songDtos);

    SongGetDto songToSongGetDto(Song destination);
    List<SongGetDto> songsToSongGetDtos(List<Song> songs);

    Song SongWithImagePostDtoToSong(SongWithImageAndAudioIdPostDto source);
    SongWithImageAndAudioIdPostDto songToSongWithImagePostDto(Song destination);
}
    