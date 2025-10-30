package com.example.messaging_app.database;

import android.content.Context;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessagingRepository {
    private UserDao userDao;
    private ChatDao chatDao;
    private MessageDao messageDao;
    private ExecutorService executor;

    public MessagingRepository(Context context) {
        MessagingDatabase database = MessagingDatabase.getInstance(context);
        userDao = database.userDao();
        chatDao = database.chatDao();
        messageDao = database.messageDao();
        executor = Executors.newFixedThreadPool(4);
    }

    // User operations
    public void insertUser(User user, DatabaseCallback<Long> callback) {
        executor.execute(() -> {
            try {
                long id = userDao.insertUser(user);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getUserByEmail(String email, DatabaseCallback<User> callback) {
        executor.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);
                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void authenticateUser(String email, String phoneNumber, DatabaseCallback<User> callback) {
        executor.execute(() -> {
            try {
                User user = userDao.authenticateUser(email, phoneNumber);
                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    // Chat operations
    public void insertChat(Chat chat, DatabaseCallback<Long> callback) {
        executor.execute(() -> {
            try {
                long id = chatDao.insertChat(chat);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getAllChats(DatabaseCallback<List<Chat>> callback) {
        executor.execute(() -> {
            try {
                List<Chat> chats = chatDao.getAllChats();
                callback.onSuccess(chats);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getChatById(long chatId, DatabaseCallback<Chat> callback) {
        executor.execute(() -> {
            try {
                Chat chat = chatDao.getChatById(chatId);
                callback.onSuccess(chat);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getChatByParticipant(String participantEmail, DatabaseCallback<Chat> callback) {
        executor.execute(() -> {
            try {
                Chat chat = chatDao.getChatByParticipant(participantEmail);
                callback.onSuccess(chat);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateLastMessage(long chatId, String message, long timestamp) {
        executor.execute(() -> {
            chatDao.updateLastMessage(chatId, message, timestamp);
        });
    }

    public void markChatAsRead(long chatId) {
        executor.execute(() -> {
            chatDao.markAsRead(chatId);
        });
    }

    // Message operations
    public void insertMessage(Message message, DatabaseCallback<Long> callback) {
        executor.execute(() -> {
            try {
                long id = messageDao.insertMessage(message);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void searchMessagesInChat(long chatId, String searchText, DatabaseCallback<List<Message>> callback) {
        executor.execute(() -> {
            try {
                List<Message> messages = messageDao.searchMessagesInChat(chatId, "%" + searchText + "%");
                callback.onSuccess(messages);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getMessagesByChatId(long chatId, DatabaseCallback<List<Message>> callback) {
        executor.execute(() -> {
            try {
                List<Message> messages = messageDao.getMessagesByChatId(chatId);
                callback.onSuccess(messages);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void searchAllMessages(String searchText, DatabaseCallback<List<Message>> callback) {
        executor.execute(() -> {
            try {
                List<Message> messages = messageDao.searchAllMessages(searchText);
                callback.onSuccess(messages);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getFirstMatchingMessage(long chatId, String searchText, DatabaseCallback<Message> callback) {
        executor.execute(() -> {
            try {
                Message message = messageDao.getFirstMatchingMessage(chatId, searchText);
                callback.onSuccess(message);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateMessage(Message message, DatabaseCallback<Void> callback) {
        executor.execute(() -> {
            try {
                messageDao.updateMessage(message);
                callback.onSuccess(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteMessage(long messageId) {
        executor.execute(() -> {
            messageDao.softDeleteMessage(messageId);
        });
    }

    public void deleteSelectedMessages() {
        executor.execute(() -> {
            messageDao.softDeleteSelectedMessages();
        });
    }

    public void setMessageSelection(long messageId, boolean selected) {
        executor.execute(() -> {
            messageDao.setMessageSelection(messageId, selected);
        });
    }

    public void getSelectedMessages(DatabaseCallback<List<Message>> callback) {
        executor.execute(() -> {
            try {
                List<Message> messages = messageDao.getSelectedMessages();
                callback.onSuccess(messages);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getMessagesWithImages(DatabaseCallback<List<Message>> callback) {
        executor.execute(() -> {
            try {
                List<Message> messages = messageDao.getMessagesWithImages();
                callback.onSuccess(messages);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getUnreadCount(long chatId, DatabaseCallback<Integer> callback) {
        executor.execute(() -> {
            try {
                int count = messageDao.getUnreadCount(chatId);
                callback.onSuccess(count);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    // Callback interface
    public interface DatabaseCallback<T> {
        void onSuccess(T result);

        void onError(Exception error);
    }
}
