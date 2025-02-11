package com.musicservice.catalog.dto.get.song;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class PostedSongResponseDto {
    SongGetDto songDto;
    UUID audioMetadataId;
    Integer coverId;
    String coverPath;
}
