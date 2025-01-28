package com.musicservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="song_audio_metadata")
public class SongAudioMetadataEntity {

    @Id
    private String id;

    private long size;

    private String path;

    @Column(name = "http_content_type")
    private String httpContentType;

    @OneToOne(mappedBy = "audioMetadata", cascade = CascadeType.ALL)
    private Song song;
}