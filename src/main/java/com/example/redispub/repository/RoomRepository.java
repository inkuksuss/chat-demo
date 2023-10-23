package com.example.redispub.repository;


import com.example.redispub.entity.Room;
import com.example.redispub.entity.RoomMapper;
import com.example.redispub.repository.dto.MemberAccessDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByCreatorId(Long creatorId);

    @Query("SELECT r1 from RoomMapper r1 join fetch r1.member JOIN fetch r1.room " +
            "WHERE r1.room.id IN (SELECT r.room.id FROM RoomMapper r WHERE r.member.id = :memberId)")
    List<RoomMapper> findRoomAllMemberByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT roomMapper from RoomMapper roomMapper JOIN FETCH roomMapper.member WHERE roomMapper.room.id = :roomId")
    List<RoomMapper> findMemberDetailOfRoom(@Param("roomId") Long roomId);

    @Query("SELECT r FROM RoomMapper r JOIN FETCH r.member JOIN FETCH r.room WHERE r.room.id = :roomId")
    List<RoomMapper> findRoomMapperDetailByRoomId(Long roomId);

    @Query("SELECT roomMapper FROM RoomMapper roomMapper WHERE roomMapper.room.id = :roomId AND roomMapper.member.id = :memberId")
    Optional<RoomMapper> findRoomByMemberId(@Param("roomId") Long roomId, @Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE RoomMapper roomMapper SET roomMapper.lastAccessDate = :lastAccessDate " +
            "WHERE roomMapper.room.id = :roomId AND roomMapper.member.id IN :memberIdList")
    Integer updateLastAccessByMemberIdList(
            @Param("lastAccessDate") LocalDateTime localDateTime,
            @Param("roomId") Long roomId,
            @Param("memberIdList") List<Long> memberIdList);

    @Query("SELECT new com.example.redispub.repository.dto.MemberAccessDto(roomMapper.member.id, roomMapper.room.id, roomMapper.lastAccessDate) " +
            "FROM RoomMapper roomMapper WHERE roomMapper.room.id = :roomId")
    List<MemberAccessDto> findMemberAccessDtoList(@Param("roomId") Long roomId);

}
