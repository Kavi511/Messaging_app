package com.example.messaging_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextInputEditText postContentInput;
    private CardView imagePreviewCard;
    private ImageView imagePreview;
    private ImageButton removeImageButton;
    private Button postButton;
    private TextView usernameText;
    private TextView userAvatar;

    private String selectedImagePath;
    private String userEmail;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Get user info from intent
        userEmail = getIntent().getStringExtra("user_email");
        username = getIntent().getStringExtra("username");
        if (username == null) {
            username = "User"; // Default username
        }

        setupToolbar();
        initializeViews();
        setupClickListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Post");
        }
    }

    private void initializeViews() {
        postContentInput = findViewById(R.id.post_content_input);
        imagePreviewCard = findViewById(R.id.image_preview_card);
        imagePreview = findViewById(R.id.image_preview);
        removeImageButton = findViewById(R.id.remove_image_button);
        postButton = findViewById(R.id.post_button);
        usernameText = findViewById(R.id.username_text);
        userAvatar = findViewById(R.id.user_avatar);

        // Set user info
        usernameText.setText(username);
        userAvatar.setText(username.substring(0, 1).toUpperCase());
    }

    private void setupClickListeners() {
        // Add image button
        findViewById(R.id.add_image_button).setOnClickListener(v -> openImagePicker());

        // Remove image button
        removeImageButton.setOnClickListener(v -> removeSelectedImage());

        // Post button
        postButton.setOnClickListener(v -> createPost());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                selectedImagePath = imageUri.toString();
                displaySelectedImage(imageUri);
            }
        }
    }

    private void displaySelectedImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            imagePreview.setImageBitmap(bitmap);
            imagePreviewCard.setVisibility(android.view.View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeSelectedImage() {
        selectedImagePath = null;
        imagePreviewCard.setVisibility(android.view.View.GONE);
        imagePreview.setImageBitmap(null);
    }

    private void createPost() {
        String content = postContentInput.getText().toString().trim();

        if (TextUtils.isEmpty(content) && selectedImagePath == null) {
            Toast.makeText(this, "Please add some content or an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create post object
        String postId = generatePostId();
        String timestamp = getCurrentTimestamp();
        String userInitial = username.substring(0, 1).toUpperCase();

        Post newPost = new Post(
                postId,
                username,
                userInitial,
                content,
                selectedImagePath,
                timestamp,
                0, // likes count
                false, // is liked
                userEmail);

        // Return result to StatusActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_post", newPost);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private String generatePostId() {
        return "post_" + System.currentTimeMillis();
    }

    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
