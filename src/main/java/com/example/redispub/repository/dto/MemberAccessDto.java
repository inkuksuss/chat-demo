package com.example.redispub.repository.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class MemberAccessDto {

    private Long memberId;
    private Long roomId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastAccessDate;

    public MemberAccessDto(Long memberId, Long roomId, LocalDateTime lastAccessDate) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.lastAccessDate = lastAccessDate;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDateTime getLastAccessDate() {
        return lastAccessDate;
    }
}
