package com.example.redispub.service;

import com.example.redispub.entity.RoomMapper;
import com.example.redispub.repository.MessageRepository;
import com.example.redispub.repository.RoomMapperRepository;
import com.example.redispub.repository.RoomRepository;
import com.example.redispub.repository.dto.MemberAccessDto;
import com.example.redispub.repository.dto.MessageSummaryDto;
import com.example.redispub.service.dto.MemberDetailDto;
import com.example.redispub.service.dto.RoomDetailDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapperRepository roomMapperRepository;
    private final MessageRepository messageRepository;

    public RoomService(RoomRepository roomRepository, RoomMapperRepository roomMapperRepository, MessageRepository messageRepository) {
        this.roomRepository = roomRepository;
        this.roomMapperRepository = roomMapperRepository;
        this.messageRepository = messageRepository;
    }

    public List<RoomDetailDto> getRoomSummaryDtoList(Long memberId) {

        List<RoomMapper> roomMapperList = roomRepository.findRoomAllMemberByMemberId(memberId);
        List<MessageSummaryDto> messageSummaryDtoList = this.getEachRoomRecentMessage(
                roomMapperList.stream()
                        .map(roomMapper -> roomMapper.getRoom().getId())
                        .distinct()
                        .toList()
        );
        return this.createRoomSummaryDtoList(roomMapperList, messageSummaryDtoList);
    }

    // TODO :: 20000
    private List<RoomDetailDto> createRoomSummaryDtoList(List<RoomMapper> roomMapperList, List<MessageSummaryDto> messageSummaryDtoList) {
        List<Long> roomIdList =
                roomMapperList.stream().map(roomMapper -> roomMapper.getRoom().getId()).distinct().toList();

        List<RoomDetailDto> roomDetailDtoList = new ArrayList<>();
        for (Long roomId : roomIdList) {
            RoomDetailDto roomDetailDto = new RoomDetailDto(roomId);
            roomMapperList.stream()
                    .filter(rm -> rm.getRoom().getId().equals(roomId))
                    .forEach(rm -> roomDetailDto.getMemberList().add(new MemberDetailDto(rm)));
            messageSummaryDtoList.stream()
                    .filter(msg -> msg.getRoomId().equals(roomId))
                    .forEach(msg -> roomDetailDto.getMessageList().add(msg));
            roomDetailDtoList.add(roomDetailDto);
        }

        return roomDetailDtoList;
    }

    public List<MessageSummaryDto> getEachRoomRecentMessage(List<Long> roomIds) {
        return messageRepository.findEachRoomRecentMessage(roomIds);
    }

    @Transactional
    public Integer updateLastAccessByMemberIdList(LocalDateTime lastAccessDate, Long roomId, List<Long> memberIdList) {
        return roomRepository.updateLastAccessByMemberIdList(lastAccessDate, roomId, memberIdList);
    }

    public List<MemberAccessDto> findMemberAccessDtoList(Long roomId) {
        return roomRepository.findMemberAccessDtoList(roomId);
    }

    public void checkAuthentication(Long roomId, Long memberId) {
        roomRepository.findRoomByMemberId(roomId, memberId).orElseThrow(IllegalAccessError::new);
    }
}
