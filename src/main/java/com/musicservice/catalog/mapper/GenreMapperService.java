package com.musicservice.catalog.mapper;

import com.musicservice.catalog.dto.get.artist.ArtistGetDto;
import com.musicservice.catalog.dto.get.genre.GenreGetDto;
import com.musicservice.catalog.dto.post.artist.ArtistPostDto;
import com.musicservice.domain.model.Artist;
import com.musicservice.domain.model.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapperService {
    GenreGetDto entityToArtistGetDto(Genre destination);
}
