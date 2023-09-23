package com.example.redispub.controller;

import com.example.redispub.authentication.AuthenticationUtils;
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
        Long memberId = AuthenticationUtils.getMemberId(requestDto.getToken());
        chatService.initRoomList(principal.getName(), memberId);
    }

    @MessageMapping("/chat/join")
    public void join(RequestDto requestDto) {
//        chatService.joinRoom(this.getMemberId(requestDto));
    }

    @MessageMapping("/chat/message")
    public void sendMessage(RequestDto requestDto) {
        Long memberId = AuthenticationUtils.getMemberId(requestDto.getToken());
        chatService.sendMessage(memberId, requestDto.getRoomId(), requestDto.getData(), requestDto.getMessageType());
    }
}
