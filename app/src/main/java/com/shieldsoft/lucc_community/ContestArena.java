package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContestArena extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    public static final String NAME_KEY = "com.example.listview.name.key";
    ListView listView;
    Button button;
    String[] arr = {"AllContest","Contest Within 24 Hours","CodeForces", "CodeChef", "TopCoder", "AtCoder", "CSAcademy", "HackerRank", "HackerEarth", "LeetCode",};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_arena);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Contest Arena");

        bottomNavigationView=findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_contest);

        listView = findViewById(R.id.listView);
        Intent intent = new Intent(this, ContestActivity.class);
        MyAdapter myAdapter = new MyAdapter(this, R.layout.my_layout, arr);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, arr[position], Toast.LENGTH_SHORT).show();
                intent.putExtra(NAME_KEY,arr[position]);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_dashboard:
                        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_contest:
                        return true;
                    case R.id.nav_toolkit:
                        startActivity(new Intent(getApplicationContext(), ToolKit.class));
                        overridePendingTransition(0, 0);
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