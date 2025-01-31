package com.musicservice.exception;

public class AudioNotFoundException extends NotFoundException {
    public AudioNotFoundException(String id) {
        super("Song with ID " + id + " not found");
    }
}
