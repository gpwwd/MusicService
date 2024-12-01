package com.musicservice.util;

import com.musicservice.dao.UserDao;
import com.musicservice.dto.UserDto;
import com.musicservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserDao userDao;
    

    @Autowired
    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if(userDao.userExists(user.getEmail())){
            errors.rejectValue("email", "This email address is already in use");
        }
    }
}
