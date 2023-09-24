package com.example.redispub.controller;

import com.example.redispub.authentication.AuthenticationUtils;
import com.example.redispub.request.RequestDto;
import com.example.redispub.service.ChatService;
import com.example.redispub.service.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/init")
    public void init(Principal principal, RequestDto requestDto) {
        chatService.initRoomList(principal.getName(), AuthenticationUtils.getMemberId(requestDto.getToken()));
    }

    @MessageMapping("/chat/join")
    public void join(RequestDto requestDto) {
        chatService.joinRoom(AuthenticationUtils.getMemberId(requestDto.getToken()), requestDto.getRoomId());
    }

    @MessageMapping("/chat/message")
    public void sendMessage(RequestDto requestDto) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMemberId(AuthenticationUtils.getMemberId(requestDto.getToken()));
        messageDto.setRoomId(requestDto.getRoomId());
        messageDto.setBody(requestDto.getData());
        messageDto.setMessageType(requestDto.getMessageType());

        chatService.sendMessage(messageDto);
    }
}
