package com.example.redispub.repository;


import com.example.redispub.repository.dto.RoomDto;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RoomRepository {

    private final static Map<Long, RoomDto> store = new HashMap<>();
    private static Long sequence = 0L;

    public RoomDto save(RoomDto roomDto) {
        roomDto.setId(++sequence);
        store.put(roomDto.getId(), roomDto);

        return roomDto;
    }

    public Optional<RoomDto> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<RoomDto> findByMemberId(Long id) {
        List<RoomDto> roomDtoList = new ArrayList<>();

        for (Map.Entry<Long, RoomDto> entry : store.entrySet()) {
            RoomDto roomDto = entry.getValue();
            if (roomDto.getMemberId().equals(id)) {
                roomDtoList.add(roomDto);
            }
        }

        return roomDtoList;
    }
}
