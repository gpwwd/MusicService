package com.musicservice.catalog.dto.post.artist;

import com.musicservice.catalog.dto.get.genre.GenreGetDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter @Setter
public class ArtistPostDto {
    private String name;
    private String description;
    private Set<String> genres;
}
