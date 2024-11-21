package com.musicservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class User {
    private int id;
    private String name;
    private List<Song> favouriteSongs;
}
