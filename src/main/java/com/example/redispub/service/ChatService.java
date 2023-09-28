package com.example.redispub.service;

import com.example.redispub.controller.RedisService;
import com.example.redispub.entity.Member;
import com.example.redispub.entity.RoomMapping;
import com.example.redispub.enums.ActionType;
import com.example.redispub.repository.*;
import com.example.redispub.entity.Room;
import com.example.redispub.entity.RoomInfo;
import com.example.redispub.service.dto.ChatDto;
import com.example.redispub.entity.Message;
import com.example.redispub.service.dto.MessageDto;
import com.example.redispub.service.dto.RoomInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
//@Transactional(readOnly = true)
public class ChatService {

    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final RoomMappingRepository roomMappingRepository;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(RedisService redisService,
                       MemberRepository memberRepository,
                       RoomRepository roomRepository,
                       MessageRepository messageRepository,
                       RoomMappingRepository roomMappingRepository) {
        this.redisService = redisService;
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.roomMappingRepository = roomMappingRepository;
    }

    public ChatDto<List<Long>> initRoomList(String name, Long memberId) {
        List<Long> findRoomIdList = roomMappingRepository.findByMemberId(memberId)
                .stream().map(roomMapping -> roomMapping.getRoom().getId()).toList();

        ChatDto<List<Long>> chatDto = new ChatDto<>();
        chatDto.setMemberId(memberId);
        chatDto.setPrincipalName(name);
        chatDto.setData(findRoomIdList);

        return chatDto;
    }

//    @Transactional
    public ChatDto<RoomInfoDto> joinRoom(Long memberId, Long roomId) throws JsonProcessingException {
        Assert.notNull(memberId, "member id can not be null");
        Assert.notNull(roomId, "room id can not be null");

        List<RoomMapping> findMemberList = roomMappingRepository.findRoomDetailByRoomId(roomId);

        Optional<RoomMapping> checkRoomAuthentication = findMemberList.stream()
                .filter(roomMapping -> roomId.equals(roomMapping.getRoom().getId()))
                .filter(roomMapping -> memberId.equals(roomMapping.getMember().getId()))
                .findAny();

        checkRoomAuthentication.orElseThrow(() -> new IllegalStateException("참가할 수 없는 방입니다."));

//        chatRepository.deleteByMemberId(memberId);\
        redisService.saveRoom(new RoomInfo(roomId, memberId));

        ChatDto<RoomInfoDto> chatDto = new ChatDto<>();
        chatDto.setMemberId(memberId);
        chatDto.setRoomId(roomId);
        chatDto.setActionType(ActionType.ROOM_JOIN);
        chatDto.setData(this.getRoomParticipationInformation(roomId, findMemberList));

        return chatDto;
    }

    private RoomInfoDto getRoomParticipationInformation(Long roomId, List<RoomMapping> findMemberList) {
        List<Long> totalMemberIdList = findMemberList.stream()
                .map(RoomMapping::getMember)
                .map(Member::getId)
                .toList();

        List<Long> currentMemberIdList = redisService.findByRoomId(roomId).stream()
                .map(RoomInfo::getMemberId)
                .toList();

        return new RoomInfoDto(totalMemberIdList, currentMemberIdList);
    }

    @Transactional
    public ChatDto<MessageDto> sendMessage(MessageDto messageDto) {
        Assert.notNull(messageDto.getMemberId(), "member id can not be null");
        Assert.notNull(messageDto.getRoomId(), "room id can not be null");
        Assert.notNull(messageDto.getBody(), "message body can not be null");
        Assert.notNull(messageDto.getMessageType(), "message type can not be null");

        Optional<RoomMapping> findMember = roomMappingRepository.findByMemberIdAndRoomId(messageDto.getMemberId(), messageDto.getRoomId());
        findMember.orElseThrow(() -> new IllegalStateException("방에 참가하지 않은 인원입니다."));

        Message savedMessage = messageRepository.save(this.createMessage(messageDto));

        ChatDto<MessageDto> chatDto = new ChatDto<>();
        chatDto.setMemberId(savedMessage.getMember().getId());
        chatDto.setRoomId(savedMessage.getRoom().getId());
        chatDto.setActionType(ActionType.MESSAGE);
        chatDto.setData(savedMessage.toEntity());

        return chatDto;
    }

    private Message createMessage(MessageDto messageDto) {
        Member refMember = memberRepository.getReferenceById(messageDto.getMemberId());
        Room refRoom = roomRepository.getReferenceById(messageDto.getRoomId());

        Message message = new Message();
        message.setRoom(refRoom);
        message.setMember(refMember);
        message.setBody(messageDto.getBody());
        message.setType(messageDto.getMessageType());
        message.setCreated(LocalDateTime.now());

        return message;
    }
}
