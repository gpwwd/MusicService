package com.musicservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test-security")
public class TestController {
    @GetMapping()
    public ResponseEntity<String> getUsers() {
        return new ResponseEntity<>("test for authentication", HttpStatus.OK);
    }
}
