package com.musicservice.exception;

public class SongNotFoundException extends RuntimeException {
    private int id;

    public SongNotFoundException(int id) {
        super("Song with ID " + id + " not found");
        this.id = id;
    }
}
