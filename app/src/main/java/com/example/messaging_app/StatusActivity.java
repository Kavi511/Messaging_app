package com.example.messaging_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StatusActivity extends AppCompatActivity
        implements StatusStoriesAdapter.OnStatusStoryClickListener, PostsAdapter.OnPostClickListener {

    private static final int CREATE_POST_REQUEST = 1;

    private RecyclerView statusRecyclerView;
    private RecyclerView postsRecyclerView;
    private StatusStoriesAdapter statusAdapter;
    private PostsAdapter postsAdapter;
    private List<Status> statusList;
    private List<Post> postsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        setupToolbar();
        initializeViews();
        setupStatusList();
        setupPostsList();
        setupBottomNavigation();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Status & Posts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void initializeViews() {
        statusRecyclerView = findViewById(R.id.status_stories_recycler_view);
        statusRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        postsRecyclerView = findViewById(R.id.posts_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create Post Button
        findViewById(R.id.create_post_button).setOnClickListener(v -> openCreatePostActivity());
    }

    private void setupStatusList() {
        statusList = new ArrayList<>();

        // Add My Status
        statusList.add(new Status("My Status", "?", "Tap to add status update", true));

        // Add sample status stories (horizontal scrollable)
        statusList.add(new Status("Bob", "B", "2 hours ago", false));
        statusList.add(new Status("Dian Tangg...", "D", "4 hours ago", false));
        statusList.add(new Status("Hanan", "H", "6 hours ago", false));
        statusList.add(new Status("Madz Suwa...", "M", "8 hours ago", false));
        statusList.add(new Status("Mas Wahyu", "M", "1 day ago", false));

        statusAdapter = new StatusStoriesAdapter(statusList, this);
        statusRecyclerView.setAdapter(statusAdapter);
    }

    private void setupPostsList() {
        postsList = new ArrayList<>();

        // Add sample posts
        postsList.add(new Post(
                "post_1",
                "John Doe",
                "J",
                "Just finished an amazing hike! The view from the top was incredible. Nature never fails to amaze me. 🌄",
                null,
                "Dec 15, 2024 at 2:30 PM",
                12,
                false,
                "john@example.com"));

        postsList.add(new Post(
                "post_2",
                "Sarah Wilson",
                "S",
                "Working on a new project. Can't wait to share the results!",
                null,
                "Dec 15, 2024 at 1:15 PM",
                8,
                true,
                "sarah@example.com"));

        postsList.add(new Post(
                "post_3",
                "Mike Johnson",
                "M",
                "Coffee break time! ☕",
                null,
                "Dec 15, 2024 at 11:45 AM",
                5,
                false,
                "mike@example.com"));

        postsAdapter = new PostsAdapter(postsList, this);
        postsRecyclerView.setAdapter(postsAdapter);
    }

    private void openCreatePostActivity() {
        Intent intent = new Intent(this, CreatePostActivity.class);
        String userEmail = getIntent().getStringExtra("user_email");
        String username = getIntent().getStringExtra("username");

        if (userEmail != null) {
            intent.putExtra("user_email", userEmail);
        }
        if (username != null) {
            intent.putExtra("username", username);
        } else {
            intent.putExtra("username", "User"); // Default username
        }

        startActivityForResult(intent, CREATE_POST_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_POST_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Post newPost = (Post) data.getSerializableExtra("new_post");
            if (newPost != null) {
                // Add new post to the beginning of the list
                postsList.add(0, newPost);
                postsAdapter.notifyItemInserted(0);
                postsRecyclerView.smoothScrollToPosition(0);
                Toast.makeText(this, "Post created successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupBottomNavigation() {
        LinearLayout chatsTab = findViewById(R.id.chats_tab);
        LinearLayout statusTab = findViewById(R.id.status_tab);
        LinearLayout callsTab = findViewById(R.id.calls_tab);

        chatsTab.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatsActivity.class);
            String userEmail = getIntent().getStringExtra("user_email");
            if (userEmail != null) {
                intent.putExtra("user_email", userEmail);
            }
            startActivity(intent);
            finish();
        });

        statusTab.setOnClickListener(v -> {
            // Already in status, do nothing
        });

        callsTab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CallsActivity.class);
            String userEmail = getIntent().getStringExtra("user_email");
            if (userEmail != null) {
                intent.putExtra("user_email", userEmail);
            }
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onMyStatusClick() {
        Toast.makeText(this, "Add your status update", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusStoryClick(Status status) {
        Toast.makeText(this, "Viewing " + status.getUsername() + "'s status", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLikeClick(Post post, int position) {
        // Toggle like status
        post.setLiked(!post.isLiked());
        if (post.isLiked()) {
            post.setLikesCount(post.getLikesCount() + 1);
        } else {
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        }

        // Update the adapter
        postsAdapter.notifyItemChanged(position);

        // Show feedback
        String message = post.isLiked() ? "Liked!" : "Unliked";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentClick(Post post) {
        Toast.makeText(this, "Comments feature coming soon!", Toast.LENGTH_SHORT).show();
    }
}
