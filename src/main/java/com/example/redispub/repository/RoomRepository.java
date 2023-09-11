package com.example.redispub.repository;


import com.example.redispub.repository.dto.RoomDto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class RoomRepository {

    private final HashMap<Long, RoomDto> store = new HashMap<>();


}
