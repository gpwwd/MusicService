package com.musicservice.exception;

public class SongNotFoundException extends NotFoundException {
    public SongNotFoundException(int id) {
        super("Song with ID " + id + " not found");
    }
}
