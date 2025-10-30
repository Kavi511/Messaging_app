package com.example.messaging_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.messaging_app.database.MessagingRepository;
import com.example.messaging_app.database.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileAvatar;
    private TextView userNameText;
    private TextView userEmailText;
    private TextView phoneNumberText;
    private TextView accountCreatedText;
    private TextView lastSeenText;

    private LinearLayout editProfileButton;
    private LinearLayout settingsButton;
    private LinearLayout logoutButton;

    private MessagingRepository repository;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get current user email from intent
        currentUserEmail = getIntent().getStringExtra("user_email");
        if (currentUserEmail == null) {
            currentUserEmail = "demo@example.com";
        }

        repository = new MessagingRepository(this);
        setupToolbar();
        initializeViews();
        setupListeners();
        loadUserProfile();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeViews() {
        profileAvatar = findViewById(R.id.profile_avatar);
        userNameText = findViewById(R.id.user_name_text);
        userEmailText = findViewById(R.id.user_email_text);
        phoneNumberText = findViewById(R.id.phone_number_text);
        accountCreatedText = findViewById(R.id.account_created_text);
        lastSeenText = findViewById(R.id.last_seen_text);

        editProfileButton = findViewById(R.id.edit_profile_button);
        settingsButton = findViewById(R.id.settings_button);
        logoutButton = findViewById(R.id.logout_button);
    }

    private void setupListeners() {
        editProfileButton.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profile feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        settingsButton.setOnClickListener(v -> {
            Toast.makeText(this, "Settings feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        logoutButton.setOnClickListener(v -> {
            logout();
        });
    }

    private void loadUserProfile() {
        repository.getUserByEmail(currentUserEmail, new MessagingRepository.DatabaseCallback<User>() {
            @Override
            public void onSuccess(User result) {
                runOnUiThread(() -> {
                    if (result != null) {
                        displayUserProfile(result);
                    } else {
                        // User not found, show default profile
                        displayDefaultProfile();
                    }
                });
            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(() -> {
                    Toast.makeText(ProfileActivity.this, "Error loading profile: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    displayDefaultProfile();
                });
            }
        });
    }

    private void displayUserProfile(User user) {
        // Set avatar (first letter of name)
        String avatarLetter = user.getName() != null && !user.getName().isEmpty()
                ? user.getName().substring(0, 1).toUpperCase()
                : "?";
        profileAvatar.setText(avatarLetter);

        // Set user details
        userNameText.setText(user.getName() != null ? user.getName() : "Unknown User");
        userEmailText.setText(user.getEmail());
        phoneNumberText.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "Not provided");

        // Format account created date
        if (user.getCreatedAt() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            String createdDate = sdf.format(new Date(user.getCreatedAt()));
            accountCreatedText.setText(createdDate);
        } else {
            accountCreatedText.setText("Unknown");
        }

        // Format last seen
        if (user.getLastSeen() > 0) {
            long timeDiff = System.currentTimeMillis() - user.getLastSeen();
            if (timeDiff < 5 * 60 * 1000) { // 5 minutes
                lastSeenText.setText("Online");
            } else if (timeDiff < 24 * 60 * 60 * 1000) { // 24 hours
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                String lastSeenTime = sdf.format(new Date(user.getLastSeen()));
                lastSeenText.setText("Last seen " + lastSeenTime);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
                String lastSeenDate = sdf.format(new Date(user.getLastSeen()));
                lastSeenText.setText("Last seen " + lastSeenDate);
            }
        } else {
            lastSeenText.setText("Never");
        }
    }

    private void displayDefaultProfile() {
        // Set default values when user data is not available
        profileAvatar.setText("?");
        userNameText.setText("Demo User");
        userEmailText.setText(currentUserEmail);
        phoneNumberText.setText("+1 234 567 8900");
        accountCreatedText.setText("January 1, 2024");
        lastSeenText.setText("Online");
    }

    private void logout() {
        // Clear any stored user data and return to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}