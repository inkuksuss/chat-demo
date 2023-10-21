package com.example.redispub.repository;

import com.example.redispub.entity.RoomMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomRepositoryTest {


    public static final Logger log = LoggerFactory.getLogger(RoomRepositoryTest.class);
    @Autowired
    RoomRepository roomRepository;

    @Test
    void findRoomMapperDetailByRoomId() {
        Long testMemberId = 1L;
        List<RoomMapper> roomAllMemberByMemberId = roomRepository.findRoomAllMemberByMemberId(testMemberId);
        log.info("result = {}", roomAllMemberByMemberId);

    }

}