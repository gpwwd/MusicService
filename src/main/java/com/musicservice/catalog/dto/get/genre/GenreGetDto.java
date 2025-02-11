package com.musicservice.catalog.dto.get.genre;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreGetDto {
    private Long id;

    private String name;

    private String description;
}
