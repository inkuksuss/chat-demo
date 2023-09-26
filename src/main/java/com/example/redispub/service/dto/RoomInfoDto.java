package com.example.redispub.service.dto;

import java.io.Serializable;
import java.util.List;

public class RoomInfoDto implements Serializable {

    private List<Long> totalMemberIdList;

    private List<Long> currentMemberIdList;

    public RoomInfoDto(List<Long> totalMemberIdList, List<Long> currentMemberIdList) {
        this.totalMemberIdList = totalMemberIdList;
        this.currentMemberIdList = currentMemberIdList;
    }

    public void setTotalMemberIdList(List<Long> totalMemberIdList) {
        this.totalMemberIdList = totalMemberIdList;
    }

    public void setCurrentMemberIdList(List<Long> currentMemberIdList) {
        this.currentMemberIdList = currentMemberIdList;
    }

    public List<Long> getTotalMemberIdList() {
        return totalMemberIdList;
    }

    public List<Long> getCurrentMemberIdList() {
        return currentMemberIdList;
    }
}
