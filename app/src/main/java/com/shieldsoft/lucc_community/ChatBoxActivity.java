package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shieldsoft.lucc_community.adapters.AdapterChat;
import com.shieldsoft.lucc_community.models.ModelChat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatBoxActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView profile_picIV;
    TextView profile_nameTV, user_statusTV;
    ImageButton sendBtn, profile_backBtn;
    EditText mssgET;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String hisUID;
    String myUID;
    String hisImage;

    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList;
    AdapterChat adapterChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");



        readMssg();
        seenMssg();


        recyclerView = findViewById(R.id.chatbox_recyclerView);
        profile_picIV = findViewById(R.id.profile_picIV);
        profile_nameTV = findViewById(R.id.profile_nameTV);
        user_statusTV = findViewById(R.id.profile_statusTV);
        sendBtn = findViewById(R.id.sendBtn);
        mssgET = findViewById(R.id.mssgET);
        profile_backBtn = findViewById(R.id.profile_backBtn);

        //Layout(Linear Layout) for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);

        //Recycler View Properties

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();

        myUID = user.getUid();
        hisUID = intent.getStringExtra("userUID");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


        //search user to get that users Info

        Query query = databaseReference.orderByChild("uid").equalTo(hisUID);

        //get user picture and name
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    //get data
                    String name = "" + ds.child("name").getValue();
                    hisImage = "" + ds.child("image").getValue();



                    String onlineStatus = "" + ds.child("onlineStatus").getValue();
                    String typingStatus = "" + ds.child("typingTo").getValue();


                    if(typingStatus.equals(myUID)){
                        user_statusTV.setText("typing...");
                    }
                    else
                    {
                        if(onlineStatus.equals("online")){
                            user_statusTV.setText(onlineStatus);
                        }
                        else{
                            //convertTimestamp
                            //convert time to dd/mm/yy hh:mm am/pm
                            Calendar calendar= Calendar.getInstance(Locale.ENGLISH);
                            //calendar.setTimeInMillis(Long.parseLong(onlineStatus));

                            String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();
                            user_statusTV.setText("Last Seen : "+dateTime);
                        }
                    }



                    profile_nameTV.setText(name);



                    try {

                        Picasso.get().load(hisImage).placeholder(R.drawable.ic_baseline_account_circle_24).into(profile_picIV);

                    } catch (Exception e) {

                        Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(profile_picIV);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendBtn.setOnClickListener(v -> {

            String mssg = mssgET.getText().toString().trim();

            if (TextUtils.isEmpty(mssg)) {
                Toast.makeText(getApplicationContext(), "Enter text to send", Toast.LENGTH_SHORT).show();

            } else {
                sendMssg(mssg);

            }




        });


        profile_backBtn.setOnClickListener(v -> {

            finish();

        });

        mssgET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    checkTypingStatus("noOne");
                }
                else{
                    checkTypingStatus(hisUID);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


    }

    private void seenMssg() {

        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ModelChat chat = dataSnapshot.getValue(ModelChat.class);

                    if (chat.getReceiver().equals(myUID) && chat.getSender().equals(hisUID)) {

                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();

                        hasSeenHashMap.put("isSeen", true);
                        dataSnapshot.getRef().updateChildren(hasSeenHashMap);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readMssg() {

        chatList = new ArrayList<>();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Chats");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    ModelChat chat = dataSnapshot.getValue(ModelChat.class);


                    if (chat.getReceiver().equals(myUID) && chat.getSender().equals(hisUID) || chat.getReceiver().equals(hisUID) && chat.getSender().equals(myUID)) {
                        chatList.add(chat);
                    }

                    adapterChat = new AdapterChat(getApplicationContext(), chatList, hisImage);
                    adapterChat.notifyDataSetChanged();

                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterChat);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMssg(String mssg) {

        String timestamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUID);
        hashMap.put("receiver", hisUID);
        hashMap.put("message", mssg);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", false);
        dbref.child("Chats").push().setValue(hashMap);

        mssgET.setText("");

    }


    private void checkOnlineStatus(String status){


        String myUID=firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(myUID);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus",status);

        databaseReference.updateChildren(hashMap);


    }

    private void checkTypingStatus(String typing){

        String myUID=firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(myUID);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("typingTo",typing);

        databaseReference.updateChildren(hashMap);


    }

    @Override
    protected void onStart() {

        //set user status online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {

        //set user status offline and add timestamp
        String timestamp=String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {

        //set user status online
        checkOnlineStatus("online");
        super.onResume();
    }
}