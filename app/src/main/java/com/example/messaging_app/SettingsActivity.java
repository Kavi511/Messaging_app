package com.example.messaging_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private Switch notificationSwitch;
    private Switch soundSwitch;
    private Switch vibrationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToolbar();
        initializeViews();
        setupListeners();
        loadSettings();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeViews() {
        notificationSwitch = findViewById(R.id.notification_switch);
        soundSwitch = findViewById(R.id.sound_switch);
        vibrationSwitch = findViewById(R.id.vibration_switch);
    }

    private void setupListeners() {
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle notification setting change
            android.widget.Toast.makeText(this, "Notifications " + (isChecked ? "enabled" : "disabled"),
                    android.widget.Toast.LENGTH_SHORT).show();
        });

        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle sound setting change
            android.widget.Toast.makeText(this, "Sound " + (isChecked ? "enabled" : "disabled"),
                    android.widget.Toast.LENGTH_SHORT).show();
        });

        vibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle vibration setting change
            android.widget.Toast.makeText(this, "Vibration " + (isChecked ? "enabled" : "disabled"),
                    android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void loadSettings() {
        // Load saved settings
        notificationSwitch.setChecked(true);
        soundSwitch.setChecked(true);
        vibrationSwitch.setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
