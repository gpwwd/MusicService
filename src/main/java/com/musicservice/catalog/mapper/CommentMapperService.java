package com.musicservice.catalog.mapper;

import com.musicservice.catalog.dto.get.CommentGetDto;
import com.musicservice.domain.model.Comment;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper(componentModel = "spring")
@Primary
public interface CommentMapperService {
    Comment commentDtoToComment(CommentGetDto source);
    CommentGetDto commentToCommentDto(Comment destination);
}
