package com.example.redispub.repository;

import com.example.redispub.entity.RoomInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final HashOperations<String, String, String> operations;

    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.operations = redisTemplate.opsForHash();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void saveRoomInfo(RoomInfo roomInfo) throws JsonProcessingException {
        operations.put(UUID.randomUUID().toString(), roomInfo.getRoomId().toString(), objectMapper.writeValueAsString(roomInfo));
    }

    @Override
    public List<RoomInfo> findByMemberId(Long memberId) {
        return null;
    }

    @Override
    public List<RoomInfo> findByRoomId(Long roomId) {
        List<String> values = operations.values(roomId.toString());

//        redisTemplate.opsForValue().getOperations().
        return null;
    }

    @Override
    public void deleteByMemberId(Long memberId) {

    }
}
