package com.example.redispub.repository;

import com.example.redispub.entity.RoomInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;
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
    public void enterRoom(Long roomId, Long memberId) {
        String key = this.createRoomKey(roomId);
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForSet().add(key, memberId);
                return operations.exec();
            };
        });
    }

    @Override
    public void leaveRoom(Long roomId, Long memberId) {
        String key = this.createRoomKey(roomId);
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForSet().remove(key, memberId);
                return operations.exec();
            };
        });
    }

    @Override
    public void clearRoom(Long roomId) {
        String key = this.createRoomKey(roomId);
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.delete(key);

                return operations.exec();
            };
        });
    }

    @Override
    public List<RoomInfo> findByMemberId(Long memberId) {
        return null;
    }

    @Override
    public Set<Long> findByRoomId(Long roomId) {
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
