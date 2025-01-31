package com.musicservice.catalog.dto.get;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class CommentGetDto implements Serializable {
    private Integer id;
    private String comment;
    private int rating;
}
