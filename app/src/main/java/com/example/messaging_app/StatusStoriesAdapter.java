package com.example.messaging_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StatusStoriesAdapter extends RecyclerView.Adapter<StatusStoriesAdapter.StatusStoryViewHolder> {

    private List<Status> statusList;
    private OnStatusStoryClickListener listener;

    public interface OnStatusStoryClickListener {
        void onMyStatusClick();

        void onStatusStoryClick(Status status);
    }

    public StatusStoriesAdapter(List<Status> statusList, OnStatusStoryClickListener listener) {
        this.statusList = statusList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StatusStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status_story, parent, false);
        return new StatusStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusStoryViewHolder holder, int position) {
        Status status = statusList.get(position);
        holder.bind(status);
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    class StatusStoryViewHolder extends RecyclerView.ViewHolder {
        private TextView statusCircle;
        private TextView addStatusIcon;
        private TextView statusName;

        public StatusStoryViewHolder(@NonNull View itemView) {
            super(itemView);

            statusCircle = itemView.findViewById(R.id.status_circle);
            addStatusIcon = itemView.findViewById(R.id.add_status_icon);
            statusName = itemView.findViewById(R.id.status_name);
        }

        public void bind(Status status) {
            // Set avatar letter
            statusCircle.setText(status.getAvatarLetter());

            // Set status name
            statusName.setText(status.getUsername());

            if (status.isMyStatus()) {
                // Show add status icon for "My Status"
                addStatusIcon.setVisibility(View.VISIBLE);

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onMyStatusClick();
                    }
                });
            } else {
                // Hide add status icon for other statuses
                addStatusIcon.setVisibility(View.GONE);

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onStatusStoryClick(status);
                    }
                });
            }
        }
    }
}
