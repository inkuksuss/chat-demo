package com.example.redispub.repository;

import com.example.redispub.handler.MessageSubscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRedisRepositoryImpl implements ChatRedisRepository {

    private final RedisMessageListenerContainer redisContainer;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public ChatRedisRepositoryImpl(RedisMessageListenerContainer redisContainer) {
        this.redisContainer = redisContainer;
    }

    @Override
    public void subscribe(List<String> channelList) {
        List<ChannelTopic> topicList = channelList
                .stream()
                .map(channel -> new ChannelTopic("/room/" + channel))
                .toList();

        redisContainer.addMessageListener(new MessageSubscribe(simpMessagingTemplate), topicList);
    }
}
