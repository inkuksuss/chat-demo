package com.example.redispub.repository;

import com.example.redispub.entity.RoomInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    public static final String ROOM_PREFIX = "room";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final HashOperations<String, String, String> hashOperations;
    private final SetOperations<String, Object> setOperations;

    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
        this.hashOperations = redisTemplate.opsForHash();
        this.setOperations = redisTemplate.opsForSet();
    }

    @Override
    public void addMemberInRoom(Long roomId, Long memberId) {
        String key = this.createRoomKey(roomId);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                Map<String, Object> paramMap = new HashMap<>();
//                paramMap.put("currentMemberList", )



                operations.opsForHash().putAll(key, paramMap);
                return operations.exec();
            }
        });


        setOperations.add();
    }

    @Override
    public List<RoomInfo> findByMemberId(Long memberId) {
        return null;
    }

    @Override
    public List<RoomInfo> findByRoomId(Long roomId) {
        hashOperations.get(this.createRoomKey(roomId));

//        redisTemplate.opsForValue().getOperations().
        return null;
    }

    @Override
    public void deleteByMemberId(Long memberId) {

    }

    private String createRoomKey(Long roomId) {
        Assert.notNull(roomId, "roomId can not be null");
        return ROOM_PREFIX + ":" + roomId;
    }
}
