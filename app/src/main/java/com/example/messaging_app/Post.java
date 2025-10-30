package com.example.messaging_app;

import java.io.Serializable;

public class Post implements Serializable {
    private String id;
    private String username;
    private String userInitial;
    private String content;
    private String imageUrl;
    private String timestamp;
    private int likesCount;
    private boolean isLiked;
    private String userId;

    public Post() {
        // Default constructor
    }

    public Post(String id, String username, String userInitial, String content, String imageUrl, String timestamp,
            int likesCount, boolean isLiked, String userId) {
        this.id = id;
        this.username = username;
        this.userInitial = userInitial;
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.likesCount = likesCount;
        this.isLiked = isLiked;
        this.userId = userId;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserInitial() {
        return userInitial;
    }

    public void setUserInitial(String userInitial) {
        this.userInitial = userInitial;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
