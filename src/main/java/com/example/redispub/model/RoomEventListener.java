package com.example.redispub.model;

import com.example.redispub.request.RequestDto;
import com.example.redispub.service.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;

public class RoomEventListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RoomEventListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            logger.info("message = {}, pattern = {}", objectMapper.readValue(message.getBody(), MessageDto.class), pattern);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
