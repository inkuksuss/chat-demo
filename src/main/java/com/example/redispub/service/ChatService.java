package com.example.redispub.service;

import com.example.redispub.controller.RedisService;
import com.example.redispub.entity.Member;
import com.example.redispub.entity.RoomMapper;
import com.example.redispub.enums.ActionType;
import com.example.redispub.repository.*;
import com.example.redispub.entity.Room;
import com.example.redispub.service.dto.ChatDto;
import com.example.redispub.entity.Message;
import com.example.redispub.service.dto.MessageDto;
import com.example.redispub.service.dto.RoomInfoDto;
import com.example.redispub.service.dto.RoomDetailDto;
import org.hibernate.proxy.HibernateProxy;
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
        List<RoomMapper> roomMapperList = roomMapperRepository.findRoomDetailByMemberId(memberId);

        List<Long> roomIdList = roomMapperList.stream()
                .map(RoomMapper::getRoom)
                .map(Room::getId)
                .distinct()
                .toList();

        List<RoomMapper> memberMapperList = roomMapperRepository.findMemberDetailByRoomIdList(roomIdList);
        List<RoomDetailDto> roomDetailList = this.createRoomDetailList(roomMapperList, memberMapperList);
        List<RoomDetailDto> roomDetailList1 = roomService.findRoomDetailList(roomIdList);

        ChatDto<List<RoomDetailDto>> chatDto = new ChatDto<>();
        chatDto.setMemberId(memberId);
        chatDto.setPrincipalName(name);
        chatDto.setData(roomDetailList);

        return chatDto;
    }

    @Transactional
    public ChatDto<RoomInfoDto> joinRoom(Long memberId, Long roomId) {
        Assert.notNull(memberId, "member id can not be null");
        Assert.notNull(roomId, "room id can not be null");

        List<RoomMapper> findMemberList = roomMapperRepository.findRoomMapperDetailByRoomId(roomId);
        List<Long> memberIdList = findMemberList.stream()
                .map(RoomMapper::getMember)
                .map(Member::getId)
                .toList();

        if (!checkAuthentication(memberIdList, memberId)) throw new IllegalStateException("참가할 수 없는 방입니다.");

        redisService.enterRoom(roomId, memberId);

        ChatDto<RoomInfoDto> chatDto = new ChatDto<>();
        chatDto.setMemberId(memberId);
        chatDto.setRoomId(roomId);
        chatDto.setActionType(ActionType.ROOM_JOIN);
        chatDto.setData(this.getRoomParticipationInformation(roomId, memberIdList));

        return chatDto;
    }

    private boolean checkAuthentication(List<Long> idList, Long id) {
        return idList.contains(id);
    }

    private RoomInfoDto getRoomParticipationInformation(Long roomId, List<Long> memberIdList) {
        Set<Long> currentMemberIdList = redisService.findByRoomId(roomId);
        Slice<Message> messageList =
                messageRepository.findMessageByRoomId(roomId, PageRequest.of(0, 20, Sort.by(Sort.Order.desc("created"))));

        return new RoomInfoDto(Set.copyOf(memberIdList), currentMemberIdList, messageList);
    }

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

    private List<RoomDetailDto> createRoomDetailList(List<RoomMapper> roomMapperList, List<RoomMapper> memberMapperList) {
        List<RoomDetailDto> dtoList = new ArrayList<>();
        for (RoomMapper roomMapper : roomMapperList) {
            RoomDetailDto roomDetailDto = new RoomDetailDto();
            roomDetailDto.setRoomId(roomMapper.getRoom().getId());
            roomDetailDto.getMemberList().addAll(memberMapperList.stream()
                    .filter(rm -> roomMapper.getRoom().getId().equals(rm.getRoom().getId()))
                    .map(RoomMapper::getMember).toList());
            dtoList.add(roomDetailDto);
        }

        return dtoList;
    }
}
