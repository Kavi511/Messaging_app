package com.example.messaging_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserInfoActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView nameText;
    private TextView statusText;
    private Button callButton;
    private Button videoCallButton;
    private Button messageButton;
    private String contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Get contact name from intent
        contactName = getIntent().getStringExtra("contact_name");
        if (contactName == null) {
            contactName = "Unknown";
        }

        setupToolbar();
        initializeViews();
        setupListeners();
        loadUserInfo();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User Information");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeViews() {
        profileImageView = findViewById(R.id.profile_image);
        nameText = findViewById(R.id.name_text);
        statusText = findViewById(R.id.status_text);
        callButton = findViewById(R.id.call_button);
        videoCallButton = findViewById(R.id.video_call_button);
        messageButton = findViewById(R.id.message_button);
    }

    private void setupListeners() {
        callButton.setOnClickListener(v -> startCall());
        videoCallButton.setOnClickListener(v -> startVideoCall());
        messageButton.setOnClickListener(v -> startMessage());
    }

    private void loadUserInfo() {
        nameText.setText(contactName);
        statusText.setText("Online");
        // Set profile image
        profileImageView.setImageResource(R.drawable.ic_launcher_foreground);
    }

    private void startCall() {
        Intent intent = new Intent(this, CallActivity.class);
        startActivity(intent);
    }

    private void startVideoCall() {
        Intent intent = new Intent(this, VideoCallActivity.class);
        startActivity(intent);
    }

    private void startMessage() {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("contact_name", contactName);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_block) {
            blockUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void blockUser() {
        android.widget.Toast.makeText(this, "User blocked", android.widget.Toast.LENGTH_SHORT).show();
        finish();
    }
}
