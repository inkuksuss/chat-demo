package com.example.redispub.repository;

import java.util.List;

public interface ChatRedisRepository {

    void subscribe(List<String> channelList);
}
