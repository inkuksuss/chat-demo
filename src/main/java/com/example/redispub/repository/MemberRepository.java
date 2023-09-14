package com.example.redispub.repository;


import com.example.redispub.repository.dto.MemberDto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final static Map<Long, MemberDto> store = new HashMap<>();

    public MemberDto save(MemberDto memberDto) {
        store.put(memberDto.getId(), memberDto);

        return memberDto;
    }

    public Optional<MemberDto> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
