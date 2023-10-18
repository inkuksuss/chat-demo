package com.example.redispub.repository;


import com.example.redispub.entity.Room;
import com.example.redispub.repository.dto.MessageSummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByCreatorId(Long creatorId);


    @Query("select new com.example.redispub.repository.dto.MessageSummaryDto(r.id, m1.id, m1.body, m1.type, m1.created) " +
            "from Message m1 left join Message m2 on m1.room.id = m2.room.id and m1.id < m2.id" +
            " inner join Room r on m1.room.id = r.id where m2.id is null")
    List<MessageSummaryDto> findByDto(List<Long> ids);
}
