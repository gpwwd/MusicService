package com.musicservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoFileMetadataEntity {

    @Id
    private String id;

    private long size;

    private String httpContentType;
}