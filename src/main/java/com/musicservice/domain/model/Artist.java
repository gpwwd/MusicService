package com.musicservice.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    @ManyToMany(fetch = FetchType.EAGER, cascade = {})
    @JoinTable(name="artist_genre",
            joinColumns=  @JoinColumn(name="genre_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="artist_id", referencedColumnName="id") )
    private List<Genre> genres;

    //done
    @OneToMany(mappedBy="artist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=false)
    private List<Song> songs;
}
