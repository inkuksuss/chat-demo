package com.example.redispub.repository;


import com.example.redispub.entity.Room;
import com.example.redispub.entity.RoomMapping;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoomRepository {

    private final static Map<Long, Room> store = new HashMap<>();
    private static Long sequence = 0L;

    private final RoomMappingRepository roomMappingRepository;

    public RoomRepository(RoomMappingRepository roomMappingRepository) {
        this.roomMappingRepository = roomMappingRepository;
    }

    public Room save(Room room) {
        room.setId(++sequence);
        store.put(room.getId(), room);

        return room;
    }

    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Room> findByCreatorId(Long id) {
        List<Room> roomList = new ArrayList<>();

        for (Map.Entry<Long, Room> entry : store.entrySet()) {
            Room room = entry.getValue();
            if (room.getMemberId().equals(id)) {
                roomList.add(room);
            }
        }

        return roomList;
    }

    public List<Room> findByMemberId(Long id) {
        List<Room> roomList = new ArrayList<>();

        List<RoomMapping> mappingList = roomMappingRepository.findByMemberId(id);
        for (RoomMapping roomMapping : mappingList) {
            Optional<Room> findRoom = this.findById(roomMapping.getRoomId());
            findRoom.ifPresent(roomList::add);
        }

        return roomList;
    }
}
