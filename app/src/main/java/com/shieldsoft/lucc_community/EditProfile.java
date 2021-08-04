package com.shieldsoft.lucc_community;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    EditText editName,editLu_Id,editPhone,editBloodGroup,editBatch,editSection;
    Button save;
    ImageButton editImgButton;
    ImageView editImageIV;
    TextView editEmailTV;


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference reference;


    FirebaseStorage firebaseStorage;

    Uri imageUri;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");




        editName=findViewById(R.id.editName);
        editLu_Id=findViewById(R.id.editLuId);
        editPhone=findViewById(R.id.editPhone);
        editBloodGroup=findViewById(R.id.editBloodGroup);
        editBatch=findViewById(R.id.editBatch);
        editSection=findViewById(R.id.editSection);
        editImageIV=findViewById(R.id.editImageIV);
        editImgButton=findViewById(R.id.editImgButton);
        editEmailTV=findViewById(R.id.editEmailTV);
        save=findViewById(R.id.save_edit);






        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");

        firebaseStorage=FirebaseStorage.getInstance();






        Query query=databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //check until required data get
                for(DataSnapshot ds:snapshot.getChildren()){




                    //get data

                    String name=""+ds.child("name").getValue();
                    String email=""+ds.child("email").getValue();
                    String phone=""+ds.child("phone").getValue();
                    String image=""+ds.child("image").getValue();
                    String blood_group=""+ds.child("blood_group").getValue();
                    String section=""+ds.child("section").getValue();
                    String batch=""+ds.child("batch").getValue();
                    String lu_id=""+ds.child("lu_id").getValue();

                    //set data
                    editEmailTV.setText("Email : "+email);
                    editName.setText(name);
                    editPhone.setText(phone);
                    editLu_Id.setText(lu_id);
                    editBloodGroup.setText(blood_group);
                    editBatch.setText(batch);
                    editSection.setText(section);





                    try {
                        Picasso.get().load(image).into(editImageIV);

                        Toast.makeText(getApplicationContext(), "TRY", Toast.LENGTH_SHORT).show();


                    }catch (Exception e){

                        Toast.makeText(getApplicationContext(), "EXCEPTION", Toast.LENGTH_SHORT).show();


                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        save.setOnClickListener(v -> {


            String name=editName.getText().toString();
            String phone=editPhone.getText().toString();
            String blood_group=editBloodGroup.getText().toString();
            String section=editSection.getText().toString();
            String batch=editBatch.getText().toString();
            String lu_id=editLu_Id.getText().toString();




            FirebaseUser user=firebaseAuth.getCurrentUser();

            String email=user.getEmail();
            String uid= user.getUid();

            HashMap<Object ,String> hashMap = new HashMap<>();


            hashMap.put("email",email);
            hashMap.put("name",name);
            hashMap.put("phone",phone);
            hashMap.put("lu_id",lu_id);
            hashMap.put("blood_group",blood_group);
            hashMap.put("section",section);
            hashMap.put("batch",batch);




             database=FirebaseDatabase.getInstance();
             reference=database.getReference("Users");

            reference.child(uid).child("name").setValue(name);
            reference.child(uid).child("phone").setValue(phone);
            reference.child(uid).child("lu_id").setValue(lu_id);
            reference.child(uid).child("blood_group").setValue(blood_group);
            reference.child(uid).child("section").setValue(section);
            reference.child(uid).child("batch").setValue(batch);


            uploadImage();


            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));






        });


        editImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mGetContent.launch("image/*");




            }
        });






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
                        editImageIV.setImageURI(result);
                        imageUri=result;


                    }

                }
            });






}