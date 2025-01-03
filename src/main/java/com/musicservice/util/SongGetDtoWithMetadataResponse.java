package com.musicservice.util;

import com.musicservice.dto.get.SongGetDto;
import com.musicservice.model.SongAudioMetadataEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SongGetDtoWithMetadataResponse {
    private SongGetDto song;
    private AudioFileResponse metadata;
}
