package com.example.redispub.service.dto;

import com.example.redispub.enums.ActionType;

import java.io.Serializable;

public class ChatDto<T> implements Serializable {

    private Long memberId;

    private Long roomId;

    private String principalName;

    private ActionType actionType;

    private T data;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "ChatDto{" +
                "senderId=" + memberId +
                ", roomId=" + roomId +
                ", name='" + principalName + '\'' +
                ", actionType=" + actionType +
                ", data=" + data +
                '}';
    }
}
