package com.musicservice.service.mapper;

import com.musicservice.dto.get.CommentGetDto;
import com.musicservice.model.Comment;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper(componentModel = "spring")
@Primary
public interface CommentMapperService {
    List<CommentGetDto> commentsToCommentDtos(List<Comment> comments);
    List<Comment> commentDtosToComments(List<CommentGetDto> commentDtos);

    Comment commentDtoToComment(CommentGetDto source);
    CommentGetDto commentToCommentDto(Comment destination);
}
