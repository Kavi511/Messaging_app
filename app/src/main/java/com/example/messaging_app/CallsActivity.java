package com.example.messaging_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CallsActivity extends AppCompatActivity implements CallsAdapter.OnCallClickListener {

    private RecyclerView callsRecyclerView;
    private CallsAdapter callsAdapter;
    private List<Call> callList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls);

        setupToolbar();
        initializeViews();
        setupCallList();
        setupBottomNavigation();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Calls");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void initializeViews() {
        callsRecyclerView = findViewById(R.id.calls_recycler_view);
        callsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupCallList() {
        callList = new ArrayList<>();

        // Add sample call history matching the screenshot
        callList.add(
                new Call("William Anders", "W", "20 minutes ago", Call.CallType.VOICE, Call.CallDirection.OUTGOING));
        callList.add(new Call("Mom", "M", "45 minutes ago", Call.CallType.VOICE, Call.CallDirection.OUTGOING));
        callList.add(new Call("Hannah", "H", "55 minutes ago", Call.CallType.VOICE, Call.CallDirection.OUTGOING));
        callList.add(new Call("Dad", "D", "56 minutes ago", Call.CallType.VOICE, Call.CallDirection.MISSED));
        callList.add(new Call("Cayne Don", "C", "Today 04:30 pm", Call.CallType.VOICE, Call.CallDirection.MISSED));
        callList.add(new Call("Abby Gale", "A", "Today 03:30 pm", Call.CallType.VOICE, Call.CallDirection.OUTGOING));
        callList.add(
                new Call("Williams Anders", "W", "Today 12:15 pm", Call.CallType.VOICE, Call.CallDirection.OUTGOING));
        callList.add(
                new Call("Williams Anders", "W", "Today 12:10 pm", Call.CallType.VOICE, Call.CallDirection.OUTGOING));

        callsAdapter = new CallsAdapter(callList, this);
        callsRecyclerView.setAdapter(callsAdapter);
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
            Intent intent = new Intent(this, StatusActivity.class);
            String userEmail = getIntent().getStringExtra("user_email");
            if (userEmail != null) {
                intent.putExtra("user_email", userEmail);
            }
            startActivity(intent);
            finish();
        });

        callsTab.setOnClickListener(v -> {
            // Already in calls, do nothing
        });
    }

    @Override
    public void onCallClick(Call call) {
        Toast.makeText(this, "Calling " + call.getContactName(), Toast.LENGTH_SHORT).show();
    }
}
