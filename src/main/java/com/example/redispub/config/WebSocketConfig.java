package com.example.redispub.config;

import com.example.redispub.controller.RedisService;
import com.example.redispub.handler.SocketAuthenticationInterceptor;
import com.example.redispub.handler.StompAuthenticationInterceptor;
import com.example.redispub.handler.StompHandshakeHandler;
import com.example.redispub.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    @Autowired
//    ChatService chatService;

    @Bean
    ChannelInterceptor stompAuthenticationInterceptor() { return new StompAuthenticationInterceptor(); }

    @Bean
    HandshakeInterceptor socketAuthenticationInterceptor() {
        return new SocketAuthenticationInterceptor();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // pub
        config.setApplicationDestinationPrefixes("/app");

        // sub
        config.enableSimpleBroker("/topic", "/queue");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-server")
                .addInterceptors(socketAuthenticationInterceptor())
                .setHandshakeHandler(new StompHandshakeHandler())
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthenticationInterceptor());
    }
}
