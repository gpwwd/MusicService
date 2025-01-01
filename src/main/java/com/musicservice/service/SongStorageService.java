package com.musicservice.service;

import com.musicservice.musicservice.DefaultSongStorageServiceImpl;
import com.musicservice.util.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface SongStorageService {
    UUID save(MultipartFile video);

    DefaultSongStorageServiceImpl.ChunkWithMetadata fetchChunk(UUID id, Range range);
}
