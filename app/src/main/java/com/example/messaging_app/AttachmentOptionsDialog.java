package com.example.messaging_app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AttachmentOptionsDialog extends DialogFragment {

    private OnAttachmentOptionSelectedListener listener;

    public interface OnAttachmentOptionSelectedListener {
        void onCamera();

        void onGallery();

        void onDocument();

        void onLocation();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAttachmentOptionSelectedListener) {
            listener = (OnAttachmentOptionSelectedListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_attachment_options, container, false);

        Button cameraButton = view.findViewById(R.id.camera_button);
        Button galleryButton = view.findViewById(R.id.gallery_button);
        Button documentButton = view.findViewById(R.id.document_button);
        Button locationButton = view.findViewById(R.id.location_button);

        cameraButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCamera();
            }
            dismiss();
        });

        galleryButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onGallery();
            }
            dismiss();
        });

        documentButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDocument();
            }
            dismiss();
        });

        locationButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLocation();
            }
            dismiss();
        });

        return view;
    }
}
