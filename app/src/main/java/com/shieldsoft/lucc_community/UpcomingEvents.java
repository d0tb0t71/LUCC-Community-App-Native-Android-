package com.shieldsoft.lucc_community;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UpcomingEvents extends AppCompatActivity {

    Button ev_cancel,ev_update;
    EditText ev_title,ev_description;
    Button event_photo_add_btn;
    ImageView event_banner_IV;

    RecyclerView recyclerView;
    Uri imageUri;
    String pic_url="www";
    FirebaseUser user;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Upcoming Events");

        recyclerView=findViewById(R.id.event_list_recycler_view);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.eventaddbutton,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_event_btn:
                showEventAddDialog();
        }


        return super.onOptionsItemSelected(item);
    }
    private void showEventAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view= LayoutInflater.from(this).inflate(R.layout.add_event_dialog,findViewById(R.id.event_dialog));
        builder.setView(view);

        ev_cancel=view.findViewById(R.id.ev_cancel);
        ev_update=view.findViewById(R.id.ev_update);

        ev_title=view.findViewById(R.id.event_title);
        ev_description=view.findViewById(R.id.event_description);
        event_photo_add_btn=view.findViewById(R.id.event_photo_add_btn);
        event_banner_IV=view.findViewById(R.id.event_banner_IV);


        AlertDialog alertDialog = builder.create();

        ev_cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        ev_update.setOnClickListener(v-> {

            FirebaseAuth mAuth= FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();

            String eventTitle = ev_title.getText().toString();
            String eventDescription = ev_description.getText().toString();


            if(!eventTitle.equals("")&&!eventDescription.equals("")){

                HashMap<Object ,String> hashMap = new HashMap<>();


                String authorID=user.getUid();
                String authorName=user.getDisplayName();



                hashMap.put("eventTitle",eventTitle);
                hashMap.put("eventDescription",eventDescription);
                hashMap.put("authorName",authorName);
                hashMap.put("authorID",authorID);
                hashMap.put("eventBanner",pic_url);


                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("Events");


                reference.push().setValue(hashMap);

                Toast.makeText(getApplicationContext(), "Event Updated Successfully", Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();


            }
            else {
                if(eventTitle.isEmpty())
                {
                    ev_title.setError("Enter Titile");
                }
                if(eventDescription.isEmpty()){
                    ev_description.setError("Enter Description");
                }

            }


        });

        event_photo_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mGetContent.launch("image/*");




            }
        });



        alertDialog.show();

    }

    private void uploadImage() {

        if(imageUri!=null){

            String uid=user.getUid().toString();

            StorageReference pic_reference = firebaseStorage.getReference().child("ProfilePic/"+ uid.toString());

            pic_reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){


                        Toast.makeText(getApplicationContext(), "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Failed...\n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

            });
            pic_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    String pic_url=uri.toString();
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Events");
                    reference.child(uid).child("image").setValue(pic_url);

                }
            });

        }

    }
    ActivityResultLauncher<String> mGetContent =  registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if(result!=null){
                        event_banner_IV.setImageURI(result);
                        event_banner_IV.setVisibility(View.VISIBLE);
                        imageUri=result;


                    }

                }
            });

}

