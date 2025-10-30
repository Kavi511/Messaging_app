package com.example.messaging_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private EditText otpEditText;
    private Button sendOtpButton;
    private Button verifyOtpButton;
    private Button resendOtpButton;
    private TextView resendTimerText;
    private boolean isOtpSent = false;
    private int resendTimer = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        otpEditText = findViewById(R.id.otp_edit_text);
        sendOtpButton = findViewById(R.id.send_otp_button);
        verifyOtpButton = findViewById(R.id.verify_otp_button);
        resendOtpButton = findViewById(R.id.resend_otp_button);
        resendTimerText = findViewById(R.id.resend_timer_text);

        // Initially hide OTP related views
        otpEditText.setVisibility(View.GONE);
        verifyOtpButton.setVisibility(View.GONE);
        resendOtpButton.setVisibility(View.GONE);
        resendTimerText.setVisibility(View.GONE);
    }

    private void setupListeners() {
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendOtpButton.setEnabled(s.length() >= 10);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        sendOtpButton.setOnClickListener(v -> sendOtp());
        verifyOtpButton.setOnClickListener(v -> verifyOtp());
        resendOtpButton.setOnClickListener(v -> resendOtp());

        // Add click listener for sign up prompt
        TextView signupPrompt = findViewById(R.id.signup_prompt);
        signupPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

    }

    private void sendOtp() {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        if (phoneNumber.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate OTP sending
        Toast.makeText(this, "OTP sent to " + phoneNumber, Toast.LENGTH_SHORT).show();

        // Show OTP input
        isOtpSent = true;
        otpEditText.setVisibility(View.VISIBLE);
        verifyOtpButton.setVisibility(View.VISIBLE);
        resendOtpButton.setVisibility(View.VISIBLE);
        resendTimerText.setVisibility(View.VISIBLE);

        // Disable phone number input
        phoneNumberEditText.setEnabled(false);
        sendOtpButton.setEnabled(false);

        // Start resend timer
        startResendTimer();
    }

    private void verifyOtp() {
        String otp = otpEditText.getText().toString().trim();
        if (otp.length() != 6) {
            Toast.makeText(this, "Please enter 6-digit OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate OTP verification
        if (otp.equals("123456")) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ChatsActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendOtp() {
        Toast.makeText(this, "OTP resent", Toast.LENGTH_SHORT).show();
        startResendTimer();
    }

    private void startResendTimer() {
        resendTimer = 30;
        resendOtpButton.setEnabled(false);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (resendTimer > 0) {
                    resendTimerText.setText("Resend OTP in " + resendTimer + "s");
                    resendTimer--;
                    new android.os.Handler().postDelayed(this, 1000);
                } else {
                    resendTimerText.setText("");
                    resendOtpButton.setEnabled(true);
                }
            }
        }, 1000);
    }

}
