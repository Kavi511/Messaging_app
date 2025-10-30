package com.example.messaging_app;

public class Status {
    private String username;
    private String avatarLetter;
    private String timeAgo;
    private boolean isMyStatus;
    private String statusText;
    private long timestamp;

    public Status(String username, String avatarLetter, String timeAgo, boolean isMyStatus) {
        this.username = username;
        this.avatarLetter = avatarLetter;
        this.timeAgo = timeAgo;
        this.isMyStatus = isMyStatus;
        this.timestamp = System.currentTimeMillis();
    }

    public Status(String username, String avatarLetter, String timeAgo, boolean isMyStatus, String statusText) {
        this.username = username;
        this.avatarLetter = avatarLetter;
        this.timeAgo = timeAgo;
        this.isMyStatus = isMyStatus;
        this.statusText = statusText;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarLetter() {
        return avatarLetter;
    }

    public void setAvatarLetter(String avatarLetter) {
        this.avatarLetter = avatarLetter;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public boolean isMyStatus() {
        return isMyStatus;
    }

    public void setMyStatus(boolean myStatus) {
        isMyStatus = myStatus;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
