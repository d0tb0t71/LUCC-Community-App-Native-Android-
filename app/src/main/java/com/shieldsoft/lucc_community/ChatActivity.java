package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chat Box");


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_chatbox);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_chatbox:
                        return true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_contest:
                        startActivity(new Intent(getApplicationContext(), ContestArena.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_toolkit:
                        startActivity(new Intent(getApplicationContext(), ToolKit.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_dashboard:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                }

                return false;
            }
        });

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}