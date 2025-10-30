package com.example.messaging_app;

public class Chat {
    private String name;
    private String lastMessage;
    private String timestamp;
    private int unreadCount;
    private String avatarLetter;

    public Chat(String name, String lastMessage, String timestamp, int unreadCount, String avatarLetter) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
        this.avatarLetter = avatarLetter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean hasUnreadMessage() {
        return unreadCount > 0;
    }

    public String getAvatarLetter() {
        return avatarLetter;
    }

    public void setAvatarLetter(String avatarLetter) {
        this.avatarLetter = avatarLetter;
    }
}
