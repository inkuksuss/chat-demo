package com.example.redispub.handler;

import com.example.redispub.service.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;

public class InitSubscribe implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(InitSubscribe.class);

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public InitSubscribe(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MessageDto messageDto = objectMapper.readValue(message.getBody(), MessageDto.class);
            logger.info("message = {}, pattern = {}",messageDto , pattern);
            logger.info("channel = {}", message.getChannel());

            Long senderId = messageDto.getSenderId();

            simpMessagingTemplate.convertAndSendToUser(messageDto.getName(), "/queue/init", "hello");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
