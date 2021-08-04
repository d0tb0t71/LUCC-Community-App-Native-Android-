package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shieldsoft.lucc_community.adapters.AdapterUsers;
import com.shieldsoft.lucc_community.models.ModelUsers;

import java.util.ArrayList;
import java.util.List;

public class RegisteredUsers extends AppCompatActivity {

    RecyclerView users_recyclerView;
    AdapterUsers adapterUsers;
    List<ModelUsers> usersList;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_users);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Registered Users");


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_users);



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
                    case R.id.nav_users:
                        return true;
                    case R.id.nav_chatbox:
                        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                }

                return false;
            }
        });













        users_recyclerView=findViewById(R.id.users_recyclerView);

        users_recyclerView.setHasFixedSize(true);
        users_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        //init user list
        
        usersList=new ArrayList<>();
        
        
        getAllUsers();




    }

    private void getAllUsers() {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList.clear();

                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelUsers modelUsers=ds.getValue(ModelUsers.class);

                    if(!modelUsers.getUid().equals((firebaseUser.getUid()))){

                        usersList.add(modelUsers);

                    }



                    adapterUsers=new AdapterUsers(getApplicationContext(),usersList);

                    users_recyclerView.setAdapter(adapterUsers);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_registered_users,menu);



        SearchView searchView=(SearchView) menu.findItem(R.id.user_search).getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if(!TextUtils.isEmpty(query.trim())){

                    searchUser(query);
                }else
                {
                    getAllUsers();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                if(!TextUtils.isEmpty(newText.trim())){

                    searchUser(newText);
                }else
                {
                    getAllUsers();
                }


                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void searchUser(String query) {


        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersList.clear();

                for(DataSnapshot ds:snapshot.getChildren()){

                    ModelUsers modelUsers=ds.getValue(ModelUsers.class);

                    if(!modelUsers.getUid().equals((firebaseUser.getUid()))){


                        if((modelUsers.getName().toLowerCase().contains((query.toLowerCase())))
                        || modelUsers.getEmail().toLowerCase().contains((query.toLowerCase()))
                        || modelUsers.getLu_id().toLowerCase().contains((query.toLowerCase()))){


                            usersList.add(modelUsers);
                        }

                    }



                    adapterUsers=new AdapterUsers(getApplicationContext(),usersList);

                    adapterUsers.notifyDataSetChanged();

                    users_recyclerView.setAdapter(adapterUsers);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}