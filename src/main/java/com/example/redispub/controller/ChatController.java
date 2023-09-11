package com.example.redispub.controller;

import com.example.redispub.repository.RedisJpaRepository;
import com.example.redispub.repository.dto.RoomInfoDto;
import com.example.redispub.request.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final RedisJpaRepository redisJpaRepository;

    public ChatController(RedisJpaRepository redisJpaRepository) {
        this.redisJpaRepository = redisJpaRepository;
    }

    @MessageMapping("/chat/room")
    public void message(RequestDto message) {
        logger.info("chat controller message = {}", message);
    }

    @MessageMapping("/chat/join")
    public void join(RequestDto message) {
        logger.info("chat controller join = {}", message);
        RoomInfoDto roomInfoDto = new RoomInfoDto();
        roomInfoDto.setRoomId(1L);
        roomInfoDto.setMemberId(1L);
        redisJpaRepository.save(roomInfoDto);

        RoomInfoDto roomInfoDto2 = new RoomInfoDto();
        roomInfoDto2.setRoomId(2L);
        roomInfoDto2.setMemberId(1L);
        redisJpaRepository.save(roomInfoDto2);

        List<RoomInfoDto> byMemberId = redisJpaRepository.findByMemberId(1L);
        for (RoomInfoDto infoDto : byMemberId) {
            logger.info("data = {}", infoDto.toString());
        }
    }
}
