package com.musicservice.exception;

public class SongAlreadyFavouriteException extends RuntimeException {
    public SongAlreadyFavouriteException(int songId) {
        super("Song with ID " + songId + " is already in the list of favourites.");
    }
}