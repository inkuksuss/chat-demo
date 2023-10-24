package com.example.redispub.handler;

import com.example.redispub.controller.request.RequestDto;
import com.example.redispub.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StompAuthenticationInterceptor implements ChannelInterceptor {


    private final Logger logger = LoggerFactory.getLogger(StompAuthenticationInterceptor.class);
    private final ChatService service;

    public StompAuthenticationInterceptor(@Lazy ChatService service) {
        this.service = service;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        logger.info("command = {}", accessor.getCommand());
        if (accessor.getCommand() == StompCommand.CONNECT || accessor.getCommand() == StompCommand.SUBSCRIBE || accessor.getCommand() == StompCommand.SEND) {
            return message;
        }

        if (accessor.getCommand() == StompCommand.DISCONNECT) {
            if (accessor.getSessionAttributes() != null) {
                String memberId = (String) accessor.getSessionAttributes().get("memberId");
                service.closeConnection(Long.parseLong(memberId));
            }

            return message;
        }

        try {
            RequestDto requestDto = new ObjectMapper().readValue((byte[]) message.getPayload(), RequestDto.class);

            if (requestDto.getToken() == null) {
                throw new IllegalArgumentException("인증 토큰이 존재하지 않습니다");
            }

            return message;
        }
        catch (IOException e) {
            throw new IllegalArgumentException("잘못된 데이터 형식입니다");
        }
    }
}
