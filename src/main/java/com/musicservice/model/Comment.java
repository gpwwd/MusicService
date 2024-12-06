package com.musicservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    public Comment() {
    }

    public Comment(Integer id, String comment, int rating) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="text")
    private String comment;

    @Column(name="rating")
    private int rating;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="song_id", referencedColumnName="id", nullable=true)
    private Song song;//owner-side
}
