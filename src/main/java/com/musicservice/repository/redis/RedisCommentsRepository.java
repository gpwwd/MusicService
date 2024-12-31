package com.musicservice.repository.redis;

import com.musicservice.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisCommentsRepository {
    private final RedisTemplate<String, Comment> redisTemplate;
    private static final String COMMENTS_CACHE_KEY_PREFIX = "comments:";
    private ListOperations<String, Comment> listOperations;

    @Value("${cache.song.ttl}")
    private long songCacheTtl;

    @Autowired
    public RedisCommentsRepository(@Qualifier("commentRedisTemplate") RedisTemplate<String, Comment> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        listOperations = redisTemplate.opsForList();
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Comment.class));
    }

    public void add(final int songId, final List<Comment> comments) {
        if (comments != null && !comments.isEmpty()) {
            listOperations.rightPushAll(COMMENTS_CACHE_KEY_PREFIX + songId, comments);
            redisTemplate.expire(COMMENTS_CACHE_KEY_PREFIX + songId, songCacheTtl, TimeUnit.SECONDS);
        }
    }

    public void delete(final int songId) {
        redisTemplate.delete(COMMENTS_CACHE_KEY_PREFIX + songId);
    }

    public List<Comment> findComments(final int id){
        List<Comment> comments = listOperations.range(COMMENTS_CACHE_KEY_PREFIX + id, 0, -1);
        if(comments.isEmpty()){
            return null;
        }
        return comments;
    }
}