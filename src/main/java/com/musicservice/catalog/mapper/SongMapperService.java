package com.musicservice.catalog.mapper;

import com.musicservice.catalog.dto.post.song.SongPostDto;
import com.musicservice.catalog.dto.post.song.SongUpdateDto;
import com.musicservice.catalog.dto.get.song.SongGetDto;
import com.musicservice.domain.model.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapperService {
    Song songDtoToSong(SongUpdateDto source);
    SongUpdateDto songToSongDto(Song destination);

    SongGetDto songToSongGetDto(Song destination);

    Song SongPostDtoToEntity(SongPostDto source);
}
    