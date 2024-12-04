package com.musicservice.controller;

import com.musicservice.dto.UserDto;
import com.musicservice.dto.UserDtoWithSongs;
import com.musicservice.exception.CustomValidationException;
import com.musicservice.exception.UserNotFoundException;
import com.musicservice.model.User;
import com.musicservice.musicservice.UserServiceImpl;
import com.musicservice.service.UserService;
import com.musicservice.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserServiceImpl userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        List<UserDto> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id) {
        UserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/favourite-songs")
    public ResponseEntity<?> getUserByIdWithSongs(@PathVariable int userId) {
        UserDtoWithSongs user = userService.getUserWithFavouriteSong(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> postUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult);
        }

        UserDto user = userService.addUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<?> putUser(@RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@PathVariable int id, @RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(id, userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/{userId}/favourite-songs/{songId}")
    public ResponseEntity<?> addFavouriteSong(@PathVariable int userId, @PathVariable int songId) {
        userService.addFavouriteSong(userId, songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
