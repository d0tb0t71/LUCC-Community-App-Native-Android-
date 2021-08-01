package com.shieldsoft.lucc_community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {

    Button Sign_Out;

    ImageView user_photo;
    TextView user_name,user_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        Sign_Out = findViewById(R.id.sign_out);
        user_photo = findViewById(R.id.user_photo);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);


        /*SharedPreferences preferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);

        String username=preferences.getString("username","");
        String useremail=preferences.getString("useremail","");
        String userphotoURL=preferences.getString("userPhoto","");



        user_name.setText(username);
        user_email.setText(useremail);
        Glide.with(this).load(userphotoURL).into(user_photo);*/


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.child("Name").setValue("Tanzim");


        Sign_Out.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();


        });


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}