package com.musicservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="song_audio_metadata")
public class SongFileMetadataEntity {

    @Id
    private String id;

    private long size;

    private String path;

    @Column(name = "http_content_type")
    private String httpContentType;
}