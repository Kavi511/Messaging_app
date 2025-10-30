package com.example.messaging_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity implements ChatsAdapter.OnChatClickListener {

    private RecyclerView chatsRecyclerView;
    private ChatsAdapter chatsAdapter;
    private List<Chat> chatList;

    // Bottom navigation
    private LinearLayout chatsTab, statusTab, callsTab;
    private TextView chatsText, statusText, callsText;
    private ImageView chatsIcon, statusIcon, callsIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        setupToolbar();
        initializeViews();
        setupBottomNavigation();
        setupChats();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Social Chat");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void initializeViews() {
        chatsRecyclerView = findViewById(R.id.chats_recycler_view);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Header buttons
        ImageView searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> showSearch());

        ImageView profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(v -> openProfile());

        ImageView moreButton = findViewById(R.id.more_button);
        moreButton.setOnClickListener(v -> showMoreOptions());
    }

    private void setupBottomNavigation() {
        // Get bottom navigation elements
        chatsTab = findViewById(R.id.chats_tab);
        statusTab = findViewById(R.id.status_tab);
        callsTab = findViewById(R.id.calls_tab);

        // Set click listeners
        chatsTab.setOnClickListener(v -> switchToTab("chats"));
        statusTab.setOnClickListener(v -> switchToTab("status"));
        callsTab.setOnClickListener(v -> switchToTab("calls"));

        // Set initial state (chats tab active)
        switchToTab("chats");
    }

    private void switchToTab(String tab) {
        // Reset all tabs
        resetTabStyles();

        // Get user email once for all cases
        String userEmail = getIntent().getStringExtra("user_email");

        switch (tab) {
            case "chats":
                setActiveTab(chatsTab, true);
                break;
            case "status":
                setActiveTab(statusTab, true);
                // Navigate to Status Activity
                Intent statusIntent = new Intent(this, StatusActivity.class);
                if (userEmail != null) {
                    statusIntent.putExtra("user_email", userEmail);
                }
                startActivity(statusIntent);
                break;
            case "calls":
                setActiveTab(callsTab, true);
                // Navigate to Calls Activity
                Intent callsIntent = new Intent(this, CallsActivity.class);
                if (userEmail != null) {
                    callsIntent.putExtra("user_email", userEmail);
                }
                startActivity(callsIntent);
                break;
        }
    }

    private void resetTabStyles() {
        setActiveTab(chatsTab, false);
        setActiveTab(statusTab, false);
        setActiveTab(callsTab, false);
    }

    private void setActiveTab(LinearLayout tab, boolean isActive) {
        ImageView icon = (ImageView) tab.getChildAt(0);
        TextView text = (TextView) tab.getChildAt(1);

        if (isActive) {
            icon.setColorFilter(getResources().getColor(R.color.primary_dark));
            text.setTextColor(getResources().getColor(R.color.primary_dark));
        } else {
            icon.setColorFilter(getResources().getColor(R.color.muted_foreground_dark));
            text.setTextColor(getResources().getColor(R.color.muted_foreground_dark));
        }
    }

    private void setupChats() {
        chatList = new ArrayList<>();

        // Add sample chats matching the React interface
        chatList.add(new Chat("Alice Johnson", "Hey! How are you doing?", "2:45 PM", 2, "A"));
        chatList.add(new Chat("Bob Smith", "Can we meet tomorrow?", "1:30 PM", 0, "B"));
        chatList.add(new Chat("Carol White", "Thanks for your help!", "12:15 PM", 1, "C"));
        chatList.add(new Chat("David Brown", "See you soon 👋", "11:20 AM", 0, "D"));
        chatList.add(new Chat("Emma Davis", "That sounds great!", "Yesterday", 0, "E"));
        chatList.add(new Chat("Frank Wilson", "Let me check and get back", "Yesterday", 3, "F"));
        chatList.add(new Chat("Grace Lee", "Perfect timing!", "Monday", 0, "G"));
        chatList.add(new Chat("Henry Taylor", "On my way", "Sunday", 0, "H"));

        chatsAdapter = new ChatsAdapter(chatList, this);
        chatsRecyclerView.setAdapter(chatsAdapter);
    }

    @Override
    public void onChatClick(Chat chat) {
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra("contact_name", chat.getName());
        startActivity(intent);
    }

    private void showSearch() {
        // TODO: Implement search functionality
    }

    private void openProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        // Pass current user email if available
        String userEmail = getIntent().getStringExtra("user_email");
        if (userEmail != null) {
            intent.putExtra("user_email", userEmail);
        }
        startActivity(intent);
    }

    private void showMoreOptions() {
        // TODO: Show more options menu
    }
}
