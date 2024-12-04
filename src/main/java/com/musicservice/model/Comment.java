package com.musicservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter @Setter
@RedisHash
@Entity
@Table(name="comments")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="text")
    private String comment;

    @Column(name="rating")
    private int rating;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="song_id", referencedColumnName="id", nullable=false)
    private Song song;//owner-side
}
