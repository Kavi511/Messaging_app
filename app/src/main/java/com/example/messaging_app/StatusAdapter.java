package com.example.messaging_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {

    private List<Status> statusList;
    private OnStatusClickListener listener;

    public interface OnStatusClickListener {
        void onMyStatusClick();

        void onStatusClick(Status status);
    }

    public StatusAdapter(List<Status> statusList, OnStatusClickListener listener) {
        this.statusList = statusList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        Status status = statusList.get(position);
        holder.bind(status);
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class StatusViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout myStatusSection;
        private TextView myStatusAvatar;
        private TextView addStatusIcon;

        private LinearLayout statusItem;
        private TextView statusAvatar;
        private TextView statusUsername;
        private TextView statusTime;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);

            myStatusSection = itemView.findViewById(R.id.my_status_section);
            myStatusAvatar = itemView.findViewById(R.id.my_status_avatar);
            addStatusIcon = itemView.findViewById(R.id.add_status_icon);

            statusItem = itemView.findViewById(R.id.status_item);
            statusAvatar = itemView.findViewById(R.id.status_avatar);
            statusUsername = itemView.findViewById(R.id.status_username);
            statusTime = itemView.findViewById(R.id.status_time);
        }

        public void bind(Status status) {
            if (status.isMyStatus()) {
                // Show My Status section
                myStatusSection.setVisibility(View.VISIBLE);
                statusItem.setVisibility(View.GONE);

                myStatusAvatar.setText(status.getAvatarLetter());

                myStatusSection.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onMyStatusClick();
                    }
                });
            } else {
                // Show regular status item
                myStatusSection.setVisibility(View.GONE);
                statusItem.setVisibility(View.VISIBLE);

                statusAvatar.setText(status.getAvatarLetter());
                statusUsername.setText(status.getUsername());
                statusTime.setText(status.getTimeAgo());

                statusItem.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onStatusClick(status);
                    }
                });
            }
        }
    }
}
