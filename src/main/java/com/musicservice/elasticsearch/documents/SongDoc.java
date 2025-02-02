package com.musicservice.elasticsearch.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import com.musicservice.elasticsearch.util.Indices;

@Document(indexName = Indices.SONG_INDEX)
@Setting(settingPath = "/elasticsearch-settings.json")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class SongDoc {
    @Id
    @Field(type = FieldType.Keyword)
    private Integer id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String artist;
}
