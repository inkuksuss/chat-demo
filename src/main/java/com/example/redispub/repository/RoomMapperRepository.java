package com.example.redispub.repository;

import com.example.redispub.entity.RoomMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RoomMapperRepository extends CrudRepository<RoomMapper, Long> {

    List<RoomMapper> findRommMapperListByRoomId(Long roomId);

//    Optional<RoomMapper> findByMemberIdAndRoomId(Long memberId, Long roomId);

//    @Query("SELECT r FROM RoomMapper r JOIN FETCH r.member JOIN FETCH r.room WHERE r.room.id = :roomId")
//    List<RoomMapper> findRoomMapperDetailByRoomId(Long roomId);
}
