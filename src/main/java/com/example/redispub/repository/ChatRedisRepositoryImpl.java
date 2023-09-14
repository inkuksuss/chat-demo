package com.example.redispub.repository;

import com.example.redispub.model.RoomEventListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

public class ChatRedisRepositoryImpl implements ChatRedisRepository {

    private final RedisMessageListenerContainer redisContainer;

    public ChatRedisRepositoryImpl(RedisMessageListenerContainer redisContainer) {
        this.redisContainer = redisContainer;
    }

    @Override
    public void subscribe(List<String> channelList) {
        List<ChannelTopic> topicList = channelList
                .stream()
                .map(channel -> new ChannelTopic("/room/" + channel))
                .toList();

        redisContainer.addMessageListener(new RoomEventListener(), topicList);
    }
}
