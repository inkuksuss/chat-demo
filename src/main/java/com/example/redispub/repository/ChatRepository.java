package com.example.redispub.repository;

import com.example.redispub.repository.dto.RoomInfoDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CrudRepository<RoomInfoDto, Long>, ChatRedisRepository {

    List<RoomInfoDto> findByMemberId(Long id);
}
