package com.example.redispub.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    public static final String ROOM_PREFIX = "room";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public void enterRoom(Long roomId, Long memberId) {
        String key = this.createRoomKey(roomId);
        redisTemplate.opsForSet().add(key, memberId);
    }

    @Override
    @Transactional
    public void leaveRoom(Long roomId, Long memberId) {
        String key = this.createRoomKey(roomId);
        redisTemplate.opsForSet().remove(key, memberId);
    }

    @Override
    @Transactional
    public void clearRoom(Long roomId) {
        String key = this.createRoomKey(roomId);
        redisTemplate.delete(key);
    }

    @Override
    public Set<Long> findMemberIdListByRoomId(Long roomId) {
        return redisTemplate.opsForSet().members(this.createRoomKey(roomId)).stream()
                .map(value -> Long.valueOf(value.toString()))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteByMemberId(Long memberId) {

    }

    private String createRoomKey(Long roomId) {
        Assert.notNull(roomId, "roomId can not be null");
        return String.format("%s:%d", ROOM_PREFIX, roomId);
    }
}
