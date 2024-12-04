package com.musicservice.service;

import com.musicservice.dto.SongDto;
import com.musicservice.dto.SongDtoWithNoComments;
import com.musicservice.dto.UserDto;
import com.musicservice.model.Song;
import com.musicservice.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperService {
    Song songDtoToSong(SongDto source);
    SongDto songToSongDto(Song destination);

    List<SongDto> songsToSongDtos(List<Song> songs);
    List<Song> songDtosToSongs(List<SongDto> songDtos);

    SongDtoWithNoComments songToSongDtoWithNoComments(Song destination);
    List<SongDtoWithNoComments> songsToSongDtosWithNoComments(List<Song> songs);
}
    