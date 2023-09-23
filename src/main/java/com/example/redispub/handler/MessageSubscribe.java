package com.example.redispub.handler;

import com.example.redispub.response.ResponseDto;
import com.example.redispub.service.dto.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;

public class MessageSubscribe implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageSubscribe.class);

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageSubscribe(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatDto chatDto = objectMapper.readValue(message.getBody(), ChatDto.class);
            logger.info("message = {}, pattern = {}", chatDto, pattern);
            logger.info("channel = {}", message.getChannel());
            ResponseDto responseDto = new ResponseDto();

            responseDto.setMemberId(chatDto.getSenderId());
            responseDto.setRoomId(chatDto.getRoomId());
            responseDto.setMessageType(chatDto.getMessageType());
            responseDto.setData(chatDto.getData());

            simpMessagingTemplate.convertAndSend("/topic/room/" + chatDto.getRoomId(), responseDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
