package com.musicservice.user.mapper;

import com.musicservice.user.dto.get.UserGetDto;
import com.musicservice.user.dto.post.UserPostDto;
import com.musicservice.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperService {
    UserGetDto userToUserGetDto(User destination);
    User userPostDtoToUser(UserPostDto source);
}
