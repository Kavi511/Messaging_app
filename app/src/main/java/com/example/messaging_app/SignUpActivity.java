package com.example.messaging_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.messaging_app.database.MessagingRepository;
import com.example.messaging_app.database.User;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private TextView loginPrompt;

    private MessagingRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        repository = new MessagingRepository(this);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        fullNameEditText = findViewById(R.id.full_name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        registerButton = findViewById(R.id.register_button);
        loginPrompt = findViewById(R.id.login_prompt);
    }

    private void setupListeners() {
        registerButton.setOnClickListener(v -> registerUser());
        loginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Real-time validation
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail();
            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword();
            }
        });
    }

    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validation
        if (!validateInputs(fullName, email, phoneNumber, password, confirmPassword)) {
            return;
        }

        // Check if user already exists
        repository.getUserByEmail(email, new MessagingRepository.DatabaseCallback<User>() {
            @Override
            public void onSuccess(User result) {
                if (result != null) {
                    runOnUiThread(() -> {
                        Toast.makeText(SignUpActivity.this, "User with this email already exists", Toast.LENGTH_SHORT)
                                .show();
                    });
                } else {
                    // Create new user
                    User newUser = new User(email, fullName, phoneNumber, null);
                    repository.insertUser(newUser, new MessagingRepository.DatabaseCallback<Long>() {
                        @Override
                        public void onSuccess(Long result) {
                            runOnUiThread(() -> {
                                Toast.makeText(SignUpActivity.this, "Account created successfully!", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent = new Intent(SignUpActivity.this, ChatsActivity.class);
                                intent.putExtra("user_email", email);
                                startActivity(intent);
                                finish();
                            });
                        }

                        @Override
                        public void onError(Exception error) {
                            runOnUiThread(() -> {
                                Toast.makeText(SignUpActivity.this, "Failed to create account: " + error.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(() -> {
                    Toast.makeText(SignUpActivity.this, "Error checking user: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private boolean validateInputs(String fullName, String email, String phoneNumber, String password,
            String confirmPassword) {
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void validateEmail() {
        String email = emailEditText.getText().toString().trim();
        if (!email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
        } else {
            emailEditText.setError(null);
        }
    }

    private void validatePassword() {
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!confirmPassword.isEmpty() && !password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
        } else {
            confirmPasswordEditText.setError(null);
        }
    }

}
