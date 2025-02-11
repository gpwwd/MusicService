package com.musicservice.catalog.mapper;

import com.musicservice.catalog.dto.get.artist.ArtistGetDto;
import com.musicservice.catalog.dto.get.song.SongGetDto;
import com.musicservice.catalog.dto.post.artist.ArtistPostDto;
import com.musicservice.domain.model.Artist;
import com.musicservice.domain.model.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapperService {
    Artist artistPostDtoToEntity(ArtistPostDto source);
    ArtistGetDto entityToArtistGetDto(Artist destination);
}
