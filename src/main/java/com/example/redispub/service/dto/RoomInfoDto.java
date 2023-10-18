package com.example.redispub.service.dto;

import com.example.redispub.entity.Message;
import org.springframework.data.domain.Slice;

import java.io.Serializable;
import java.util.Set;

public class RoomInfoDto implements Serializable {

    private final Set<Long> totalMemberIdList;

    private final Set<Long> currentMemberIdList;

    private final Slice<Message> messageList;

    public RoomInfoDto(Set<Long> totalMemberIdList, Set<Long> currentMemberIdList, Slice<Message> messageList) {
        this.totalMemberIdList = totalMemberIdList;
        this.currentMemberIdList = currentMemberIdList;
        this.messageList = messageList;
    }

    public Set<Long> getTotalMemberIdList() {
        return totalMemberIdList;
    }

    public Set<Long> getCurrentMemberIdList() {
        return currentMemberIdList;
    }

    public Slice<Message> getMessageList() {
        return messageList;
    }
}
