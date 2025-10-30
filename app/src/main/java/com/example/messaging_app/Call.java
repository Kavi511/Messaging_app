package com.example.messaging_app;

public class Call {
    private String contactName;
    private String avatarLetter;
    private String callTime;
    private CallType callType;
    private CallDirection callDirection;
    private boolean isGroupCall;

    public enum CallType {
        VOICE, VIDEO
    }

    public enum CallDirection {
        OUTGOING, INCOMING, MISSED
    }

    public Call(String contactName, String avatarLetter, String callTime, CallType callType,
            CallDirection callDirection) {
        this.contactName = contactName;
        this.avatarLetter = avatarLetter;
        this.callTime = callTime;
        this.callType = callType;
        this.callDirection = callDirection;
        this.isGroupCall = false;
    }

    public Call(String contactName, String avatarLetter, String callTime, CallType callType,
            CallDirection callDirection, boolean isGroupCall) {
        this.contactName = contactName;
        this.avatarLetter = avatarLetter;
        this.callTime = callTime;
        this.callType = callType;
        this.callDirection = callDirection;
        this.isGroupCall = isGroupCall;
    }

    // Getters and Setters
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAvatarLetter() {
        return avatarLetter;
    }

    public void setAvatarLetter(String avatarLetter) {
        this.avatarLetter = avatarLetter;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public CallDirection getCallDirection() {
        return callDirection;
    }

    public void setCallDirection(CallDirection callDirection) {
        this.callDirection = callDirection;
    }

    public boolean isGroupCall() {
        return isGroupCall;
    }

    public void setGroupCall(boolean groupCall) {
        isGroupCall = groupCall;
    }
}
