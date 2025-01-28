package com.musicservice.catalog.dto.get;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class CommentGetDto implements Serializable {
    public CommentGetDto() {
    }

    public CommentGetDto(Integer id, String comment, int rating) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
    }

    private Integer id;
    private String comment;
    private int rating;
}
