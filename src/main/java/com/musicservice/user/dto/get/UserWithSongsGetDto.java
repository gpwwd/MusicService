package com.musicservice.user.dto.get;

import com.musicservice.catalog.dto.get.SongGetDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UserWithSongsGetDto {
    private int id;

    private String name;

    private String email;

    private List<SongGetDto> favouriteSongs;
}
