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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shieldsoft.lucc_community.adapters.AdapterEvents;
import com.shieldsoft.lucc_community.adapters.AdapterPost;
import com.shieldsoft.lucc_community.adapters.AdapterUsers;
import com.shieldsoft.lucc_community.models.ModelEvents;
import com.shieldsoft.lucc_community.models.ModelPost;
import com.shieldsoft.lucc_community.models.ModelUsers;
import com.squareup.picasso.Picasso;

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
    String  authorName ="";
    String imageUrl="";

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


        getAllPost();



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

                FirebaseAuth mAuth= FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();

                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");

                Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //check until required data get
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            authorName = ""+ds.child("name").getValue();
                            imageUrl = ""+ds.child("image").getValue();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




                AlertDialog alertDialog = builder.create();

                post_cancel.setOnClickListener(view1->{

                    alertDialog.dismiss();

                });

                post_now.setOnClickListener(v1-> {

                    String woym_t = woym.getText().toString();




                    if(!woym_t.equals("")){



                        HashMap<Object ,String> hashMap = new HashMap<>();

                        String authorID=user.getUid();
                        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        String currentTime = new SimpleDateFormat("HH:mm a", Locale.getDefault()).format(new Date());



                        hashMap.put("woym",woym_t);
                        hashMap.put("authorName",authorName);
                        hashMap.put("authorID",authorID);
                        hashMap.put("postTime",currentTime+" "+currentDate);
                        hashMap.put("imageUrl",imageUrl);

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_registered_users,menu);



        SearchView searchView=(SearchView) menu.findItem(R.id.user_search).getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if(!TextUtils.isEmpty(query.trim())){

                    searchPost(query);
                }else
                {
                    getAllPost();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                if(!TextUtils.isEmpty(newText.trim())){

                    searchPost(newText);
                }else
                {
                    getAllPost();
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void getAllPost() {
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
    }

    private void searchPost(String query) {


        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelPost modelPost=ds.getValue(ModelPost.class);


                        if((modelPost.getAuthorName().toLowerCase().contains((query.toLowerCase())))
                                || modelPost.getWoym().toLowerCase().contains((query.toLowerCase()))
                                || modelPost.getPostTime().toLowerCase().contains((query.toLowerCase()))){
                            list.add(modelPost);
                        }




                    adapterPost=new AdapterPost(getApplicationContext(),list);

                    adapterPost.notifyDataSetChanged();

                    recyclerView.setAdapter(adapterPost);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}