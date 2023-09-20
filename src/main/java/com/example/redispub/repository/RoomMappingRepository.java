package com.example.redispub.repository;


import com.example.redispub.entity.Room;
import com.example.redispub.entity.RoomMapping;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoomMappingRepository {

    private final static Map<Long, RoomMapping> store = new HashMap<>();
    private static Long sequence = 0L;

    public RoomMapping save(RoomMapping room) {
        room.setId(++sequence);
        store.put(room.getId(), room);

        return room;
    }

    public Optional<RoomMapping> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<RoomMapping> findByMemberId(Long id) {
        List<RoomMapping> roomMappingList = new ArrayList<>();

        for (Map.Entry<Long, RoomMapping> entry : store.entrySet()) {
            RoomMapping roomMapping = entry.getValue();
            if (roomMapping.getMemberId().equals(id)) {
                roomMappingList.add(roomMapping);
            }
        }

        return roomMappingList;
    }
}
