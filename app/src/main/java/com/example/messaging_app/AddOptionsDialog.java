package com.example.messaging_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddOptionsDialog extends DialogFragment {

    private OnAddOptionSelectedListener listener;

    public interface OnAddOptionSelectedListener {
        void onAddFriend();

        void onCreateGroup();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAddOptionSelectedListener) {
            listener = (OnAddOptionSelectedListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Add Options");
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_options, container, false);

        Button addFriendButton = view.findViewById(R.id.add_friend_button);
        Button createGroupButton = view.findViewById(R.id.create_group_button);

        addFriendButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddFriend();
            }
            dismiss();
        });

        createGroupButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCreateGroup();
            }
            dismiss();
        });

        return view;
    }
}
