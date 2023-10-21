package com.example.redispub.controller;

import com.example.redispub.entity.RoomMapper;
import com.example.redispub.service.dto.RoomDetailDto;
import com.example.redispub.utils.AuthenticationUtils;
import com.example.redispub.controller.request.RequestDto;
import com.example.redispub.service.ChatService;
import com.example.redispub.service.dto.ChatDto;
import com.example.redispub.service.dto.MessageDto;
import com.example.redispub.service.dto.RoomInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;


@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private final RedisTemplate<String, Object> redisTemplate;

    public ChatController(ChatService chatService, RedisTemplate<String, Object> redisTemplate) {
        this.chatService = chatService;
        this.redisTemplate = redisTemplate;
    }

    @MessageMapping("/chat/init")
    public void init(Principal principal, RequestDto requestDto) {
        ChatDto<List<RoomDetailDto>> chatDto = chatService.initRoomList(
                principal.getName(),
                AuthenticationUtils.getMemberId(requestDto.getToken())
        );

        redisTemplate.convertAndSend("/init", chatDto);
    }

    @MessageMapping("/chat/join")
    public void join(RequestDto requestDto) {
        ChatDto<RoomDetailDto> chatDto = chatService.joinRoom(
                AuthenticationUtils.getMemberId(requestDto.getToken()),
                requestDto.getRoomId()
        );

        redisTemplate.convertAndSend("/actions", chatDto);
    }

    @MessageMapping("/chat/message")
    public void sendMessage(RequestDto requestDto) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMemberId(AuthenticationUtils.getMemberId(requestDto.getToken()));
        messageDto.setRoomId(requestDto.getRoomId());
        messageDto.setBody(requestDto.getData());
        messageDto.setMessageType(requestDto.getMessageType());

        ChatDto<MessageDto> chatDto = chatService.sendMessage(messageDto);

        redisTemplate.convertAndSend("/message", chatDto);
    }
}
