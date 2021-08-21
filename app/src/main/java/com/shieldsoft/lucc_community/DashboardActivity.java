package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity {




    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if(!isConnected(this)){
            showInternetDialog();
        }






        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dashboard");


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_dashboard:
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
                    case R.id.nav_chatbox:
                        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });






    }

    private void showInternetDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view= LayoutInflater.from(this).inflate(R.layout.no_internet_alert_dialog,findViewById(R.id.no_internet_layout));
        view.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected(DashboardActivity.this)){
                    showInternetDialog();
                }
                else{
                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                }
            }
        });
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    private boolean isConnected(DashboardActivity dashboardActivity) {

        ConnectivityManager connectivityManager= (ConnectivityManager) dashboardActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiCon != null && wifiCon.isConnected() )|| (mobileCon != null && mobileCon.isConnected());

    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}