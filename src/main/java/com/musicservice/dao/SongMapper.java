package com.musicservice.dao;

import com.musicservice.model.Comment;
import com.musicservice.model.Song;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongMapper implements RowMapper<Song> {
    @Override
    public Song mapRow(ResultSet rs, int rowNum) throws SQLException {
        Song song = new Song();
        song.setId(rs.getInt("id"));
        song.setTitle(rs.getString("title"));

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(rs.getInt("comment_id"));
        comment.setComment(rs.getString("comment_text"));

        if (comment.getId() != null) {
            comments.add(comment);
        }

        song.setComments(comments);
        return song;
    }
}