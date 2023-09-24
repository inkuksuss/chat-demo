package com.example.redispub.repository;

import com.example.redispub.entity.RoomMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RoomMappingRepository extends CrudRepository<RoomMapping, Long> {

    List<RoomMapping> findByMemberId(Long memberId);

    Optional<RoomMapping> findByMemberIdAndRoomId(Long memberId, Long roomId);

    @Query("SELECT r FROM RoomMapping r JOIN FETCH r.member JOIN FETCH r.room WHERE r.room.id = :roomId")
    List<RoomMapping> findRoomDetailByRoomId(Long roomId);

}
