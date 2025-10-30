package com.example.messaging_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    private String email;
    private String name;
    private String phoneNumber;
    private String profileImagePath;
    private long createdAt;
    private long lastSeen;

    public User(@NonNull String email, String name, String phoneNumber, String profileImagePath) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImagePath = profileImagePath;
        this.createdAt = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
    }

    // Getters and Setters
    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
}
