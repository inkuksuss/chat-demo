package com.example.redispub.handler;

import com.example.redispub.controller.response.ResponseDto;
import com.example.redispub.service.dto.ChatDto;
import com.example.redispub.service.dto.RoomDetailDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ActionSubscribe implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ActionSubscribe.class);

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ActionSubscribe(SimpMessagingTemplate simpMessagingTemplate) {
        this.objectMapper = new ObjectMapper();
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatDto<RoomDetailDto> chatDto = objectMapper.readValue(message.getBody(), ChatDto.class);

            switch (chatDto.getActionType()) {
                case ROOM_JOIN:
                    break;
                case ROOM_QUIT:
                    break;
                case MESSAGE:
                    break;
                default:
                    break;
            }

            ResponseDto responseDto = new ResponseDto();
            responseDto.setMemberId(chatDto.getMemberId());
            responseDto.setRoomId(chatDto.getRoomId());
            responseDto.setActionType(chatDto.getActionType());
            responseDto.setData(chatDto.getData());

            simpMessagingTemplate.convertAndSend("/topic/room/" + chatDto.getRoomId(), responseDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
