package com.musicservice.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongWithImagePostDto {
    private String title;
    private ImageInfoPostDto imageInfo;
}
