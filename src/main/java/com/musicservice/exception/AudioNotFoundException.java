package com.musicservice.exception;

public class AudioNotFoundException extends RuntimeException {
    public AudioNotFoundException(String id) {
        super("Song with ID " + id + " not found");
    }
}
