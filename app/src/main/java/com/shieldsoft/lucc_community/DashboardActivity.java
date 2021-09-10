package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shieldsoft.lucc_community.adapters.AdapterEvents;
import com.shieldsoft.lucc_community.adapters.AdapterPost;
import com.shieldsoft.lucc_community.models.ModelEvents;
import com.shieldsoft.lucc_community.models.ModelPost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {




    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    Button post_cancel,post_now;
    EditText woym;
    RecyclerView recyclerView;
    ArrayList<ModelPost> list;
    AdapterPost adapterPost;
    DatabaseReference databaseReference;

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
        floatingActionButton=findViewById(R.id.fab_add_post);


        recyclerView=findViewById(R.id.post_recycler_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapterPost= new AdapterPost(this,list);
        recyclerView.setAdapter(adapterPost);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {

                    ModelPost modelPost = dataSnapshot.getValue(ModelPost.class);
                    list.add(modelPost);

                }

                adapterPost.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setCancelable(false);

                View view= LayoutInflater.from(DashboardActivity.this).inflate(R.layout.add_post_dialog,findViewById(R.id.post_dialog));
                builder.setView(view);
                post_cancel=view.findViewById(R.id.post_cancel);
                post_now=view.findViewById(R.id.post_now);
                woym=view.findViewById(R.id.woym);




                AlertDialog alertDialog = builder.create();

                post_cancel.setOnClickListener(view1->{

                    alertDialog.dismiss();

                });

                post_now.setOnClickListener(v1-> {

                    FirebaseAuth mAuth= FirebaseAuth.getInstance();
                    FirebaseUser user=mAuth.getCurrentUser();

                    String woym_t = woym.getText().toString();


                    if(!woym_t.equals("")){

                        HashMap<Object ,String> hashMap = new HashMap<>();

                        String authorID=user.getUid();
                        String authorName = user.getDisplayName();
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        String currentTime = new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date());



                        hashMap.put("woym",woym_t);
                        hashMap.put("authorName",authorName);
                        hashMap.put("authorID",authorID);
                        hashMap.put("postTime",currentTime+" "+currentDate);


                        //hashMap.put("eventBanner",pic_url);


                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference reference=database.getReference("Posts");

                        reference.push().setValue(hashMap);

                        Toast.makeText(getApplicationContext(), "Post Uploaded Successfully", Toast.LENGTH_SHORT).show();

                        alertDialog.dismiss();
                        recreate();

                    }
                    else {
                        if(woym_t.isEmpty())
                        {
                            woym.setError("Enter Text...");
                        }
                    }


                });


                alertDialog.show();
            }
        });

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