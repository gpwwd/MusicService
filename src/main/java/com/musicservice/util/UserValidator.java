package com.musicservice.util;

import com.musicservice.dao.UserDao;
import com.musicservice.dto.UserDto;
import com.musicservice.model.User;
import com.musicservice.service.UserMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserDao userDao;
    private final UserMapperService userMapperService;

    @Autowired
    public UserValidator(UserDao userDao, UserMapperService userMapperService) {
        this.userDao = userDao;
        this.userMapperService = userMapperService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        User user = userMapperService.userDtoToUser(userDto);
        if(userDao.userExists(user.getEmail())){
            errors.rejectValue("email", "This email address is already in use");
        }
    }
}
