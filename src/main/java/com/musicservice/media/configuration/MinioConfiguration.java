package com.musicservice.media.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfiguration {

    public static final String COVERS_BUCKET_NAME = "covers-bucket";
    public static final String SONG_AUDIO_BUCKET_NAME = "audio-bucket";

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    private boolean bucketExist(MinioClient client, String name) throws Exception {
        return client.bucketExists(BucketExistsArgs.builder().bucket(name).build());
    }

    private void creteBucket(MinioClient client, String name) throws Exception {
        client.makeBucket(
                MakeBucketArgs
                        .builder()
                        .bucket(name)
                        .build()
        );
    }

    @Bean
    public MinioClient minioClient() throws Exception  {
        MinioClient client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        if (!bucketExist(client, COVERS_BUCKET_NAME)) {
            creteBucket(client, COVERS_BUCKET_NAME);
        }

        if(!bucketExist(client, SONG_AUDIO_BUCKET_NAME)) {
            creteBucket(client, SONG_AUDIO_BUCKET_NAME);
        }

        return client;
    }
}