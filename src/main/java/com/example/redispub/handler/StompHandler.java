package com.example.redispub.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StompHandler implements ChannelInterceptor {

    private final Logger logger = LoggerFactory.getLogger(StompHandler.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        logger.info("preSend > message = {}, channel = {}", message, channel);
        logger.info("!!!payload = {}, header = {}", message.getPayload(), message.getHeaders());

        Map<String, List> nativeHeaders = (Map<String, List>) message.getHeaders().get("nativeHeaders");
        List list = nativeHeaders.get("jwt");
        logger.info("list = {}", list);

        MessageHeaders headers = message.getHeaders();
        Object simpMessageType = headers.get("simpMessageType");
        Object jwt = headers.get("jwt");
        logger.info("jwt = {}", jwt);
        if (simpMessageType != null) logger.info("simp meesage = {}", simpMessageType);

        return ChannelInterceptor.super.preSend(message, channel);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        logger.info("postSend > message = {}, channel = {}, send = {}", message, channel, sent);
        logger.info("@@@payload = {}, header = {}", message.getPayload(), message.getHeaders());
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
//        logger.info("afterSendCompletion > message = {}, channel = {}, send = {} ex = {}", message, channel, sent, ex);
        logger.info("###payload = {}, header = {}", message.getPayload(), message.getHeaders());
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        logger.info("preReceive > channel = {}", channel);
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        logger.info("postReceive > message = {}, channel = {}", message, channel);
        return ChannelInterceptor.super.postReceive(message, channel);
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        logger.info("afterReceiveCompletion > message = {}, channel = {}, ex = {}", message, channel, ex);
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}
