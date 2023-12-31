package com.example.redispub.repository;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    public static final String ROOM_PREFIX = "room";
    public static final String CURRENT_ROOM_PREFIX = "currentRoom";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Transactional
    public void enterRoom(Long roomId, Long memberId) {
        String key = this.createRoomKey(roomId);
        redisTemplate.opsForSet().add(key, memberId);
    }

    @Override
    public Optional<Long> getCurrentRoom(Long memberId) {
        String key = this.createCurrentRoomKey(memberId);
        String value = (String) redisTemplate.opsForValue().get(key);
        if (value != null) return Optional.of(Long.parseLong(value));
        return Optional.empty();
    }

    @Override
    public void removeCurrentRoom(Long memberId) {
        String key = this.createCurrentRoomKey(memberId);
        redisTemplate.delete(key);
    }

    @Override
    public void changeCurrentRoom(Long roomId, Long memberId) {
        String key = this.createCurrentRoomKey(memberId);
        redisTemplate.opsForValue().set(key, roomId);
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
        Set<Object> members = redisTemplate.opsForSet().members(this.createRoomKey(roomId));
        if (members != null) {
            return members.stream()
                    .map(value -> Long.valueOf(value.toString()))
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    public void deleteByMemberId(Long memberId) {

    }

    private String createRoomKey(Long roomId) {
        Assert.notNull(roomId, "roomId can not be null");
        return String.format("%s:%d", ROOM_PREFIX, roomId);
    }

    private String createCurrentRoomKey(Long memberId) {
        Assert.notNull(memberId, "roomId can not be null");
        return String.format("%s:%d", CURRENT_ROOM_PREFIX, memberId);
    }
}
