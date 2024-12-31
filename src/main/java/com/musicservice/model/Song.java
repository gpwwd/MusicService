package com.musicservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

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
}
