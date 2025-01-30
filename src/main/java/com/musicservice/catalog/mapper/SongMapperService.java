package com.musicservice.catalog.mapper;

import com.musicservice.catalog.dto.post.SongPostDto;
import com.musicservice.catalog.dto.post.SongUpdateDto;
import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.domain.model.Song;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongMapperService {
    Song songDtoToSong(SongUpdateDto source);
    SongUpdateDto songToSongDto(Song destination);

    List<SongUpdateDto> songsToSongDtos(List<Song> songs);
    List<Song> songDtosToSongs(List<SongUpdateDto> songDtos);

    SongGetDto songToSongGetDto(Song destination);
    List<SongGetDto> songsToSongGetDtos(List<Song> songs);

    Song SongPostDtoToEntity(SongPostDto source);
    SongPostDto songToSongWithImagePostDto(Song destination);
}
    