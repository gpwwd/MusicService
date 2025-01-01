package com.musicservice.musicservice;

import com.musicservice.configuration.MinioConfiguration;
import com.musicservice.service.StorageService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.put-object-part-size}")
    private Long putObjectPartSize;

    public StorageServiceImpl(MinioClient minioClient) {
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



    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    private void createBucket(String bucketName) throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    public String uploadFile(String bucketName, String objectNamePath, InputStream inputStream, String contentType) {
        try {
            boolean found = bucketExists(bucketName);
            if (!found) {
                this.createBucket(bucketName);
           }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectNamePath)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build());
            return objectNamePath;
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading file: " + e.getMessage());
        }
    }

    public InputStream getFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving file: " + e.getMessage());
        }
    }
}
