package com.example.redispub.service;

import com.example.redispub.controller.RedisService;
import com.example.redispub.entity.Member;
import com.example.redispub.entity.RoomMapper;
import com.example.redispub.enums.ActionType;
import com.example.redispub.repository.*;
import com.example.redispub.entity.Room;
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
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final RedisService redisService;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final RoomMapperRepository roomMapperRepository;
    private final RoomService roomService;
    private final Logger logger = LoggerFactory.getLogger(ChatService.class);

    public ChatService(RedisService redisService, MemberRepository memberRepository, RoomRepository roomRepository, MessageRepository messageRepository, RoomMapperRepository roomMapperRepository, RoomService roomService) {
        this.redisService = redisService;
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.roomMapperRepository = roomMapperRepository;
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

        if (!roomService.checkAuthentication(roomId, memberId)) throw new IllegalStateException("참가할 수 없는 방입니다.");

        List<RoomMapper> memberDetailOfRoom = roomRepository.findMemberDetailOfRoom(roomId);

        RoomDetailDto roomDetailDto = new RoomDetailDto(roomId);
        memberDetailOfRoom.forEach(roomMapper -> roomDetailDto.getMemberList().add(new MemberDetailDto(roomMapper.getMember())));

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

    private boolean checkAuthentication(List<Long> idList, Long id) {
        return idList.contains(id);
    }

//    private RoomInfoDto getRoomParticipationInformation(Long roomId, List<Long> memberIdList) {
//        Set<Long> currentMemberIdList = redisService.findByRoomId(roomId);


//        return new RoomInfoDto(Set.copyOf(memberIdList), currentMemberIdList, messageList);
//    }

    @Transactional
    public ChatDto<MessageDto> sendMessage(MessageDto messageDto) {
        Assert.notNull(messageDto.getMemberId(), "member id can not be null");
        Assert.notNull(messageDto.getRoomId(), "room id can not be null");
        Assert.notNull(messageDto.getBody(), "message body can not be null");
        Assert.notNull(messageDto.getMessageType(), "message type can not be null");

        Optional<RoomMapper> findMember = roomMapperRepository.findByMemberIdAndRoomId(messageDto.getMemberId(), messageDto.getRoomId());
        findMember.orElseThrow(() -> new IllegalStateException("방에 참가하지 않은 인원입니다."));

        Message savedMessage = messageRepository.save(this.createMessage(messageDto));

        ChatDto<MessageDto> chatDto = new ChatDto<>();
        chatDto.setMemberId(savedMessage.getMember().getId());
        chatDto.setRoomId(savedMessage.getRoom().getId());
        chatDto.setActionType(ActionType.MESSAGE);
//        chatDto.setData(savedMessage.toEntity());

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
