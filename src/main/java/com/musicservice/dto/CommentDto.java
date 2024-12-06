package com.musicservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class CommentDto implements Serializable {
    private Integer id;

    public CommentDto() {
    }

    public CommentDto(Integer id, String comment, int rating) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
    }

    private String comment;
    private int rating;
}
