package com.musicservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name="songs")
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name="user_songs",
            joinColumns=  @JoinColumn(name="song_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="user_id", referencedColumnName="id") )
    List<User> users;

    @OneToMany(mappedBy="song", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_info_id", referencedColumnName = "id")
    private ImageInfo imageInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "audio_metadata_id", referencedColumnName = "id")
    private SongAudioMetadataEntity audioMetadata;

    //done
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="artist_id", referencedColumnName="id", nullable=true)
    private Artist artist;

    //done
    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "song_genre", joinColumns = @JoinColumn(name = "song_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<Genre> genres;
}
