package com.example.redispub.repository;

import com.example.redispub.entity.RoomInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedisRepository {

    void addMemberInRoom(Long roomId, Long memberId);

    List<RoomInfo> findByMemberId(Long memberId);

    List<RoomInfo> findByRoomId(Long roomId);

    void deleteByMemberId(Long memberId);
}
