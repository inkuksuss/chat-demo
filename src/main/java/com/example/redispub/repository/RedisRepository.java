package com.example.redispub.repository;

import com.example.redispub.entity.RoomInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RedisRepository {

    void enterRoom(Long roomId, Long memberId);

    void leaveRoom(Long roomId, Long memberId);

    void clearRoom(Long roomId);

    List<RoomInfo> findByMemberId(Long memberId);

    Set<Long> findByRoomId(Long roomId);

    void deleteByMemberId(Long memberId);


}
