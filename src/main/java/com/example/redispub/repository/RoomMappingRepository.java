package com.example.redispub.repository;

import com.example.redispub.entity.RoomMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RoomMappingRepository extends CrudRepository<RoomMapping, Long> {

    List<RoomMapping> findByMemberId(Long memberId);

}
