package com.example.redispub.service.dto;

import java.util.List;

public class RoomInfoDto {

    private final List<Long> totalMemberIdList;

    private final List<Long> currentMemberIdList;

    public RoomInfoDto(List<Long> totalMemberIdList, List<Long> currentMemberIdList) {
        this.totalMemberIdList = totalMemberIdList;
        this.currentMemberIdList = currentMemberIdList;
    }
}
