package com.musicservice.security.controller;

import com.musicservice.user.dto.get.UserGetDto;
import com.musicservice.user.dto.post.UserPostDto;
import com.musicservice.exception.CustomValidationException;
import com.musicservice.security.service.AuthService;
import com.musicservice.user.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final AuthService authService;

    @Autowired
    public AuthController(UserValidator userValidator,
                          AuthService authService) {
        this.userValidator = userValidator;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserPostDto userDto, BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult);
        }

        UserGetDto user = authService.register(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserPostDto userDto) {
        String status = authService.login(userDto);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }

}
