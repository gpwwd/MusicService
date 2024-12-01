package com.musicservice.service;

import com.musicservice.dto.CommentDto;
import com.musicservice.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper(componentModel = "spring")
@Primary
public interface CommentMapperService {
    List<CommentDto> commentsToCommentDtos(List<Comment> comments);
    List<Comment> commentDtosToComments(List<CommentDto> commentDtos);

    Comment commentDtoToComment(CommentDto source);
    CommentDto commentToCommentDto(Comment destination);
}
