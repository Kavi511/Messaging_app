package com.example.messaging_app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE email = :email AND phoneNumber = :phoneNumber")
    User authenticateUser(String email, String phoneNumber);

    @Query("UPDATE users SET lastSeen = :timestamp WHERE email = :email")
    void updateLastSeen(String email, long timestamp);

    @Query("UPDATE users SET profileImagePath = :imagePath WHERE email = :email")
    void updateProfileImage(String email, String imagePath);
}
