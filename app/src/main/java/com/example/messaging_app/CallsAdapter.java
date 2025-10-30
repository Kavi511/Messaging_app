package com.example.messaging_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.CallViewHolder> {

    private List<Call> callList;
    private OnCallClickListener listener;

    public interface OnCallClickListener {
        void onCallClick(Call call);
    }

    public CallsAdapter(List<Call> callList, OnCallClickListener listener) {
        this.callList = callList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        Call call = callList.get(position);
        holder.bind(call);
    }

    @Override
    public int getItemCount() {
        return callList.size();
    }

    class CallViewHolder extends RecyclerView.ViewHolder {
        private TextView callAvatar;
        private TextView callContactName;
        private ImageView callDirectionIcon;
        private TextView callTime;
        private ImageView callTypeIcon;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);

            callAvatar = itemView.findViewById(R.id.call_avatar);
            callContactName = itemView.findViewById(R.id.call_contact_name);
            callDirectionIcon = itemView.findViewById(R.id.call_direction_icon);
            callTime = itemView.findViewById(R.id.call_time);
            callTypeIcon = itemView.findViewById(R.id.call_type_icon);
        }

        public void bind(Call call) {
            // Set avatar
            callAvatar.setText(call.getAvatarLetter());

            // Set contact name
            callContactName.setText(call.getContactName());

            // Set call time
            callTime.setText(call.getCallTime());

            // Set call direction icon and color
            switch (call.getCallDirection()) {
                case OUTGOING:
                    callDirectionIcon.setImageResource(R.drawable.ic_call_outgoing);
                    callDirectionIcon.setColorFilter(itemView.getContext().getColor(R.color.primary_dark));
                    break;
                case INCOMING:
                    callDirectionIcon.setImageResource(R.drawable.ic_call_incoming);
                    callDirectionIcon.setColorFilter(itemView.getContext().getColor(R.color.primary_dark));
                    break;
                case MISSED:
                    callDirectionIcon.setImageResource(R.drawable.ic_call_missed);
                    callDirectionIcon.setColorFilter(itemView.getContext().getColor(R.color.destructive_dark));
                    break;
            }

            // Set call type icon
            if (call.getCallType() == Call.CallType.VIDEO) {
                callTypeIcon.setImageResource(R.drawable.ic_video_call);
            } else {
                callTypeIcon.setImageResource(R.drawable.ic_phone);
            }
            callTypeIcon.setColorFilter(itemView.getContext().getColor(R.color.primary_dark));

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCallClick(call);
                }
            });
        }
    }
}
