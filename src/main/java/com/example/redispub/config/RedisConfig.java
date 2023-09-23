package com.example.redispub.config;

import com.example.redispub.handler.InitSubscribe;
import com.example.redispub.handler.MessageSubscribe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class RedisConfig {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    MessageListener initSubscribe() {
        return new InitSubscribe(simpMessagingTemplate);
    }

    @Bean
    MessageListener messageSubscribe() { return new MessageSubscribe(simpMessagingTemplate); }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(initSubscribe(), new ChannelTopic("/init"));
        container.addMessageListener(messageSubscribe(), new ChannelTopic("/message"));

        return container;
    }

    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

        return template;
    }
}
