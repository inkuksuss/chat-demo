package com.example.redispub.service;

import com.example.redispub.entity.Member;
import com.example.redispub.entity.RoomMapping;
import com.example.redispub.enums.MessageType;
import com.example.redispub.repository.*;
import com.example.redispub.entity.Room;
import com.example.redispub.request.RequestDto;
import com.example.redispub.service.dto.ChatDto;
import com.example.redispub.entity.Message;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final RoomMappingRepository roomMappingRepository;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(
            RedisTemplate<String, Object> redisTemplate,
            ChatRepository chatRepository,
            MemberRepository memberRepository,
            RoomRepository roomRepository,
            MessageRepository messageRepository,
            RoomMappingRepository roomMappingRepository) {
        this.redisTemplate = redisTemplate;
        this.chatRepository = chatRepository;
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.roomMappingRepository = roomMappingRepository;
    }

    public void initRoomList(String name, Long memberId) {

        List<Long> findRoomIdList = roomMappingRepository.findByMemberId(memberId)
                .stream().map(roomMapping -> roomMapping.getRoom().getId()).toList();


        ChatDto<List<Long>> chatDto = new ChatDto<>();
        chatDto.setName(name);
        chatDto.setData(findRoomIdList);

        redisTemplate.convertAndSend("/init", chatDto);
    }

    public void joinRoom(Long memberId) {
//        List<String> roomIdList = roomRepository.findByMemberId(memberId).stream()
//                .map(Room::getRoomId)
//                .map(String::valueOf)
//                .toList();
//
//        logger.info("chatService.joinRoom memberId = {}, roomIdList = {}", memberId, roomIdList);
//        chatRepository.subscribe(roomIdList);
    }

    public void sendMessage(Long senderId, Long roomId, String body, @Nullable MessageType messageType) {
        logger.info("chatService.sendMessage roomId = {}, message = {}, type = {}", roomId,  body, messageType);

        Message message = createMessage(senderId, roomId, body, messageType);
        messageRepository.save(message);

        ChatDto<Message> chatDto = new ChatDto<>();
        chatDto.setSenderId(senderId);
        chatDto.setRoomId(roomId);
        chatDto.setData(message);

        redisTemplate.convertAndSend("/message", chatDto);
    }

    private static Message createMessage(Long senderId, Long roomId, String body, MessageType messageType) {
        Member member = new Member();
        member.setId(senderId);

        Room room = new Room();
        room.setId(roomId);

        Message message = new Message();
        message.setRoom(room);
        message.setMember(member);
        message.setBody(body);
        message.setType(messageType == null ? MessageType.MESSAGE : messageType);
        message.setCreated(LocalDateTime.now());
        return message;
    }
}
