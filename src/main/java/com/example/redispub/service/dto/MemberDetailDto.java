package com.example.redispub.service.dto;

import com.example.redispub.entity.Member;

public class MemberDetailDto {

    private Long id;
    private String name;

    public MemberDetailDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MemberDetailDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
