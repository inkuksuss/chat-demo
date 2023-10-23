package com.example.redispub.repository;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RedisRepository {

    void enterRoom(Long roomId, Long memberId);

    void leaveRoom(Long roomId, Long memberId);

    void clearRoom(Long roomId);

    Set<Long> findMemberIdListByRoomId(Long roomId);

    void deleteByMemberId(Long memberId);


}
