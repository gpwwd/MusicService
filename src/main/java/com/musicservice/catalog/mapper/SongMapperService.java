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

    SongGetDto songToSongGetDto(Song destination);

    Song SongPostDtoToEntity(SongPostDto source);
}
    