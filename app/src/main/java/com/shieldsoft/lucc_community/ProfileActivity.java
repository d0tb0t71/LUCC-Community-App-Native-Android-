package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ImageView avatarIV;
    TextView nameTV, emailTV, phoneTV, lu_idTV, batchTV, sectionTV, blood_groupTV;

    FloatingActionButton floatingActionButton;

    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        progressDialogShow();


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        avatarIV = findViewById(R.id.avatarIV);
        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        phoneTV = findViewById(R.id.phoneTV);
        lu_idTV = findViewById(R.id.lu_idTV);
        batchTV = findViewById(R.id.batchTV);
        sectionTV = findViewById(R.id.sectionTV);
        blood_groupTV = findViewById(R.id.bloodGroupTV);


        floatingActionButton = findViewById(R.id.fab);


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //check until required data get
                for (DataSnapshot ds : snapshot.getChildren()) {


                    //get data

                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();
                    String blood_group = "" + ds.child("blood_group").getValue();
                    String section = "" + ds.child("section").getValue();
                    String batch = "" + ds.child("batch").getValue();
                    String lu_id = "" + ds.child("lu_id").getValue();


                    //set data
                    nameTV.setText(name);
                    emailTV.setText("Email : " + email);
                    phoneTV.setText("Phone : " + phone);
                    blood_groupTV.setText("Blood Group : " + blood_group);
                    batchTV.setText("Batch : " + batch);
                    sectionTV.setText("Section : " + section);
                    lu_idTV.setText("LU ID : " + lu_id);

                    try {
                        Picasso.get().load(image).into(avatarIV);

                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), "EXCEPTIONAL", Toast.LENGTH_SHORT).show();

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.nav_dashboard:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.nav_profile:
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


        floatingActionButton.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), EditProfile.class));


        });


    }

    private void progressDialogShow() {
        progress = new ProgressDialog(this);
        progress.setTitle("Data Loading");
        progress.setMessage("Please wait...");
        progress.show();
        progress.setCancelable(false);

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 2000);
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }



}