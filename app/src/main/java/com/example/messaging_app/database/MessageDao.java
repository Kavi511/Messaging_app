package com.example.messaging_app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    long insertMessage(Message message);

    @Update
    void updateMessage(Message message);

    @Delete
    void deleteMessage(Message message);

    @Query("SELECT * FROM messages WHERE chatId = :chatId AND text LIKE :searchText AND isDeleted = 0 ORDER BY timestamp ASC")
    List<Message> searchMessagesInChat(long chatId, String searchText);

    @Query("SELECT * FROM messages WHERE chatId = :chatId AND isDeleted = 0 ORDER BY timestamp ASC")
    List<Message> getMessagesByChatId(long chatId);

    @Query("SELECT * FROM messages WHERE id = :messageId")
    Message getMessageById(long messageId);

    @Query("SELECT * FROM messages WHERE text LIKE '%' || :searchText || '%' AND isDeleted = 0 ORDER BY timestamp DESC")
    List<Message> searchAllMessages(String searchText);

    @Query("SELECT * FROM messages WHERE chatId = :chatId AND text LIKE '%' || :searchText || '%' AND isDeleted = 0 ORDER BY timestamp ASC LIMIT 1")
    Message getFirstMatchingMessage(long chatId, String searchText);

    @Query("UPDATE messages SET isRead = 1 WHERE chatId = :chatId AND isSent = 0")
    void markMessagesAsRead(long chatId);

    @Query("UPDATE messages SET isSelected = :selected WHERE id = :messageId")
    void setMessageSelection(long messageId, boolean selected);

    @Query("UPDATE messages SET isSelected = :selected WHERE chatId = :chatId")
    void setAllMessagesSelection(long chatId, boolean selected);

    @Query("SELECT * FROM messages WHERE isSelected = 1 AND isDeleted = 0")
    List<Message> getSelectedMessages();

    @Query("DELETE FROM messages WHERE isSelected = 1")
    void deleteSelectedMessages();

    @Query("UPDATE messages SET isDeleted = 1 WHERE id = :messageId")
    void softDeleteMessage(long messageId);

    @Query("UPDATE messages SET isDeleted = 1 WHERE isSelected = 1")
    void softDeleteSelectedMessages();

    @Query("DELETE FROM messages WHERE id = :messageId")
    void hardDeleteMessage(long messageId);

    @Query("SELECT COUNT(*) FROM messages WHERE chatId = :chatId AND isSent = 0 AND isRead = 0 AND isDeleted = 0")
    int getUnreadCount(long chatId);

    @Query("SELECT * FROM messages WHERE imagePath IS NOT NULL AND imagePath != '' AND isDeleted = 0 ORDER BY timestamp DESC")
    List<Message> getMessagesWithImages();

    @Query("SELECT * FROM messages WHERE chatId = :chatId AND imagePath IS NOT NULL AND imagePath != '' AND isDeleted = 0 ORDER BY timestamp DESC")
    List<Message> getImagesInChat(long chatId);
}
