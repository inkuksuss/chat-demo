package com.example.redispub.service;

import com.example.redispub.repository.ChatRepository;
import com.example.redispub.repository.RoomRepository;
import com.example.redispub.repository.dto.RoomDto;
import com.example.redispub.request.RequestDto;
import com.example.redispub.service.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(RedisTemplate<String, Object> redisTemplate, ChatRepository chatRepository, RoomRepository roomRepository) {
        this.redisTemplate = redisTemplate;
        this.chatRepository = chatRepository;
        this.roomRepository = roomRepository;
    }

    public void joinRoom(Long memberId) {
        List<String> roomIdList = roomRepository.findByMemberId(memberId).stream()
                .map(RoomDto::getRoomId)
                .map(String::valueOf)
                .toList();

        logger.info("chatService.joinRoom memberId = {}, roomIdList = {}", memberId, roomIdList);
        chatRepository.subscribe(roomIdList);
    }

    public void sendMessage(Long senderId, Long roomId, String message) {
        logger.info("chatService.sendMessage roomId = {}, message = {}", roomId,  message);
        MessageDto messageDto = new MessageDto();
        messageDto.setSenderId(senderId);
        messageDto.setMessage(message);

        redisTemplate.convertAndSend("/room/" + roomId, messageDto);
    }
}
