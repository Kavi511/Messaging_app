package com.example.messaging_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    private List<Chat> chatList;
    private OnChatClickListener onChatClickListener;

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public ChatsAdapter(List<Chat> chatList, OnChatClickListener listener) {
        this.chatList = chatList;
        this.onChatClickListener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView avatarTextView;
        private TextView nameTextView;
        private TextView lastMessageTextView;
        private TextView timestampTextView;
        private TextView unreadBadgeTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarTextView = itemView.findViewById(R.id.avatar_text);
            nameTextView = itemView.findViewById(R.id.name_text);
            lastMessageTextView = itemView.findViewById(R.id.last_message_text);
            timestampTextView = itemView.findViewById(R.id.timestamp_text);
            unreadBadgeTextView = itemView.findViewById(R.id.unread_badge);

            itemView.setOnClickListener(v -> {
                if (onChatClickListener != null) {
                    onChatClickListener.onChatClick(chatList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Chat chat) {
            // Set avatar letter
            avatarTextView.setText(chat.getAvatarLetter());
            
            // Set name
            nameTextView.setText(chat.getName());
            
            // Set last message
            lastMessageTextView.setText(chat.getLastMessage());
            
            // Set timestamp
            timestampTextView.setText(chat.getTimestamp());

            // Handle unread badge
            if (chat.hasUnreadMessage()) {
                unreadBadgeTextView.setVisibility(View.VISIBLE);
                unreadBadgeTextView.setText(String.valueOf(chat.getUnreadCount()));
                // Make name bold for unread messages
                nameTextView.setTextColor(itemView.getContext().getColor(R.color.foreground_dark));
            } else {
                unreadBadgeTextView.setVisibility(View.GONE);
                // Make name normal for read messages
                nameTextView.setTextColor(itemView.getContext().getColor(R.color.foreground_dark));
            }
        }
    }
}
