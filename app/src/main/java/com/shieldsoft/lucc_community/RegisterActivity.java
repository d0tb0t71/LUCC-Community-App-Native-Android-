package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmailEdt, mPassEdt,mNameEdt;
    Button mRegisterBtn, mLoginBtn;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        //edit box
        mNameEdt=findViewById(R.id.nameEDT);
        mEmailEdt = findViewById(R.id.emailEDT);
        mPassEdt = findViewById(R.id.passEDT);



        //button
        mRegisterBtn = findViewById(R.id.register_btn);
        mLoginBtn = findViewById(R.id.login_now_btn);


        //firebase auth
        mAuth = FirebaseAuth.getInstance();


        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");


        //register button click
        mRegisterBtn.setOnClickListener(view ->{

            createUser();

        });

        //login button click
        mLoginBtn.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        });

    }

    private void createUser() {

        String email = mEmailEdt.getText().toString();
        String password = mPassEdt.getText().toString();
        String name = mNameEdt.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            mEmailEdt.setError("Invalid Email");
            mEmailEdt.setFocusable(true);

        } else if (password.length() < 6) {

            mPassEdt.setError("Password length must be above 6 character");
            mPassEdt.setFocusable(true);

        }
        else if (name.length()< 3) {

            mPassEdt.setError("Enter Valid Name");
            mPassEdt.setFocusable(true);

        }
        else {

            //registering

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {

                        progressDialogShow();


                        FirebaseUser user=mAuth.getCurrentUser();

                        String email=user.getEmail();
                        String uid= user.getUid();


                        HashMap<Object ,String> hashMap = new HashMap<>();

                        hashMap.put("email",email);
                        hashMap.put("uid",uid);
                        hashMap.put("name",name);
                        hashMap.put("phone","+880**********");
                        hashMap.put("image","");
                        hashMap.put("lu_id","********");
                        hashMap.put("blood_group","No Info");
                        hashMap.put("section","No Info");
                        hashMap.put("batch","No Info");
                        hashMap.put("onlineStatus","online");
                        hashMap.put("typingTo","noOne");




                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference reference=database.getReference("Users");

                        reference.child(uid).setValue(hashMap);


                        Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();


                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();


                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }


                }
            });


        }

    }


    private void progressDialogShow() {
        progress = new ProgressDialog(this);
        progress.setTitle("Registration in progress...");
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
        pdCanceller.postDelayed(progressRunnable, 500);
    }


}
