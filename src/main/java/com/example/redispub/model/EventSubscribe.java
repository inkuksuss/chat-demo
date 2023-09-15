package com.example.redispub.model;

import com.example.redispub.service.dto.MessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EventSubscribe implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(EventSubscribe.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SimpMessagingTemplate simpMessagingTemplate;

    public EventSubscribe(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MessageDto messageDto = objectMapper.readValue(message.getBody(), MessageDto.class);
            logger.info("message = {}, pattern = {}",messageDto , pattern);
            logger.info("channel = {}", message.getChannel());

            simpMessagingTemplate.convertAndSend("/topic/room/1", messageDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
