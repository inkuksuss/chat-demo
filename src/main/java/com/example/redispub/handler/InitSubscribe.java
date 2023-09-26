package com.example.redispub.handler;

import com.example.redispub.enums.ActionType;
import com.example.redispub.controller.response.ResponseDto;
import com.example.redispub.service.dto.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.List;

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
            ChatDto<List<Long>> chatDto = objectMapper.readValue(message.getBody(), ChatDto.class);

            ResponseDto responseDto = new ResponseDto();
            responseDto.setActionType(ActionType.ROOM_INIT);
            responseDto.setMemberId(chatDto.getMemberId());
            responseDto.setData(chatDto.getData());

            simpMessagingTemplate.convertAndSendToUser(chatDto.getPrincipalName(), "/queue/init", objectMapper.writeValueAsString(responseDto));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
