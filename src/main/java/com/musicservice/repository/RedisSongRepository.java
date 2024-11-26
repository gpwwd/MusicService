package com.musicservice.repository;

import com.musicservice.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisSongRepository {
    private RedisTemplate<String, Song> redisTemplate;
    private static final String SONG_CACHE_KEY_PREFIX = "song:";
    private ValueOperations valueOperations;

    @Value("${cache.song.ttl}")
    private long songCacheTtl;

    @Autowired
    public RedisSongRepository(RedisTemplate<String, Song> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        valueOperations = redisTemplate.opsForValue();
    }

    public void add(final Song song) {
        valueOperations.set(SONG_CACHE_KEY_PREFIX + song.getId(), song, songCacheTtl, TimeUnit.SECONDS);
    }

    public void delete(final int id) {
        redisTemplate.delete(SONG_CACHE_KEY_PREFIX + id);
    }

    public Song findSong(final int id){
        return (Song) valueOperations.get(SONG_CACHE_KEY_PREFIX + id);
    }
}
