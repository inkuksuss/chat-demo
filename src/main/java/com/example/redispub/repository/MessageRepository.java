package com.example.redispub.repository;

import com.example.redispub.entity.Message;
import com.example.redispub.repository.dto.MessageSummaryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    Slice<Message> findMessageByRoomId(Long roomId, Pageable pageable);


    // TODO 비동기 처리
    @Query("select new com.example.redispub.repository.dto.MessageSummaryDto(r.id, m1.id, m1.member.id, m1.body, m1.type, m1.created) " +
            "from Message m1 " +
            "left join Message m2 on m1.room.id = m2.room.id and m1.id < m2.id" +
            " inner join Room r on m1.room.id = r.id where m2.id is null and m1.room.id in :roomIdList")
    List<MessageSummaryDto> findEachRoomRecentMessage(@Param("roomIdList") List<Long> roomIdList);
}
