package com.example.messaging_app;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CallActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView contactNameText;
    private TextView callDurationText;
    private ImageView endCallButton;
    private ImageView backButton;

    private Handler durationHandler;
    private Runnable durationRunnable;
    private int callDurationSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        initializeViews();
        setupListeners();
        startCall();
        startDurationTimer();
    }

    private void initializeViews() {
        profileImageView = findViewById(R.id.profile_image);
        contactNameText = findViewById(R.id.contact_name_text);
        callDurationText = findViewById(R.id.call_duration_text);
        endCallButton = findViewById(R.id.end_call_button);
        backButton = findViewById(R.id.back_button);
    }

    private void setupListeners() {
        endCallButton.setOnClickListener(v -> endCall());
        backButton.setOnClickListener(v -> finish());
    }

    private void startCall() {
        // Set contact name from intent or use default
        String contactName = getIntent().getStringExtra("contact_name");
        if (contactName != null) {
            contactNameText.setText(contactName);
        }

        // Initialize call duration
        callDurationText.setText("00:00:00");
    }

    private void startDurationTimer() {
        durationHandler = new Handler();
        durationRunnable = new Runnable() {
            @Override
            public void run() {
                callDurationSeconds++;
                updateDurationDisplay();
                durationHandler.postDelayed(this, 1000);
            }
        };
        durationHandler.post(durationRunnable);
    }

    private void updateDurationDisplay() {
        int hours = callDurationSeconds / 3600;
        int minutes = (callDurationSeconds % 3600) / 60;
        int seconds = callDurationSeconds % 60;

        String duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        callDurationText.setText(duration);
    }

    private void endCall() {
        if (durationHandler != null) {
            durationHandler.removeCallbacks(durationRunnable);
        }
        android.widget.Toast.makeText(this, "Call ended", android.widget.Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (durationHandler != null) {
            durationHandler.removeCallbacks(durationRunnable);
        }
    }

    private void showMoreOptions() {
        android.widget.Toast.makeText(this, "More options", android.widget.Toast.LENGTH_SHORT).show();
    }
}
