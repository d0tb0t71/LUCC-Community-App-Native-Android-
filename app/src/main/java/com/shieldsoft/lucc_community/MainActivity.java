package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button mRegister_btn, mLogin_btn, sign_in_google;

    FirebaseAuth mAuth;


    GoogleSignInClient mGoogleSignInClient;

    public static final int RC_SIGN_IN=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(!isConnected(this)){
            showInternetDialog();
        }






        mRegister_btn = findViewById(R.id.register_home_btn);
        mLogin_btn = findViewById(R.id.login_home_btn);
        sign_in_google = findViewById(R.id.sign_in_google);


        mAuth = FirebaseAuth.getInstance();



        mRegister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        mLogin_btn.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        });



        requestGoogleSignIn();

        sign_in_google.setOnClickListener(v -> {

            signIn();


        });


    }

    private void requestGoogleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());



                //Shared Preference
                SharedPreferences.Editor editor=getApplicationContext()
                        .getSharedPreferences("MyPrefs",MODE_PRIVATE)
                        .edit();

                editor.putString("username",account.getDisplayName());
                editor.putString("useremail",account.getEmail());
                editor.putString("userPhoto",account.getPhotoUrl().toString());
                editor.apply();




            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "Authentication Failed !\n"+e, Toast.LENGTH_SHORT).show();

            }
        }
    }



    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseUser user=mAuth.getCurrentUser();

                            if(task.getResult().getAdditionalUserInfo().isNewUser()){


                                String email=user.getEmail();
                                String uid= user.getUid();
                                String image=user.getPhotoUrl().toString();
                                String name=user.getDisplayName();


                                HashMap<Object ,String> hashMap = new HashMap<>();

                                hashMap.put("email",email);
                                hashMap.put("uid",uid);
                                hashMap.put("name",name);
                                hashMap.put("phone","+880**********");
                                hashMap.put("image",image);
                                hashMap.put("lu_id","********");
                                hashMap.put("blood_group","No Info");
                                hashMap.put("section","No Info");
                                hashMap.put("batch","No Info");


                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                DatabaseReference reference=database.getReference("Users");

                                reference.child(uid).setValue(hashMap);


                            }



                            // Sign in success, update UI with the signed-in user's information

                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication Failed !", Toast.LENGTH_SHORT).show();


                        }
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
                if(!isConnected(MainActivity.this)){
                    showInternetDialog();
                }
                else{
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private boolean isConnected(MainActivity mainActivity) {

        ConnectivityManager connectivityManager= (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiCon != null && wifiCon.isConnected() )|| (mobileCon != null && mobileCon.isConnected());

    }


}