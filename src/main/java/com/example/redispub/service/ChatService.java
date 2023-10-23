package com.example.redispub.service;

import com.example.redispub.controller.RedisService;
import com.example.redispub.entity.Member;
import com.example.redispub.entity.RoomMapper;
import com.example.redispub.enums.ActionType;
import com.example.redispub.repository.*;
import com.example.redispub.entity.Room;
import com.example.redispub.repository.dto.MemberAccessDto;
import com.example.redispub.repository.dto.MessageSummaryDto;
import com.example.redispub.service.dto.*;
import com.example.redispub.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChatService {

    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final RoomService roomService;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(RedisService redisService, MemberRepository memberRepository, RoomRepository roomRepository, MessageRepository messageRepository, RoomService roomService) {
        this.redisService = redisService;
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.roomService = roomService;
    }

    public ChatDto<List<RoomDetailDto>> initRoomList(String name, Long memberId) {
        List<RoomDetailDto> roomSummaryDtoList = roomService.getRoomSummaryDtoList(memberId);

        ChatDto<List<RoomDetailDto>> chatDto = new ChatDto<>();
        chatDto.setMemberId(memberId);
        chatDto.setPrincipalName(name);
        chatDto.setData(roomSummaryDtoList);

        return chatDto;
    }

    public ChatDto<RoomDetailDto> joinRoom(Long memberId, Long roomId) {
        Assert.notNull(memberId, "member id can not be null");
        Assert.notNull(roomId, "room id can not be null");

        roomService.checkAuthentication(roomId, memberId);

        List<RoomMapper> memberDetailOfRoom = roomRepository.findMemberDetailOfRoom(roomId);

        RoomDetailDto roomDetailDto = new RoomDetailDto(roomId);
        memberDetailOfRoom.forEach(roomMapper -> roomDetailDto.getMemberList().add(new MemberDetailDto(roomMapper)));

        // 메세지 가져오는 쿼리
        Slice<Message> messageList =
                messageRepository.findMessageByRoomId(roomId, PageRequest.of(0, 20, Sort.by(Sort.Order.desc("created"))));

        roomDetailDto.setHasNext(messageList.hasNext());
        roomDetailDto.getMessageList().addAll(messageList.getContent().stream()
                .map(MessageSummaryDto::new)
                .toList());

        redisService.enterRoom(roomId, memberId);

        ChatDto<RoomDetailDto> chatDto = new ChatDto<>();
        chatDto.setMemberId(memberId);
        chatDto.setRoomId(roomId);
        chatDto.setActionType(ActionType.ROOM_JOIN);
        chatDto.setData(roomDetailDto);

        return chatDto;
    }

    public ChatDto<RoomDetailDto> sendMessage(MessageDto messageDto) {
        Assert.notNull(messageDto.getMemberId(), "member id can not be null");
        Assert.notNull(messageDto.getRoomId(), "room id can not be null");
        Assert.notNull(messageDto.getBody(), "message body can not be null");
        Assert.notNull(messageDto.getMessageType(), "message type can not be null");

        roomService.checkAuthentication(messageDto.getRoomId(), messageDto.getMemberId());

        Message savedMessage = messageRepository.save(this.createMessage(messageDto));
        Set<Long> currentMemberList = redisService.findByRoomId(messageDto.getRoomId());

        roomService.updateLastAccessByMemberIdList(
                savedMessage.getCreated(),
                savedMessage.getRoom().getId(),
                new ArrayList<>(currentMemberList));

        List<MemberAccessDto> memberAccessDtoList = roomService.findMemberAccessDtoList(savedMessage.getRoom().getId());


        RoomDetailDto roomDetailDto = new RoomDetailDto(savedMessage.getRoom().getId());
        roomDetailDto.getMessageList().add(new MessageSummaryDto(savedMessage));
        roomDetailDto.getMemberList().addAll(memberAccessDtoList.stream()
                .map(dto -> new MemberDetailDto(dto.getMemberId(), dto.getLastAccessDate())).toList());

        ChatDto<RoomDetailDto> chatDto = new ChatDto<>();
        chatDto.setMemberId(savedMessage.getMember().getId());
        chatDto.setRoomId(savedMessage.getRoom().getId());
        chatDto.setActionType(ActionType.MESSAGE);
        chatDto.setData(roomDetailDto);

        return chatDto;
    }

    @Transactional
    public void closeConnection() {

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
