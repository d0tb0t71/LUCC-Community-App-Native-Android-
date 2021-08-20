package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText LEmailEDT,LPassEDT;
    Button Login_Btn,Register_Now_Btn,Recover_Now;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        //Edit box
        LEmailEDT=findViewById(R.id.LEmailEDT);
        LPassEDT=findViewById(R.id.LPassEDT);

        //Button
        Login_Btn=findViewById(R.id.login_btn);
        Register_Now_Btn=findViewById(R.id.register_now_btn);
        Recover_Now=findViewById(R.id.recover_now);


        //auth
        mAuth=FirebaseAuth.getInstance();


        //ProgressDialog
        progressDialog=new ProgressDialog(this);




        Login_Btn.setOnClickListener(v->{

            loginUser();

        });


        Recover_Now.setOnClickListener(v->{

            ShowRecoverPasswordDialog();

        });

        Register_Now_Btn.setOnClickListener(v->{

            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            finish();
        });




    }

    private void ShowRecoverPasswordDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        builder.setIcon(R.drawable.recover_pass_icon);


        LinearLayout linearLayout=new LinearLayout(this);

        EditText emailRe= new EditText(this);
        emailRe.setHint("Enter Email Here...");
        emailRe.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        linearLayout.addView(emailRe);
        linearLayout.setPadding(20,20,20,20);


        builder.setView(linearLayout);


        //Positive Button
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email=emailRe.getText().toString();
                beginRecover(email);


            }
        });

        //Negative Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });


        builder.create().show();



    }

    private void beginRecover(String email) {

        progressDialog.setMessage("Sending Email...");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                progressDialog.dismiss();

                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Recovery Email Sent", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {



                //show error massage
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();





            }
        });

    }

    private void loginUser() {

        String email= LEmailEDT.getText().toString();
        String pass= LPassEDT.getText().toString();

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Login Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }



            }
        });


    }


}