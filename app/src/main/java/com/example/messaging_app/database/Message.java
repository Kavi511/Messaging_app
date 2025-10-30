package com.example.messaging_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;

@Entity(tableName = "messages", foreignKeys = @ForeignKey(entity = Chat.class, parentColumns = "id", childColumns = "chatId", onDelete = ForeignKey.CASCADE))
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long chatId;
    @NonNull
    private String text;
    private String imagePath;
    private boolean isSent;
    private boolean isRead;
    private long timestamp;
    private String senderEmail;
    private String receiverEmail;
    private boolean isSelected; // For group selection
    private boolean isDeleted; // Soft delete
    private boolean isEdited; // Track if message was edited

    public Message(long chatId, @NonNull String text, String imagePath, boolean isSent,
            String senderEmail, String receiverEmail) {
        this.chatId = chatId;
        this.text = text;
        this.imagePath = imagePath;
        this.isSent = isSent;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.isRead = false;
        this.timestamp = System.currentTimeMillis();
        this.isSelected = false;
        this.isDeleted = false;
        this.isEdited = false;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    // Helper methods
    public boolean hasImage() {
        return imagePath != null && !imagePath.isEmpty();
    }

    public String getFormattedTimestamp() {
        return android.text.format.DateFormat.format("h:mm a", timestamp).toString();
    }
}
