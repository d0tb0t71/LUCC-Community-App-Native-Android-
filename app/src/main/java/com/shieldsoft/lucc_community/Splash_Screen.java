package com.shieldsoft.lucc_community;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {


    Animation topAnim, bottomAnim, left_to_right, right_to_left;

    ImageView img2, splash_logo, img3, img4;
    TextView community_app_tv, a_promise_to_lead_tv;



    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        left_to_right = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        right_to_left = AnimationUtils.loadAnimation(this, R.anim.right_to_left);


        img2 = findViewById(R.id.img2);
        splash_logo = findViewById(R.id.splash_logo);
        community_app_tv = findViewById(R.id.community_app_tv);
        a_promise_to_lead_tv = findViewById(R.id.a_promise_to_lead_tv);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);


        img2.setAnimation(topAnim);
        splash_logo.setAnimation(topAnim);
        community_app_tv.setAnimation(bottomAnim);
        a_promise_to_lead_tv.setAnimation(bottomAnim);
        img3.setAnimation(right_to_left);
        img4.setAnimation(left_to_right);


    }





    @Override
    protected void onStart() {
        super.onStart();




            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    FirebaseUser user = mAuth.getCurrentUser();

                    if (user != null) {

                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();
                    } else {


                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }, 1500);


    }


}