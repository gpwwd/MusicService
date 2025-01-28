package com.musicservice.domain.repository.redis;

import com.musicservice.domain.model.SongRedisEntity;
import com.musicservice.domain.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisSongRepository {
    private final RedisTemplate<String, SongRedisEntity> redisTemplate;
    private static final String SONG_CACHE_KEY_PREFIX = "song:";
    private ValueOperations<String, SongRedisEntity> valueOperations;

    @Value("${cache.song.ttl}")
    private long songCacheTtl;

    @Autowired
    public RedisSongRepository(@Qualifier("songRedisTemplate")RedisTemplate<String, SongRedisEntity> songRedisTemplate) {
        this.redisTemplate = songRedisTemplate;
        valueOperations = redisTemplate.opsForValue();
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SongRedisEntity.class));
    }

    @PostConstruct
    private void init(){
    }

    public void add(final Song song) {
        SongRedisEntity songDtoRedis = new SongRedisEntity();
        songDtoRedis.setTitle(song.getTitle());
        songDtoRedis.setId(song.getId());
        valueOperations.set(SONG_CACHE_KEY_PREFIX + songDtoRedis.getId(), songDtoRedis, songCacheTtl, TimeUnit.SECONDS);
    }

    public void delete(final int id) {
        redisTemplate.delete(SONG_CACHE_KEY_PREFIX + id);
    }

    public SongRedisEntity findSong(final int id){
        var cached = valueOperations.get(SONG_CACHE_KEY_PREFIX + id);
        return (SongRedisEntity)cached;
    }
}
