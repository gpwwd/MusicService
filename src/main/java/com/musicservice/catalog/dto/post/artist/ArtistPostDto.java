package com.musicservice.catalog.dto.post.artist;

import com.musicservice.catalog.dto.get.genre.GenreGetDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ArtistPostDto {
    private String name;
    private String description;
    private List<Long> genresIds;
}
