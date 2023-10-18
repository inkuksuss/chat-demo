package com.example.redispub.service;

import com.example.redispub.entity.RoomMapper;
import com.example.redispub.repository.MessageRepository;
import com.example.redispub.repository.RoomMapperRepository;
import com.example.redispub.repository.RoomRepository;
import com.example.redispub.repository.dto.MessageSummaryDto;
import com.example.redispub.service.dto.RoomDetailDto;
import org.springframework.stereotype.Service;

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

    public List<RoomDetailDto> findRoomDetailList(List<Long> ids) {
        List<RoomMapper> roomMapperList = roomMapperRepository.findMemberDetailByRoomIdList(ids);
        List<MessageSummaryDto> byDto = roomRepository.findByDto(ids);


        return List.of();
    }
}
