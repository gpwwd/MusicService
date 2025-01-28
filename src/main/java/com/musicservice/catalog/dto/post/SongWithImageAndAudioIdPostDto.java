package com.musicservice.catalog.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongWithImageAndAudioIdPostDto {
    private String title;
    private ImageInfoPostDto imageInfo;
    private String audioMetadataId;
}
