package com.musicservice.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name="artist")
public class Artist {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    //done
    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "artist_genre", joinColumns = @JoinColumn(name = "artist_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<Genre> genres;

    //done
    @OneToMany(mappedBy="artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=false)
    private List<Song> songs;
}
