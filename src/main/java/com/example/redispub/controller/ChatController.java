package com.example.redispub.controller;

import com.example.redispub.request.RequestDto;
import com.example.redispub.service.ChatService;
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

        requestDto.setName(principal.getName());
        chatService.initRoomList(requestDto);
    }

    @MessageMapping("/chat/join")
    public void join(RequestDto requestDto) {
        chatService.joinRoom(this.getMemberId(requestDto));
    }

    @MessageMapping("/chat/message")
    public void sendMessage(RequestDto requestDto) {
        chatService.sendMessage(this.getMemberId(requestDto), requestDto.getRoomId(), requestDto.getData());
    }

    private Long getMemberId(RequestDto requestDto) {
        return Long.valueOf(requestDto.getToken());
    }
}
