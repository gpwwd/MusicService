package com.musicservice.media.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class AudioFileMetadataResponse {
    private UUID fileUuid;
    private String fileName;

    public AudioFileMetadataResponse(UUID fileUuid, String fileName) {
        this.fileUuid = fileUuid;
        this.fileName = fileName;
    }
}
