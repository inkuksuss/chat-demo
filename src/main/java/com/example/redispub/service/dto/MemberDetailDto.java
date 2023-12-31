package com.example.redispub.service.dto;

import com.example.redispub.entity.RoomMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class MemberDetailDto {

    private Long id;
    private String name;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastAccessDate;

    public MemberDetailDto(RoomMapper roomMapper) {
        this.id = roomMapper.getMember().getId();
        this.name = roomMapper.getMember().getName();
        this.lastAccessDate = roomMapper.getLastAccessDate();
    }

    public MemberDetailDto(Long id, LocalDateTime lastAccessDate) {
        this.id = id;
        this.lastAccessDate = lastAccessDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLastAccessDate() {
        return lastAccessDate;
    }
}
