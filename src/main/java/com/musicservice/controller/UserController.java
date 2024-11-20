package com.musicservice.controller;

import com.musicservice.dto.UserDto;
import com.musicservice.model.User;
import com.musicservice.musicservice.UserServiceImpl;
import com.musicservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/api")
    public void test() {
        System.out.println(userService.getUsers());
    }

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        System.out.println(userService.getUsers());
        try {
            List<UserDto> users = userService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<?> postUser(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        try {
            UserDto user = userService.addUser(userDto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
