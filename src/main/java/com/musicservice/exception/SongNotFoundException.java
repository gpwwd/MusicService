package com.musicservice.exception;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(int id) {
        super("Song with ID " + id + " not found");
    }
}
