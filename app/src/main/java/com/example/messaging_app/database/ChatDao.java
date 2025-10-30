package com.example.messaging_app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface ChatDao {
    
    @Insert
    long insertChat(Chat chat);
    
    @Update
    void updateChat(Chat chat);
    
    @Delete
    void deleteChat(Chat chat);
    
    @Query("SELECT * FROM chats ORDER BY lastMessageTime DESC")
    List<Chat> getAllChats();
    
    @Query("SELECT * FROM chats WHERE participantEmail = :email")
    Chat getChatByParticipant(String email);
    
    @Query("SELECT * FROM chats WHERE id = :chatId")
    Chat getChatById(long chatId);
    
    @Query("UPDATE chats SET lastMessage = :message, lastMessageTime = :timestamp WHERE id = :chatId")
    void updateLastMessage(long chatId, String message, long timestamp);
    
    @Query("UPDATE chats SET unreadCount = :count WHERE id = :chatId")
    void updateUnreadCount(long chatId, int count);
    
    @Query("UPDATE chats SET unreadCount = 0 WHERE id = :chatId")
    void markAsRead(long chatId);
    
    @Query("DELETE FROM chats WHERE id = :chatId")
    void deleteChatById(long chatId);
}
