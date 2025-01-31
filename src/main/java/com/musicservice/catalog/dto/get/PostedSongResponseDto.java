package com.musicservice.catalog.dto.get;

import com.musicservice.catalog.dto.post.SongPostDto;
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
