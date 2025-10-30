package com.example.messaging_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "chats")
public class Chat {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String participantEmail;
    private String participantName;
    private String lastMessage;
    private long lastMessageTime;
    private int unreadCount;
    private String avatarLetter;
    private long createdAt;

    public Chat(@NonNull String participantEmail, String participantName, String lastMessage, 
                long lastMessageTime, int unreadCount, String avatarLetter) {
        this.participantEmail = participantEmail;
        this.participantName = participantName;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
        this.avatarLetter = avatarLetter;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(@NonNull String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getAvatarLetter() {
        return avatarLetter;
    }

    public void setAvatarLetter(String avatarLetter) {
        this.avatarLetter = avatarLetter;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
