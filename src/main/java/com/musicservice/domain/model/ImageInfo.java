package com.musicservice.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "image_info")
public class ImageInfo {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String path;

    @OneToOne(mappedBy = "imageInfo", cascade = CascadeType.ALL)
    private Song song;
}
