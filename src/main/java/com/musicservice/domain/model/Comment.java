package com.musicservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name="comments")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Comment implements Serializable {
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
