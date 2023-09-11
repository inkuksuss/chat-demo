package com.example.redispub.service;

import com.example.redispub.repository.RedisJpaRepository;
import com.example.redispub.repository.dto.RoomDto;
import com.example.redispub.repository.dto.RoomInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private final RedisJpaRepository redisJpaRepository;

    public ChatService(RedisTemplate<String, Object> redisTemplate, RedisJpaRepository redisJpaRepository) {
        this.redisTemplate = redisTemplate;
        this.redisJpaRepository = redisJpaRepository;
    }

    public void joinRoom() {

    }

    public void sendMessage(Object message) {
        logger.info("chat service message = {}", message);
        redisTemplate.convertAndSend("/chat/sub/", "hello");
    }
}
