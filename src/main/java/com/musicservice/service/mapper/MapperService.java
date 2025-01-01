package com.musicservice.service.mapper;

import com.musicservice.dto.post.SongPostDto;
import com.musicservice.dto.get.SongGetDto;
import com.musicservice.dto.post.SongWithImagePostDto;
import com.musicservice.model.Song;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperService {
    Song songDtoToSong(SongPostDto source);
    SongPostDto songToSongDto(Song destination);

    List<SongPostDto> songsToSongDtos(List<Song> songs);
    List<Song> songDtosToSongs(List<SongPostDto> songDtos);

    SongGetDto songToSongDtoWithNoComments(Song destination);
    List<SongGetDto> songsToSongGetDtos(List<Song> songs);

    Song SongWithImagePostDtoToSong(SongWithImagePostDto source);
    SongWithImagePostDto songToSongWithImagePostDto(Song destination);
}
    