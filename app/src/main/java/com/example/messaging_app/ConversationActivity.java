package com.example.messaging_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messaging_app.database.MessagingRepository;
import com.example.messaging_app.database.Message;
import com.example.messaging_app.database.Chat;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity
        implements AttachmentOptionsDialog.OnAttachmentOptionSelectedListener,
        MessagesAdapter.OnMessageActionListener {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private static final int REQUEST_LOCATION_PERMISSION = 102;
    private static final int REQUEST_CAMERA = 200;
    private static final int REQUEST_CAMERA_X = 204;
    private static final int REQUEST_GALLERY = 201;
    private static final int REQUEST_DOCUMENT = 202;
    private static final int REQUEST_LOCATION = 203;

    // Camera photo storage
    private Uri currentPhotoUri;
    private String currentPhotoPath;

    private RecyclerView messagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private EditText messageEditText;
    private ImageView sendButton;
    private ImageView attachButton;
    private ImageView emojiButton;
    private com.google.android.material.floatingactionbutton.FloatingActionButton deleteSelectedFab;
    private List<Message> messageList;
    private String contactName;
    private String contactEmail;
    private long chatId;
    private String currentUserEmail;
    private boolean isSelectionMode = false;
    private List<com.example.messaging_app.database.Message> selectedMessages = new ArrayList<>();
    private MessagingRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        // Get data from intent
        contactName = getIntent().getStringExtra("contact_name");
        contactEmail = getIntent().getStringExtra("contact_email");
        currentUserEmail = getIntent().getStringExtra("user_email");

        if (contactName == null) {
            contactName = "Unknown";
        }
        if (contactEmail == null) {
            contactEmail = "unknown@example.com";
        }
        if (currentUserEmail == null) {
            currentUserEmail = "demo@example.com";
        }

        repository = new MessagingRepository(this);
        setupToolbar();
        initializeViews();
        setupChat();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(contactName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeViews() {
        messagesRecyclerView = findViewById(R.id.messages_recycler_view);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        attachButton = findViewById(R.id.attach_button);
        emojiButton = findViewById(R.id.emoji_button);
        deleteSelectedFab = findViewById(R.id.delete_selected_fab);

        sendButton.setOnClickListener(v -> sendMessage());
        attachButton.setOnClickListener(v -> showAttachmentOptions());
        emojiButton.setOnClickListener(v -> showEmojiPicker());
        deleteSelectedFab.setOnClickListener(v -> deleteSelectedMessages());
    }

    private void setupChat() {
        // First, try to find existing chat
        repository.getChatByParticipant(contactEmail, new MessagingRepository.DatabaseCallback<Chat>() {
            @Override
            public void onSuccess(Chat result) {
                if (result != null) {
                    // Chat exists, load messages
                    chatId = result.getId();
                    loadMessages();
                } else {
                    // Create new chat
                    createNewChat();
                }
            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(() -> {
                    Toast.makeText(ConversationActivity.this, "Error loading chat: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void createNewChat() {
        String avatarLetter = contactName.length() > 0 ? contactName.substring(0, 1).toUpperCase() : "?";
        Chat newChat = new Chat(contactEmail, contactName, "", 0L, 0, avatarLetter);

        repository.insertChat(newChat, new MessagingRepository.DatabaseCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                chatId = result;
                loadMessages();
            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(() -> {
                    Toast.makeText(ConversationActivity.this, "Error creating chat: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void loadMessages() {
        repository.getMessagesByChatId(chatId, new MessagingRepository.DatabaseCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> result) {
                runOnUiThread(() -> {
                    messageList = result != null ? result : new ArrayList<>();
                    messagesAdapter = new MessagesAdapter(messageList, ConversationActivity.this);
                    messagesRecyclerView.setAdapter(messagesAdapter);

                    if (!messageList.isEmpty()) {
                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(() -> {
                    Toast.makeText(ConversationActivity.this, "Error loading messages: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    messageList = new ArrayList<>();
                    messagesAdapter = new MessagesAdapter(messageList, ConversationActivity.this);
                    messagesRecyclerView.setAdapter(messagesAdapter);
                });
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Create new message
            Message newMessage = new Message(chatId, messageText, null, true, currentUserEmail, contactEmail);

            repository.insertMessage(newMessage, new MessagingRepository.DatabaseCallback<Long>() {
                @Override
                public void onSuccess(Long result) {
                    runOnUiThread(() -> {
                        // Update chat's last message
                        repository.updateLastMessage(chatId, messageText, System.currentTimeMillis());

                        // Add to local list and update UI
                        newMessage.setId(result);
                        messageList.add(newMessage);
                        messagesAdapter.notifyItemInserted(messageList.size() - 1);
                        messagesRecyclerView.scrollToPosition(messageList.size() - 1);

                        // Clear input
                        messageEditText.setText("");
                    });
                }

                @Override
                public void onError(Exception error) {
                    runOnUiThread(() -> {
                        Toast.makeText(ConversationActivity.this, "Error sending message: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    private void showAttachmentOptions() {
        // Show attachment options dialog
        AttachmentOptionsDialog dialog = new AttachmentOptionsDialog();
        dialog.show(getSupportFragmentManager(), "AttachmentOptionsDialog");
    }

    @Override
    public void onCamera() {
        if (checkCameraPermission()) {
            openCameraX();
        } else {
            requestCameraPermission();
        }
    }

    @Override
    public void onGallery() {
        if (checkStoragePermission()) {
            openGallery();
        } else {
            requestStoragePermission();
        }
    }

    @Override
    public void onDocument() {
        if (checkStoragePermission()) {
            openDocument();
        } else {
            requestStoragePermission();
        }
    }

    @Override
    public void onLocation() {
        if (checkLocationPermission()) {
            openLocation();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION);
    }

    private void openCameraX() {
        try {
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivityForResult(cameraIntent, REQUEST_CAMERA_X);
        } catch (Exception e) {
            Toast.makeText(this, "Error opening camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
        try {
            // Create camera intent
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Create a file to save the image
            Uri photoUri = getCameraImageUri();
            if (photoUri != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            // Check if camera app is available
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            } else {
                // Try alternative camera access methods
                tryAlternativeCameraAccess();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error opening camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void tryAlternativeCameraAccess() {
        try {
            // Try different camera intents
            Intent[] cameraIntents = {
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                    new Intent("android.media.action.IMAGE_CAPTURE"),
                    new Intent(Intent.ACTION_CAMERA_BUTTON)
            };

            boolean foundCamera = false;
            for (Intent intent : cameraIntents) {
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Add URI permissions if we have a photo URI
                    Uri photoUri = getCameraImageUri();
                    if (photoUri != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    startActivityForResult(intent, REQUEST_CAMERA);
                    foundCamera = true;
                    break;
                }
            }

            if (!foundCamera) {
                // Try to find any camera app by package name
                if (tryFindAnyCameraApp()) {
                    return;
                }

                // If no camera app found, show options
                showCameraOptionsDialog();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Camera access error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean tryFindAnyCameraApp() {
        try {
            // Common camera app package names
            String[] cameraPackages = {
                    "com.android.camera",
                    "com.android.camera2",
                    "com.google.android.GoogleCamera",
                    "com.samsung.camera",
                    "com.huawei.camera",
                    "com.oneplus.camera",
                    "com.miui.camera",
                    "com.oppo.camera",
                    "com.vivo.camera",
                    "com.android.gallery3d",
                    "com.google.android.apps.photos"
            };

            for (String packageName : cameraPackages) {
                try {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                    if (intent != null) {
                        // Try to launch with camera action
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri photoUri = getCameraImageUri();
                        if (photoUri != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        }
                        startActivityForResult(intent, REQUEST_CAMERA);
                        return true;
                    }
                } catch (Exception e) {
                    // Continue to next package
                }
            }
        } catch (Exception e) {
            // Handle error
        }
        return false;
    }

    private void showCameraOptionsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Camera Not Available");
        builder.setMessage("No camera app found on this device. Would you like to open the gallery instead?");
        builder.setPositiveButton("Open Gallery", (dialog, which) -> {
            if (checkStoragePermission()) {
                openGallery();
            } else {
                requestStoragePermission();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private Uri getCameraImageUri() {
        try {
            // Create a file in the app's cache directory
            java.io.File cacheDir = getCacheDir();
            java.io.File imageFile = new java.io.File(cacheDir, "camera_photo_" + System.currentTimeMillis() + ".jpg");

            // Store the path for later use
            currentPhotoPath = imageFile.getAbsolutePath();

            // Use FileProvider to get a content URI
            currentPhotoUri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    imageFile);
            return currentPhotoUri;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean checkStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ (API 33+), use new media permissions
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        } else {
            // For older versions, use READ_EXTERNAL_STORAGE
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+ (API 33+), request new media permissions
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO },
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // For older versions, request READ_EXTERNAL_STORAGE
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_LOCATION_PERMISSION);
    }

    private void openGallery() {
        // Show gallery options dialog for images or videos
        showGalleryOptionsDialog();
    }

    private void showGalleryOptionsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Gallery Options");
        builder.setItems(new String[] { "Select Images", "Select Videos", "Select Images & Videos" },
                (dialog, which) -> {
                    switch (which) {
                        case 0: // Select Images
                            openGalleryForImages();
                            break;
                        case 1: // Select Videos
                            openGalleryForVideos();
                            break;
                        case 2: // Select Images & Videos
                            openGalleryForBoth();
                            break;
                    }
                });
        builder.show();
    }

    private void openGalleryForImages() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    private void openGalleryForVideos() {
        Intent videoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        videoIntent.setType("video/*");
        startActivityForResult(videoIntent, REQUEST_GALLERY);
    }

    private void openGalleryForBoth() {
        Intent bothIntent = new Intent(Intent.ACTION_GET_CONTENT);
        bothIntent.setType("*/*");
        bothIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] { "image/*", "video/*" });
        startActivityForResult(bothIntent, REQUEST_GALLERY);
    }

    private void openDocument() {
        Intent documentIntent = new Intent(Intent.ACTION_GET_CONTENT);
        documentIntent.setType("*/*");
        documentIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(documentIntent, REQUEST_DOCUMENT);
    }

    private void openLocation() {
        Intent locationIntent = new Intent(Intent.ACTION_VIEW);
        locationIntent.setData(Uri.parse("geo:0,0?q="));
        if (locationIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(locationIntent, REQUEST_LOCATION);
        } else {
            Toast.makeText(this, "Location services not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                    openCamera();
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Gallery access granted! You can now select images and videos.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this,
                            "Gallery permission is required to select images and videos. Please enable it in Settings.",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openLocation();
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_X:
                    // Handle CameraX result
                    if (data != null && data.hasExtra("photo_uri")) {
                        String photoUriString = data.getStringExtra("photo_uri");
                        if (photoUriString != null) {
                            Uri photoUri = Uri.parse(photoUriString);
                            sendMediaMessage(photoUri, "Camera Photo");
                        }
                    }
                    break;
                case REQUEST_CAMERA:
                    // Handle camera result - check if we have a saved file
                    if (currentPhotoPath != null) {
                        java.io.File photoFile = new java.io.File(currentPhotoPath);
                        if (photoFile.exists()) {
                            // Use FileProvider to get the URI
                            Uri photoUri = androidx.core.content.FileProvider.getUriForFile(
                                    this, getPackageName() + ".fileprovider", photoFile);
                            sendMediaMessage(photoUri, "Camera Photo");
                        } else if (data != null && data.getExtras() != null) {
                            // Fallback to bitmap from extras
                            android.graphics.Bitmap photo = (android.graphics.Bitmap) data.getExtras().get("data");
                            if (photo != null) {
                                Uri photoUri = saveBitmapToFile(photo);
                                if (photoUri != null) {
                                    sendMediaMessage(photoUri, "Camera Photo");
                                }
                            }
                        }
                    } else if (data != null && data.getExtras() != null) {
                        // Fallback to bitmap from extras
                        android.graphics.Bitmap photo = (android.graphics.Bitmap) data.getExtras().get("data");
                        if (photo != null) {
                            Uri photoUri = saveBitmapToFile(photo);
                            if (photoUri != null) {
                                sendMediaMessage(photoUri, "Camera Photo");
                            }
                        }
                    }
                    break;
                case REQUEST_GALLERY:
                    if (data != null && data.getData() != null) {
                        Uri selectedMedia = data.getData();
                        String mediaType = getContentResolver().getType(selectedMedia);
                        if (mediaType != null) {
                            if (mediaType.startsWith("image/")) {
                                Toast.makeText(this, "Image selected from gallery", Toast.LENGTH_SHORT).show();
                                sendMediaMessage(selectedMedia, "Gallery Image");
                            } else if (mediaType.startsWith("video/")) {
                                Toast.makeText(this, "Video selected from gallery", Toast.LENGTH_SHORT).show();
                                sendMediaMessage(selectedMedia, "Gallery Video");
                            } else {
                                Toast.makeText(this, "Media selected from gallery", Toast.LENGTH_SHORT).show();
                                sendMediaMessage(selectedMedia, "Gallery Media");
                            }
                        }
                    }
                    break;
                case REQUEST_DOCUMENT:
                    if (data != null && data.getData() != null) {
                        Uri selectedDocument = data.getData();
                        Toast.makeText(this, "Document selected", Toast.LENGTH_SHORT).show();
                        sendMediaMessage(selectedDocument, "Document");
                    }
                    break;
                case REQUEST_LOCATION:
                    Toast.makeText(this, "Location shared", Toast.LENGTH_SHORT).show();
                    // Handle location result
                    break;
            }
        }
    }

    private void sendMediaMessage(Uri mediaUri, String mediaType) {
        // Create a message with media attachment
        String messageText = "📎 " + mediaType;

        // Create new message with media
        Message newMessage = new Message(chatId, messageText, mediaUri.toString(), true, currentUserEmail,
                contactEmail);

        repository.insertMessage(newMessage, new MessagingRepository.DatabaseCallback<Long>() {
            @Override
            public void onSuccess(Long result) {
                runOnUiThread(() -> {
                    // Update chat's last message
                    repository.updateLastMessage(chatId, messageText, System.currentTimeMillis());

                    // Add to local list and update UI
                    newMessage.setId(result);
                    messageList.add(newMessage);
                    messagesAdapter.notifyItemInserted(messageList.size() - 1);
                    messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                });
            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(() -> {
                    Toast.makeText(ConversationActivity.this, "Error sending media: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void showEmojiPicker() {
        // TODO: Implement emoji picker
        Toast.makeText(this, "Emoji picker coming soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_search) {
            showSearchDialog();
            return true;
        } else if (id == R.id.action_select_messages) {
            toggleSelectionMode();
            return true;
        } else if (id == R.id.action_video_call) {
            startVideoCall();
            return true;
        } else if (id == R.id.action_voice_call) {
            startCall();
            return true;
        } else if (id == R.id.action_more) {
            showUserInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSearchDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Search Messages");

        final EditText input = new EditText(this);
        input.setHint("Enter search text...");
        builder.setView(input);

        builder.setPositiveButton("Search", (dialog, which) -> {
            String searchText = input.getText().toString().trim();
            if (!searchText.isEmpty()) {
                searchMessages(searchText);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void searchMessages(String searchText) {
        repository.searchMessagesInChat(chatId, searchText,
                new MessagingRepository.DatabaseCallback<List<com.example.messaging_app.database.Message>>() {
                    @Override
                    public void onSuccess(List<com.example.messaging_app.database.Message> result) {
                        runOnUiThread(() -> {
                            if (result != null && !result.isEmpty()) {
                                showSearchResults(searchText, result);
                            } else {
                                Toast.makeText(ConversationActivity.this, "No messages found", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception error) {
                        runOnUiThread(() -> {
                            Toast.makeText(ConversationActivity.this, "Search error: " + error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }

    private void showSearchResults(String searchText, List<com.example.messaging_app.database.Message> results) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Search Results for: " + searchText);

        if (results.size() == 1) {
            // Show single result
            com.example.messaging_app.database.Message message = results.get(0);
            builder.setMessage(
                    "Found 1 message:\n\n" + message.getText() + "\n\nTime: " + message.getFormattedTimestamp());
        } else {
            // Show list of results
            StringBuilder messageList = new StringBuilder("Found " + results.size() + " messages:\n\n");
            for (int i = 0; i < Math.min(results.size(), 10); i++) { // Show max 10 results
                com.example.messaging_app.database.Message message = results.get(i);
                messageList.append((i + 1)).append(". ").append(message.getText())
                        .append(" (").append(message.getFormattedTimestamp()).append(")\n\n");
            }
            if (results.size() > 10) {
                messageList.append("... and ").append(results.size() - 10).append(" more messages");
            }
            builder.setMessage(messageList.toString());
        }

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void toggleSelectionMode() {
        isSelectionMode = !isSelectionMode;
        selectedMessages.clear();

        if (isSelectionMode) {
            Toast.makeText(this, "Selection mode enabled. Long press messages to select.", Toast.LENGTH_LONG).show();
            // Update adapter to show selection mode
            if (messagesAdapter != null) {
                messagesAdapter.setSelectionMode(true);
                messagesAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "Selection mode disabled", Toast.LENGTH_SHORT).show();
            // Update adapter to hide selection mode
            if (messagesAdapter != null) {
                messagesAdapter.setSelectionMode(false);
                messagesAdapter.notifyDataSetChanged();
            }
            hideDeleteSelectedButton();
        }
    }

    private void startCall() {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("contact_name", contactName);
        intent.putExtra("contact_email", contactEmail);
        intent.putExtra("user_email", currentUserEmail);
        startActivity(intent);
    }

    private void startVideoCall() {
        Intent intent = new Intent(this, VideoCallActivity.class);
        intent.putExtra("contact_name", contactName);
        intent.putExtra("contact_email", contactEmail);
        intent.putExtra("user_email", currentUserEmail);
        startActivity(intent);
    }

    private void showUserInfo() {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("contact_name", contactName);
        startActivity(intent);
    }

    // OnMessageActionListener implementation
    @Override
    public void onEditMessage(com.example.messaging_app.database.Message message) {
        showEditMessageDialog(message);
    }

    @Override
    public void onDeleteMessage(com.example.messaging_app.database.Message message) {
        showDeleteConfirmationDialog(message);
    }

    @Override
    public void onShareMessage(com.example.messaging_app.database.Message message) {
        shareMessage(message);
    }

    @Override
    public void onMessageSelected(com.example.messaging_app.database.Message message, boolean isSelected) {
        if (isSelected) {
            selectedMessages.add(message);
        } else {
            selectedMessages.remove(message);
        }

        // Show delete button if messages are selected
        if (!selectedMessages.isEmpty()) {
            showDeleteSelectedButton();
        } else {
            hideDeleteSelectedButton();
        }
    }

    private void showEditMessageDialog(com.example.messaging_app.database.Message message) {
        // Don't allow editing images
        if (message.hasImage()) {
            Toast.makeText(this, "Cannot edit image messages", Toast.LENGTH_SHORT).show();
            return;
        }

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Edit Message");

        // Create input field
        final EditText input = new EditText(this);
        input.setText(message.getText());
        input.setSelection(input.getText().length()); // Place cursor at end
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newText = input.getText().toString().trim();
            if (!newText.isEmpty() && !newText.equals(message.getText())) {
                updateMessage(message, newText);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showDeleteConfirmationDialog(com.example.messaging_app.database.Message message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Delete Message");
        builder.setMessage("Are you sure you want to delete this message?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteMessage(message);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateMessage(com.example.messaging_app.database.Message message, String newText) {
        message.setText(newText);
        message.setEdited(true); // Mark as edited

        repository.updateMessage(message, new MessagingRepository.DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    // Update the message in the list
                    int position = messageList.indexOf(message);
                    if (position != -1) {
                        messageList.set(position, message);
                        messagesAdapter.notifyItemChanged(position);
                    }
                    Toast.makeText(ConversationActivity.this, "Message updated", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(() -> {
                    Toast.makeText(ConversationActivity.this, "Failed to update message: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void deleteMessage(com.example.messaging_app.database.Message message) {
        // Soft delete - mark as deleted
        message.setDeleted(true);

        repository.updateMessage(message, new MessagingRepository.DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    // Remove from the list
                    int position = messageList.indexOf(message);
                    if (position != -1) {
                        messageList.remove(position);
                        messagesAdapter.notifyItemRemoved(position);
                    }
                    Toast.makeText(ConversationActivity.this, "Message deleted", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(Exception error) {
                runOnUiThread(() -> {
                    Toast.makeText(ConversationActivity.this, "Failed to delete message: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void shareMessage(com.example.messaging_app.database.Message message) {
        showShareOptionsDialog(message);
    }

    private void showShareOptionsDialog(com.example.messaging_app.database.Message message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Share Message");

        String[] shareOptions = {
                "📧 Gmail",
                "📱 Other Socials"
        };

        builder.setItems(shareOptions, (dialog, which) -> {
            switch (which) {
                case 0: // Gmail
                    shareViaGmail(message);
                    break;
                case 1: // Other Socials
                    shareViaYouTube(message);
                    break;
            }
        });

        builder.show();
    }

    private void shareViaGmail(com.example.messaging_app.database.Message message) {
        // Validate if message has content to share
        if (!message.hasImage() && (message.getText() == null || message.getText().trim().isEmpty())) {
            Toast.makeText(this, "No content to share", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create universal email intent (works with any email app)
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        // Create email content
        String subject = "Message from " + contactName;
        String body = createEmailBody(message);

        // Set up email intent
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        // Handle image attachment if present
        if (message.hasImage()) {
            Uri imageUri = getSecureImageUri(message.getImagePath());
            if (imageUri != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                emailIntent.setType("image/*");

                // Grant read permission to email apps
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this, "Unable to access image for sharing", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            emailIntent.setType("text/plain");
        }

        // Check if any email apps can handle this intent
        if (emailIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "No email app found on this device. Please install an email app.", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        try {
            // Show app chooser for email apps (including Gmail)
            Intent chooserIntent = Intent.createChooser(emailIntent, "Send email via");
            startActivity(chooserIntent);
            Log.d("EmailShare", "Successfully launched email chooser");
        } catch (Exception e) {
            Log.e("EmailShare", "Error opening email app: " + e.getMessage());
            Toast.makeText(this, "Error opening email app: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getSecureImageUri(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }

        try {
            Uri originalUri = Uri.parse(imagePath);

            // Handle different URI types
            if ("file".equals(originalUri.getScheme())) {
                // Camera-captured images (file:// URIs) - use FileProvider
                return getFileProviderUri(originalUri);
            } else if ("content".equals(originalUri.getScheme())) {
                // Gallery images (content:// URIs) - use directly
                return originalUri;
            } else {
                // Fallback: try to create a file URI and convert to FileProvider
                java.io.File imageFile = new java.io.File(imagePath);
                if (imageFile.exists()) {
                    return androidx.core.content.FileProvider.getUriForFile(
                            this,
                            getPackageName() + ".fileprovider",
                            imageFile);
                }
            }
        } catch (Exception e) {
            Log.e("EmailShare", "Error creating secure URI: " + e.getMessage());
        }

        return null;
    }

    private Uri getFileProviderUri(Uri fileUri) {
        try {
            java.io.File imageFile = new java.io.File(fileUri.getPath());
            if (imageFile.exists()) {
                return androidx.core.content.FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider",
                        imageFile);
            }
        } catch (Exception e) {
            Log.e("EmailShare", "Error creating FileProvider URI: " + e.getMessage());
        }
        return null;
    }

    private String createEmailBody(com.example.messaging_app.database.Message message) {
        StringBuilder body = new StringBuilder();

        body.append("Hello,\n\n");

        if (message.hasImage()) {
            body.append("I wanted to share an image with you from my messaging app.\n\n");
            if (!message.getText().isEmpty()) {
                body.append("Caption: ").append(message.getText()).append("\n\n");
            }
        } else {
            body.append("I wanted to share this message with you:\n\n");
            body.append("\"").append(message.getText()).append("\"\n\n");
        }

        body.append("Sent from: ").append(contactName).append("\n");
        body.append("Time: ").append(message.getFormattedTimestamp()).append("\n\n");
        body.append("---\n");
        body.append("Shared via Messaging App");

        return body.toString();
    }

    private void shareViaYouTube(com.example.messaging_app.database.Message message) {
        // Validate if message has content to share
        if (!message.hasImage() && (message.getText() == null || message.getText().trim().isEmpty())) {
            Toast.makeText(this, "No content to share", Toast.LENGTH_SHORT).show();
            return;
        }

        // Try to directly open YouTube app for uploading
        String[] youtubePackages = {
                "com.google.android.youtube", // Standard YouTube
                "com.google.android.apps.youtube.music", // YouTube Music
                "com.google.android.youtube.tv", // YouTube TV
                "com.google.android.youtube.kids" // YouTube Kids
        };

        Intent youtubeIntent = null;
        String workingPackage = null;

        // Debug: Log all available sharing apps
        Log.d("YouTubeDetection", "Checking for YouTube packages...");
        debugAvailableApps(); // Call debug method to see all available apps

        // Find which YouTube package is available
        for (String packageName : youtubePackages) {
            Log.d("YouTubeDetection", "Testing package: " + packageName);

            Intent testIntent = new Intent(Intent.ACTION_SEND);
            testIntent.setPackage(packageName);
            testIntent.setType("text/plain");

            if (testIntent.resolveActivity(getPackageManager()) != null) {
                youtubeIntent = testIntent;
                workingPackage = packageName;
                Log.d("YouTubeDetection", "Found working YouTube package: " + packageName);
                break;
            } else {
                Log.d("YouTubeDetection", "Package not available: " + packageName);
            }
        }

        // Additional check: Try without package specification
        if (youtubeIntent == null) {
            Log.d("YouTubeDetection", "No specific YouTube package found, trying universal approach...");

            // Try universal intent and check if YouTube is in the results
            Intent universalIntent = new Intent(Intent.ACTION_SEND);
            universalIntent.setType("text/plain");

            List<ResolveInfo> activities = getPackageManager().queryIntentActivities(universalIntent, 0);
            boolean youtubeFound = false;

            for (ResolveInfo activity : activities) {
                String packageName = activity.activityInfo.packageName;
                String appName = activity.loadLabel(getPackageManager()).toString();
                Log.d("YouTubeDetection", "Available app: " + appName + " (" + packageName + ")");

                if (packageName.contains("youtube") || appName.toLowerCase().contains("youtube")) {
                    youtubeFound = true;
                    Log.d("YouTubeDetection", "Found YouTube app: " + appName + " (" + packageName + ")");

                    // Create intent for this YouTube app
                    youtubeIntent = new Intent(Intent.ACTION_SEND);
                    youtubeIntent.setPackage(packageName);
                    youtubeIntent.setType("text/plain");
                    workingPackage = packageName;
                    break;
                }
            }

            if (!youtubeFound) {
                Log.d("YouTubeDetection", "No YouTube app found in available apps");
            }
        }

        if (youtubeIntent == null) {
            // Fallback to universal sharing if YouTube not found
            Log.d("YouTubeDetection", "YouTube app not found, falling back to universal sharing");
            Toast.makeText(this, "YouTube app not found. Opening sharing options instead.", Toast.LENGTH_LONG).show();
            shareViaUniversalSharing(message);
            return;
        }

        // Create YouTube upload content
        String shareText = createYouTubeShareText(message);

        if (message.hasImage()) {
            // Share image to YouTube for upload (YouTube Shorts or Community posts)
            Uri imageUri = getSecureImageUri(message.getImagePath());
            if (imageUri != null) {
                youtubeIntent.setType("image/*");
                youtubeIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                youtubeIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                youtubeIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this, "Unable to access image for sharing", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // Share text content to YouTube
            youtubeIntent.setType("text/plain");
            youtubeIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }

        try {
            // Directly open YouTube app for uploading
            startActivity(youtubeIntent);
            Log.d("YouTubeUpload", "Successfully opened YouTube app for upload with package: " + workingPackage);
        } catch (Exception e) {
            Log.e("YouTubeUpload", "Error opening YouTube app: " + e.getMessage());
            // Fallback to universal sharing
            Toast.makeText(this, "Error opening YouTube. Opening sharing options instead.", Toast.LENGTH_SHORT).show();
            shareViaUniversalSharing(message);
        }
    }

    private void shareViaUniversalSharing(com.example.messaging_app.database.Message message) {
        // Fallback method for universal sharing when YouTube is not available
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        String shareText = createYouTubeShareText(message);

        if (message.hasImage()) {
            Uri imageUri = getSecureImageUri(message.getImagePath());
            if (imageUri != null) {
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            }
        } else {
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }

        if (shareIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "No sharing app found on this device.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Intent chooserIntent = Intent.createChooser(shareIntent, "Share via");
            startActivity(chooserIntent);
            Log.d("UniversalShare", "Successfully launched universal sharing chooser");
        } catch (Exception e) {
            Log.e("UniversalShare", "Error opening sharing app: " + e.getMessage());
            Toast.makeText(this, "Error opening sharing app: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String createYouTubeShareText(com.example.messaging_app.database.Message message) {
        StringBuilder shareText = new StringBuilder();

        shareText.append("Check this out! 📱\n\n");

        if (message.hasImage()) {
            shareText.append("📸 Image shared from ").append(contactName).append("\n\n");
            if (!message.getText().isEmpty()) {
                shareText.append("Caption: ").append(message.getText()).append("\n\n");
            }
        } else {
            shareText.append("Message from ").append(contactName).append(":\n\n");
            shareText.append("\"").append(message.getText()).append("\"\n\n");
        }

        shareText.append("Shared via Messaging App 📲");

        return shareText.toString();
    }

    private void debugAvailableApps() {
        // Method to debug available apps (can be called for troubleshooting)
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);
        Log.d("AvailableApps", "Apps that can handle ACTION_SEND:");

        for (ResolveInfo activity : activities) {
            String packageName = activity.activityInfo.packageName;
            String appName = activity.loadLabel(getPackageManager()).toString();
            Log.d("AvailableApps", "Package: " + packageName + " | App: " + appName);

            // Check specifically for YouTube
            if (packageName.contains("youtube") || appName.toLowerCase().contains("youtube")) {
                Log.d("AvailableApps", "*** FOUND YOUTUBE APP: " + appName + " (" + packageName + ") ***");
            }
        }
    }

    private void showDeleteSelectedButton() {
        if (deleteSelectedFab != null) {
            deleteSelectedFab.setVisibility(View.VISIBLE);
        }
        Toast.makeText(this, selectedMessages.size() + " messages selected", Toast.LENGTH_SHORT).show();
    }

    private void hideDeleteSelectedButton() {
        if (deleteSelectedFab != null) {
            deleteSelectedFab.setVisibility(View.GONE);
        }
    }

    private void deleteSelectedMessages() {
        if (selectedMessages.isEmpty()) {
            Toast.makeText(this, "No messages selected", Toast.LENGTH_SHORT).show();
            return;
        }

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Delete Selected Messages");
        builder.setMessage("Are you sure you want to delete " + selectedMessages.size() + " selected messages?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            for (com.example.messaging_app.database.Message message : selectedMessages) {
                deleteMessage(message);
            }
            selectedMessages.clear();
            isSelectionMode = false;
            if (messagesAdapter != null) {
                messagesAdapter.setSelectionMode(false);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private Uri saveBitmapToFile(android.graphics.Bitmap bitmap) {
        try {
            // Create a file in the app's cache directory
            java.io.File cacheDir = getCacheDir();
            java.io.File imageFile = new java.io.File(cacheDir, "camera_photo_" + System.currentTimeMillis() + ".jpg");

            // Compress bitmap to file
            java.io.FileOutputStream fos = new java.io.FileOutputStream(imageFile);
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            // Return URI for the file
            return Uri.fromFile(imageFile);
        } catch (Exception e) {
            Toast.makeText(this, "Error saving photo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
