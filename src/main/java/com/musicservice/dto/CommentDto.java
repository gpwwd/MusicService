package com.musicservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class CommentDto implements Serializable {
    private Integer id;
    private String comment;
    private int rating;
}
