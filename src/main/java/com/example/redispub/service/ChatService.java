package com.example.redispub.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void sendMessage(Object message) {
        logger.info("chat service message = {}", message);
        redisTemplate.convertAndSend("/chat/sub/", "hello");
    }
}
