package com.musicservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class UserNotFoundException extends RuntimeException {

    private int id;

    public UserNotFoundException(int id) {
        super("User with ID " + id + " not found");
        this.id = id;
    }
}
