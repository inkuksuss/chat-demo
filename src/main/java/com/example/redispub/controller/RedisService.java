package com.example.redispub.controller;

import com.example.redispub.entity.RoomInfo;
import com.example.redispub.repository.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RedisService {

    private final RedisRepository redisRepository;

    public RedisService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public void saveRoom(RoomInfo roomInfo) throws JsonProcessingException {
//        redisRepository.deleteById(roomInfo.getRoomId().toString());
        redisRepository.enterRoom(roomInfo.getRoomId(), roomInfo.getMemberId());
    }

    public List<RoomInfo> findByRoomId(Long roomId) {
//        return redisRepository.findByRoomId(roomId);
        return null;
    }
}
