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
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


public class StompAuthenticationInterceptor implements ChannelInterceptor {

    private final Logger logger = LoggerFactory.getLogger(StompAuthenticationInterceptor.class);
    private final boolean isDebug = true;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (this.isDebug) logger.info("command = {}", accessor.getCommand());
        if (accessor.getCommand() == StompCommand.CONNECT || accessor.getCommand() == StompCommand.DISCONNECT) {
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

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return ChannelInterceptor.super.postReceive(message, channel);
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}
