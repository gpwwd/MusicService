package com.musicservice.media.service;

import com.musicservice.exception.AudioNotFoundException;
import com.musicservice.exception.StorageException;
import com.musicservice.domain.model.SongAudioMetadataEntity;
import com.musicservice.domain.repository.jpa.AudioFileMetadataRepository;
import com.musicservice.media.util.Range;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultSongStorageServiceImpl implements SongStorageService {

    private final StorageServiceBase storageServiceBase;

    private final AudioFileMetadataRepository fileMetadataRepository;

    @Autowired
    public DefaultSongStorageServiceImpl(StorageServiceBase storageServiceBase, AudioFileMetadataRepository fileMetadataRepository) {
        this.storageServiceBase = storageServiceBase;
        this.fileMetadataRepository = fileMetadataRepository;
    }

    // transactional потому что, если save самого аудио пройдет с ошибкой,
    // то метаданные могут остаться в
    // fileMetadataRepository, что недопустимо
    @Override
    @Transactional
    public UUID save(MultipartFile audio) {
        try {
            UUID fileUuid = UUID.randomUUID();
            // аннотация builder в классе VideoFileMetadataEntity
            SongAudioMetadataEntity metadata = SongAudioMetadataEntity.builder()
                    .id(fileUuid.toString())
                    .size(audio.getSize())
                    .path(audio.getOriginalFilename())
                    .httpContentType(audio.getContentType())
                    .build();

            fileMetadataRepository.save(metadata);
            storageServiceBase.save(audio, audio.getOriginalFilename(), fileUuid);
            return fileUuid;
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    @Transactional
    public void delete(UUID fileUuid) {
        Optional<SongAudioMetadataEntity> metadataOpt = fileMetadataRepository.findById(fileUuid.toString());
        if (metadataOpt.isEmpty()) {
            throw new AudioNotFoundException(fileUuid.toString());
        }
        SongAudioMetadataEntity metadata = metadataOpt.get();

        try {
            storageServiceBase.delete(metadata.getPath(), fileUuid);
        } catch (Exception e) {
            throw new StorageException(e);
        }

        fileMetadataRepository.delete(metadata);
    }


    @Override
    public ChunkWithMetadata fetchChunk(UUID uuid, Range range) {
        SongAudioMetadataEntity fileMetadata = fileMetadataRepository.findById(uuid.toString()).orElseThrow();
        return new ChunkWithMetadata(fileMetadata, readChunk(fileMetadata.getPath(), uuid, range, fileMetadata.getSize()));
    }


    private byte[] readChunk(String path, UUID uuid, Range range, long fileSize) {
        long startPosition = range.getRangeStart();
        long endPosition = range.getRangeEnd(fileSize);
        int chunkSize = (int) (endPosition - startPosition + 1);
        try(InputStream inputStream = storageServiceBase.getInputStream(path, uuid, startPosition, chunkSize)) {
            return inputStream.readAllBytes();
        } catch (Exception exception) {
            throw new StorageException(exception);
        }
    }

    public record ChunkWithMetadata(
            SongAudioMetadataEntity metadata,
            byte[] chunk
    ) {}

}
