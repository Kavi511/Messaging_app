package com.example.messaging_app.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = { User.class, Chat.class, Message.class }, version = 2, exportSchema = false)
public abstract class MessagingDatabase extends RoomDatabase {

    private static MessagingDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract ChatDao chatDao();

    public abstract MessageDao messageDao();

    public static synchronized MessagingDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MessagingDatabase.class,
                    "messaging_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
