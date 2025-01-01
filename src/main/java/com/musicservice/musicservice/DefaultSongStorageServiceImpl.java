package com.musicservice.musicservice;

import com.musicservice.exception.StorageException;
import com.musicservice.model.SongFileMetadataEntity;
import com.musicservice.repository.jpa.VideoFileMetadataRepository;
import com.musicservice.service.StorageService;
import com.musicservice.service.SongStorageService;
import com.musicservice.util.Range;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class DefaultSongStorageServiceImpl implements SongStorageService {

    private final StorageService storageService;

    private final VideoFileMetadataRepository fileMetadataRepository;

    @Autowired
    public DefaultSongStorageServiceImpl(StorageService storageService, VideoFileMetadataRepository fileMetadataRepository) {
        this.storageService = storageService;
        this.fileMetadataRepository = fileMetadataRepository;
    }

    public record ChunkWithMetadata(
            SongFileMetadataEntity metadata,
            byte[] chunk
    ) {}

    @Override
    public ChunkWithMetadata fetchChunk(UUID uuid, Range range) {
        SongFileMetadataEntity fileMetadata = fileMetadataRepository.findById(uuid.toString()).orElseThrow();
        return new ChunkWithMetadata(fileMetadata, readChunk(fileMetadata.getPath(), uuid, range, fileMetadata.getSize()));
    }

    @Override
    @Transactional
    public UUID save(MultipartFile video) {
        try {
            UUID fileUuid = UUID.randomUUID();
            // аннотация builder в классе VideoFileMetadataEntity
            SongFileMetadataEntity metadata = SongFileMetadataEntity.builder()
                    .id(fileUuid.toString())
                    .size(video.getSize())
                    .path(video.getOriginalFilename())
                    .httpContentType(video.getContentType())
                    .build();

            fileMetadataRepository.save(metadata);
            storageService.save(video, video.getOriginalFilename(), fileUuid);
            return fileUuid;
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    private byte[] readChunk(String path, UUID uuid, Range range, long fileSize) {
        long startPosition = range.getRangeStart();
        long endPosition = range.getRangeEnd(fileSize);
        int chunkSize = (int) (endPosition - startPosition + 1);
        try(InputStream inputStream = storageService.getInputStream(path, uuid, startPosition, chunkSize)) {
            return inputStream.readAllBytes();
        } catch (Exception exception) {
            throw new StorageException(exception);
        }
    }

}
