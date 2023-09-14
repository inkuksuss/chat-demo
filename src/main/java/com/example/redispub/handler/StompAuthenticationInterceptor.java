package com.example.redispub.handler;

import com.example.redispub.request.RequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.io.IOException;


public class StompAuthenticationInterceptor implements ChannelInterceptor {

    private final Logger logger = LoggerFactory.getLogger(StompAuthenticationInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        logger.info("command = {}", accessor.getCommand());
        if (accessor.getCommand() == StompCommand.CONNECT || accessor.getCommand() == StompCommand.DISCONNECT || accessor.getCommand() == StompCommand.SUBSCRIBE || accessor.getCommand() == StompCommand.SEND) {
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
