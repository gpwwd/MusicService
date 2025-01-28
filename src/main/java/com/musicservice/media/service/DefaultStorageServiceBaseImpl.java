package com.musicservice.media.service;

import com.musicservice.media.configuration.MinioConfiguration;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class DefaultStorageServiceBaseImpl implements StorageServiceBase {

    private final MinioClient minioClient;

    @Value("${minio.put-object-part-size}")
    private Long putObjectPartSize;

    public DefaultStorageServiceBaseImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void save(MultipartFile file, String path, UUID id) throws Exception {
        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(MinioConfiguration.SONG_AUDIO_BUCKET_NAME)
                        .object(path + id.toString())
                        .stream(file.getInputStream(), file.getSize(), putObjectPartSize)
                        .build()
        );
    }

    public void delete(String path, UUID id) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs
                        .builder()
                        .bucket(MinioConfiguration.SONG_AUDIO_BUCKET_NAME)
                        .object(path + id.toString())
                        .build()
        );
    }

    public InputStream getInputStream(String path, UUID id, long offset, long length) throws Exception {
        return minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(MinioConfiguration.SONG_AUDIO_BUCKET_NAME)
                        .offset(offset)
                        .length(length)
                        .object(path + id.toString())
                        .build());
    }
}
