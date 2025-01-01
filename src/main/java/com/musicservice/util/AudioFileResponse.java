package com.musicservice.util;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class AudioFileResponse {
    private UUID fileUuid;
    private String fileName;

    public AudioFileResponse(UUID fileUuid, String fileName) {
        this.fileUuid = fileUuid;
        this.fileName = fileName;
    }
}
