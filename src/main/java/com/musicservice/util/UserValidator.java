package com.musicservice.util;

import com.musicservice.dao.UserDao;
import com.musicservice.dto.get.UserGetDto;
import com.musicservice.dto.post.UserPostDto;
import com.musicservice.model.User;
import com.musicservice.repository.jpa.UserRepository;
import com.musicservice.service.UserMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;
    private final UserMapperService userMapperService;

    @Autowired
    public UserValidator(UserRepository userRepository, UserMapperService userMapperService) {
        this.userRepository = userRepository;
        this.userMapperService = userMapperService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserGetDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserPostDto userDto = (UserPostDto) target;
        User user = userMapperService.userPostDtoToUser(userDto);
        if(userRepository.existsByEmail(user.getEmail())){
            errors.rejectValue("email", "user.email.taken", "This email address is already in use");
        }
    }
}
