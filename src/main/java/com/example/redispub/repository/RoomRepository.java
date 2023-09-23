package com.example.redispub.repository;


import com.example.redispub.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    Optional<Room> findByCreatorId(Long id);
}
