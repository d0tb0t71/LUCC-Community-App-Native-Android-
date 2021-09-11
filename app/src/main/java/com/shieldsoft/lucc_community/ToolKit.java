package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ToolKit extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton reg_users,dev_tips,cp_tips,upcoming_events,upcoming_meetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_kit);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Tool Menu");


        reg_users=findViewById(R.id.registered_users);
        dev_tips=findViewById(R.id.dev_tips);
        cp_tips=findViewById(R.id.cp_tips);
        upcoming_events=findViewById(R.id.upcoming_events);
        upcoming_meetings=findViewById(R.id.upcoming_meetings);


        upcoming_events.setOnClickListener(v-> {

            startActivity(new Intent(getApplicationContext(),UpcomingEvents.class));

        });


        reg_users.setOnClickListener(v-> {

            startActivity(new Intent(getApplicationContext(),RegisteredUsers.class));


        });

        dev_tips.setOnClickListener(v->
        {

            Intent intent=new Intent(getApplicationContext(),BrowseURL.class);
            intent.putExtra("url","https://hackr.io/blog/how-to-learn-programming");
            startActivity(intent);


        });

        cp_tips.setOnClickListener(v->
        {

            Intent intent=new Intent(getApplicationContext(),BrowseURL.class);
            intent.putExtra("url","https://blog.codingblocks.com/2019/start-with-competitive-programming/");
            startActivity(intent);

        });

        upcoming_meetings.setOnClickListener(v->
        {
            startActivity(new Intent(getApplicationContext(),UpcomingMeetings.class));

        });






        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_toolkit);





        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_dashboard:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
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
                        return true;
                    case R.id.nav_chatbox:
                        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
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