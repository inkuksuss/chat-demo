package com.example.redispub.controller;

import com.example.redispub.repository.RedisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class RedisService {

    private final RedisRepository redisRepository;

    public RedisService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Transactional
    public void enterRoom(Long roomId, Long memberId) {
        redisRepository.enterRoom(roomId, memberId);
    }

    public Set<Long> findByRoomId(Long roomId) {
        return redisRepository.findMemberIdListByRoomId(roomId);
    }
}
