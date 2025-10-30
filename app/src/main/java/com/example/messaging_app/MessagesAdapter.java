package com.example.messaging_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<com.example.messaging_app.database.Message> messageList;
    private OnMessageActionListener messageActionListener;
    private boolean isSelectionMode = false;
    private List<com.example.messaging_app.database.Message> selectedMessages = new ArrayList<>();

    public interface OnMessageActionListener {
        void onEditMessage(com.example.messaging_app.database.Message message);

        void onDeleteMessage(com.example.messaging_app.database.Message message);

        void onShareMessage(com.example.messaging_app.database.Message message);

        void onMessageSelected(com.example.messaging_app.database.Message message, boolean isSelected);
    }

    public MessagesAdapter(List<com.example.messaging_app.database.Message> messageList,
            OnMessageActionListener listener) {
        this.messageList = messageList;
        this.messageActionListener = listener;
    }

    public void setSelectionMode(boolean selectionMode) {
        this.isSelectionMode = selectionMode;
        if (!selectionMode) {
            selectedMessages.clear();
        }
        notifyDataSetChanged();
    }

    public List<com.example.messaging_app.database.Message> getSelectedMessages() {
        return selectedMessages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        com.example.messaging_app.database.Message message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout sentMessageContainer;
        private LinearLayout receivedMessageContainer;
        private TextView sentMessageText;
        private TextView sentTimestampText;
        private TextView receivedMessageText;
        private TextView receivedTimestampText;
        private ImageView sentMessageImage;
        private ImageView receivedMessageImage;
        private ImageView sentCheckbox;
        private ImageView receivedCheckbox;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            // Sent message elements
            sentMessageContainer = itemView.findViewById(R.id.sent_message_container);
            sentMessageText = itemView.findViewById(R.id.sent_message_text);
            sentTimestampText = itemView.findViewById(R.id.sent_timestamp_text);
            sentMessageImage = itemView.findViewById(R.id.sent_message_image);
            sentCheckbox = itemView.findViewById(R.id.sent_checkbox);

            // Received message elements
            receivedMessageContainer = itemView.findViewById(R.id.received_message_container);
            receivedMessageText = itemView.findViewById(R.id.received_message_text);
            receivedTimestampText = itemView.findViewById(R.id.received_timestamp_text);
            receivedMessageImage = itemView.findViewById(R.id.received_message_image);
            receivedCheckbox = itemView.findViewById(R.id.received_checkbox);
        }

        public void bind(com.example.messaging_app.database.Message message) {
            // Don't show deleted messages
            if (message.isDeleted()) {
                itemView.setVisibility(View.GONE);
                return;
            }

            itemView.setVisibility(View.VISIBLE);

            if (message.isSent()) {
                // Show sent message container, hide received
                sentMessageContainer.setVisibility(View.VISIBLE);
                receivedMessageContainer.setVisibility(View.GONE);

                // Handle image if present
                if (message.hasImage()) {
                    sentMessageImage.setVisibility(View.VISIBLE);
                    sentMessageText.setVisibility(View.GONE);

                    // Load image using Glide
                    Glide.with(itemView.getContext())
                            .load(message.getImagePath())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_placeholder)
                            .into(sentMessageImage);
                } else {
                    sentMessageImage.setVisibility(View.GONE);
                    sentMessageText.setVisibility(View.VISIBLE);
                    sentMessageText.setText(message.getText());
                }

                // Set timestamp with edited indicator
                String timestampText = message.getFormattedTimestamp();
                if (message.isEdited()) {
                    timestampText += " (edited)";
                }
                sentTimestampText.setText(timestampText);

                // Handle selection mode
                if (isSelectionMode) {
                    sentCheckbox.setVisibility(View.VISIBLE);
                    boolean isSelected = selectedMessages.contains(message);
                    sentCheckbox
                            .setImageResource(isSelected ? R.drawable.ic_check_box : R.drawable.ic_check_box_outline);

                    // Add click listener for selection
                    sentMessageContainer.setOnClickListener(v -> {
                        toggleMessageSelection(message);
                    });

                    // Remove long click listener in selection mode
                    sentMessageContainer.setOnLongClickListener(null);
                } else {
                    sentCheckbox.setVisibility(View.GONE);

                    // Add long click listener for edit/delete options (only for sent messages)
                    sentMessageContainer.setOnLongClickListener(v -> {
                        showMessageOptionsDialog(message);
                        return true;
                    });

                    // Remove selection click listener
                    sentMessageContainer.setOnClickListener(null);
                }

            } else {
                // Show received message container, hide sent
                receivedMessageContainer.setVisibility(View.VISIBLE);
                sentMessageContainer.setVisibility(View.GONE);

                // Handle image if present
                if (message.hasImage()) {
                    receivedMessageImage.setVisibility(View.VISIBLE);
                    receivedMessageText.setVisibility(View.GONE);

                    // Load image using Glide
                    Glide.with(itemView.getContext())
                            .load(message.getImagePath())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_placeholder)
                            .into(receivedMessageImage);
                } else {
                    receivedMessageImage.setVisibility(View.GONE);
                    receivedMessageText.setVisibility(View.VISIBLE);
                    receivedMessageText.setText(message.getText());
                }

                // Set timestamp
                receivedTimestampText.setText(message.getFormattedTimestamp());

                // Handle selection mode for received messages
                if (isSelectionMode) {
                    receivedCheckbox.setVisibility(View.VISIBLE);
                    boolean isSelected = selectedMessages.contains(message);
                    receivedCheckbox
                            .setImageResource(isSelected ? R.drawable.ic_check_box : R.drawable.ic_check_box_outline);

                    // Add click listener for selection
                    receivedMessageContainer.setOnClickListener(v -> {
                        toggleMessageSelection(message);
                    });

                    // Remove long click listener in selection mode
                    receivedMessageContainer.setOnLongClickListener(null);
                } else {
                    receivedCheckbox.setVisibility(View.GONE);

                    // Add long click listener for share options (received messages can be shared)
                    receivedMessageContainer.setOnLongClickListener(v -> {
                        showReceivedMessageOptionsDialog(message);
                        return true;
                    });

                    // Remove selection click listener
                    receivedMessageContainer.setOnClickListener(null);
                }
            }
        }

        private void showMessageOptionsDialog(com.example.messaging_app.database.Message message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Message Options");

            String[] options = { "Edit Message", "Delete Message", "Share Message" };
            builder.setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0: // Edit Message
                        if (messageActionListener != null) {
                            messageActionListener.onEditMessage(message);
                        }
                        break;
                    case 1: // Delete Message
                        if (messageActionListener != null) {
                            messageActionListener.onDeleteMessage(message);
                        }
                        break;
                    case 2: // Share Message
                        if (messageActionListener != null) {
                            messageActionListener.onShareMessage(message);
                        }
                        break;
                }
            });

            builder.show();
        }

        private void showReceivedMessageOptionsDialog(com.example.messaging_app.database.Message message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Message Options");

            String[] options = { "Share Message" };
            builder.setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0: // Share Message
                        if (messageActionListener != null) {
                            messageActionListener.onShareMessage(message);
                        }
                        break;
                }
            });

            builder.show();
        }

        private void toggleMessageSelection(com.example.messaging_app.database.Message message) {
            boolean isSelected = selectedMessages.contains(message);
            if (isSelected) {
                selectedMessages.remove(message);
            } else {
                selectedMessages.add(message);
            }

            // Notify listener
            if (messageActionListener != null) {
                messageActionListener.onMessageSelected(message, !isSelected);
            }

            // Update UI
            notifyItemChanged(getAdapterPosition());
        }
    }
}
