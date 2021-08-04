package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ChatBoxActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profile_picIV;
    TextView profile_nameTV,user_statusTV;
    ImageButton sendBtn,profile_backBtn;
    EditText mssgET;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String hisUID;
    String myUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");



        recyclerView=findViewById(R.id.chatbox_recyclerView);
        profile_picIV=findViewById(R.id.profile_picIV);
        profile_nameTV=findViewById(R.id.profile_nameTV);
        user_statusTV=findViewById(R.id.profile_statusTV);
        sendBtn=findViewById(R.id.sendBtn);
        mssgET=findViewById(R.id.mssgET);
        profile_backBtn=findViewById(R.id.profile_backBtn);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        Intent intent=getIntent();

        myUID=user.getUid();
        hisUID=intent.getStringExtra("userUID");

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");


        //search user to get that users Info

        Query query=databaseReference.orderByChild("uid").equalTo(hisUID);

        //get user picture and name
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){

                    //get data
                    String name=""+ds.child("name").getValue();
                    String image=""+ds.child("image").getValue();


                    profile_nameTV.setText(name);


                    try {

                        Picasso.get().load(image).placeholder(R.drawable.ic_baseline_account_circle_24).into(profile_picIV);

                    }catch (Exception e){

                        Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(profile_picIV);

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        sendBtn.setOnClickListener(v->{

            String mssg=mssgET.getText().toString().trim();

            if(TextUtils.isEmpty(mssg)){
                Toast.makeText(getApplicationContext(), "Enter text to send", Toast.LENGTH_SHORT).show();

            }
            else
            {
                sendMssg(mssg);

            }


        });


        profile_backBtn.setOnClickListener(v->{

            finish();

        });






    }

    private void sendMssg(String mssg) {
        DatabaseReference dbref=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("sender",myUID);
        hashMap.put("receiver",hisUID);
        hashMap.put("message",mssg);

        dbref.child("Chats").push().setValue(hashMap);

        mssgET.setText("");

    }
}