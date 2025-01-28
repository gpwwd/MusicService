package com.musicservice.media.service;

import com.musicservice.media.util.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface SongStorageService {
    UUID save(MultipartFile video);

    DefaultSongStorageServiceImpl.ChunkWithMetadata fetchChunk(UUID id, Range range);

    void delete(UUID fileUuid);
}
