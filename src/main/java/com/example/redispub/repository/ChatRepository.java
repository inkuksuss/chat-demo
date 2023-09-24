package com.example.redispub.repository;

import com.example.redispub.entity.RoomInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CrudRepository<RoomInfo, String>, ChatRedisRepository {

    List<RoomInfo> findByMemberId(Long memberId);

    List<RoomInfo> findByRoomId(Long roomId);

    void deleteByMemberId(Long memberId);
}
