package com.example.redispub.repository;

import com.example.redispub.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    Slice<Message> findMessageByRoomId(Long roomId, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE (m.id, m.created) IN " +
            "(SELECT mc.id, MAX(mc.created) FROM Message mc GROUP BY mc.room.id HAVING mc.room.id IN :idList)")
    List<Message> findRecentMessageByRoomId(List<Long> idList);

}
