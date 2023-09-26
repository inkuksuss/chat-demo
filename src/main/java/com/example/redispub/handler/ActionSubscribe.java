package com.example.redispub.handler;

import com.example.redispub.enums.ActionType;
import com.example.redispub.controller.response.ResponseDto;
import com.example.redispub.service.dto.ChatDto;
import com.example.redispub.service.dto.RoomInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;

public class ActionSubscribe implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ActionSubscribe.class);

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ActionSubscribe(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatDto<RoomInfoDto> chatDto = objectMapper.readValue(message.getBody(), ChatDto.class);
            ActionType actionType = chatDto.getActionType();

            ResponseDto responseDto = new ResponseDto();
            responseDto.setMemberId(chatDto.getMemberId());
            responseDto.setRoomId(chatDto.getRoomId());
            responseDto.setActionType(actionType);
            responseDto.setData(chatDto.getData());

            switch (actionType) {
                case ROOM_JOIN:


                    break;
                case ROOM_QUIT:

                    break;
                default:
                    break;
            }

            simpMessagingTemplate.convertAndSend("/topic/room/" + chatDto.getRoomId(), responseDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
