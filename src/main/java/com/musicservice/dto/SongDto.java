package com.musicservice.dto;

import com.musicservice.model.Comment;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SongDto {
    private int id;
    private String title;
    private List<CommentDto> comments;
}
