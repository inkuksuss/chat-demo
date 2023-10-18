package com.example.redispub.repository;

import com.example.redispub.entity.RoomMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RoomMapperRepository extends CrudRepository<RoomMapper, Long> {

    Optional<RoomMapper> findByMemberIdAndRoomId(Long memberId, Long roomId);

    @Query("SELECT r1 from RoomMapper r1 join fetch r1.member JOIN fetch r1.room " +
            "WHERE r1.room.id IN (SELECT r.room.id FROM RoomMapper r WHERE r.member.id = :memberId)")
    List<RoomMapper> findRoomDetailByMemberId(Long memberId);

    @Query("SELECT r FROM RoomMapper r JOIN FETCH r.member JOIN FETCH r.room WHERE r.room.id = :roomId")
    List<RoomMapper> findRoomMapperDetailByRoomId(Long roomId);

    @Query("SELECT r FROM RoomMapper r LEFT JOIN FETCH r.member WHERE r.room.id IN :roomIdList")
    List<RoomMapper> findMemberDetailByRoomIdList(List<Long> roomIdList);

}
