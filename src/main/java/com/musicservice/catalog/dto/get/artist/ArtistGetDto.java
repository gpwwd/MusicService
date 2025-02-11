package com.musicservice.catalog.dto.get.artist;

import com.musicservice.catalog.dto.get.genre.GenreGetDto;
import com.musicservice.domain.model.Genre;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArtistGetDto {
    private Long id;
    private String name;
    private String description;
    private List<GenreGetDto> genres;
}
