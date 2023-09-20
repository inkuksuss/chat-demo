package com.example.redispub.service;

import com.example.redispub.repository.ChatRepository;
import com.example.redispub.repository.MessageRepository;
import com.example.redispub.repository.RoomRepository;
import com.example.redispub.entity.Room;
import com.example.redispub.request.RequestDto;
import com.example.redispub.service.dto.ChatDto;
import com.example.redispub.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(RedisTemplate<String, Object> redisTemplate, ChatRepository chatRepository, RoomRepository roomRepository, MessageRepository messageRepository) {
        this.redisTemplate = redisTemplate;
        this.chatRepository = chatRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    public void initRoomList(RequestDto requestDto) {

        List<String> roomIdList = roomRepository.findByMemberId(Long.valueOf(requestDto.getToken())).stream()
                .map(Room::getRoomId)
                .map(String::valueOf)
                .toList();

        ChatDto<List<String>> chatDto = new ChatDto<>();
        chatDto.setSenderId(Long.valueOf(requestDto.getToken()));
        chatDto.setName(requestDto.getName());
        chatDto.setData(roomIdList);

        redisTemplate.convertAndSend("/init", chatDto);
    }

    public void joinRoom(Long memberId) {
        List<String> roomIdList = roomRepository.findByMemberId(memberId).stream()
                .map(Room::getRoomId)
                .map(String::valueOf)
                .toList();

        logger.info("chatService.joinRoom memberId = {}, roomIdList = {}", memberId, roomIdList);
        chatRepository.subscribe(roomIdList);
    }

    public void sendMessage(Long senderId, Long roomId, String message) {
        logger.info("chatService.sendMessage roomId = {}, message = {}", roomId,  message);
        new Message(roomId, message, );
        ChatDto<Message> chatDto = new ChatDto<>();
        chatDto.setSenderId(senderId);
        chatDto.setRoomId(roomId);
        chatDto.setMessage(message);

        redisTemplate.convertAndSend("/message", chatDto);
    }
}
