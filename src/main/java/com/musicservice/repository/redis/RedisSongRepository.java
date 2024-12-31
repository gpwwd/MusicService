package com.musicservice.repository.redis;

import com.musicservice.dto.redis.SongRedisDto;
import com.musicservice.model.Song;
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
    private final RedisTemplate<String, SongRedisDto> redisTemplate;
    private static final String SONG_CACHE_KEY_PREFIX = "song:";
    private ValueOperations<String, SongRedisDto> valueOperations;

    @Value("${cache.song.ttl}")
    private long songCacheTtl;

    @Autowired
    public RedisSongRepository(@Qualifier("songRedisTemplate")RedisTemplate<String, SongRedisDto> songRedisTemplate) {
        this.redisTemplate = songRedisTemplate;
        valueOperations = redisTemplate.opsForValue();
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SongRedisDto.class));
    }

    @PostConstruct
    private void init(){
    }

    public void add(final Song song) {
        SongRedisDto songDtoRedis = new SongRedisDto();
        songDtoRedis.setTitle(song.getTitle());
        songDtoRedis.setId(song.getId());
        valueOperations.set(SONG_CACHE_KEY_PREFIX + songDtoRedis.getId(), songDtoRedis, songCacheTtl, TimeUnit.SECONDS);
    }

    public void delete(final int id) {
        redisTemplate.delete(SONG_CACHE_KEY_PREFIX + id);
    }

    public SongRedisDto findSong(final int id){
        var cached = valueOperations.get(SONG_CACHE_KEY_PREFIX + id);
        return (SongRedisDto)cached;
    }
}
