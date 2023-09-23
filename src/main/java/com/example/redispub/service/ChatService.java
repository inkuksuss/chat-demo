package com.example.redispub.service;

import com.example.redispub.entity.Member;
import com.example.redispub.entity.RoomMapping;
import com.example.redispub.enums.MessageType;
import com.example.redispub.repository.*;
import com.example.redispub.entity.Room;
import com.example.redispub.service.dto.ChatDto;
import com.example.redispub.entity.Message;
import com.example.redispub.service.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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
        chatDto.setSenderId(memberId);
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

    public void sendMessage(MessageDto messageDto) {
        Optional<RoomMapping> findMember = roomMappingRepository.findByMemberIdAndRoomId(messageDto.getMemberId(), messageDto.getRoomId());
        findMember.orElseThrow(() -> new NoSuchElementException("방에 참가하지 않은 인원입니다."));

        Message message = this.createMessage(messageDto);
        Message savedMessage = messageRepository.save(message);

        ChatDto<MessageDto> chatDto = new ChatDto<>();
        chatDto.setSenderId(savedMessage.getMember().getId());
        chatDto.setRoomId(savedMessage.getRoom().getId());
        chatDto.setMessageType(savedMessage.getType());
        chatDto.setData(savedMessage.toEntity());

        redisTemplate.convertAndSend("/message", chatDto);
    }

    private Message createMessage(MessageDto messageDto) {
        Member member = new Member();
        member.setId(messageDto.getMemberId());

        Room room = new Room();
        room.setId(messageDto.getRoomId());

        Message message = new Message();
        message.setRoom(room);
        message.setMember(member);
        message.setBody(messageDto.getBody());
        message.setType(messageDto.getMessageType() == null ? MessageType.MESSAGE : messageDto.getMessageType());
        message.setCreated(LocalDateTime.now());
        return message;
    }
}
