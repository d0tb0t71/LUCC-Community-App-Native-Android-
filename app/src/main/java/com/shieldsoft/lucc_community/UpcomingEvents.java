package com.shieldsoft.lucc_community;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shieldsoft.lucc_community.adapters.AdapterEvents;
import com.shieldsoft.lucc_community.models.ModelEvents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UpcomingEvents extends AppCompatActivity {

    Button ev_cancel,ev_update;
    EditText ev_title,ev_description,event_location;
    Button event_photo_add_btn;
    TextView choose_date_event,choose_time_event,location_tv_event;
    ImageView event_banner_IV;
    RecyclerView recyclerView;


    Uri imageUri;
    String pic_url="www";
    FirebaseUser user;
    FirebaseStorage firebaseStorage;

    DatabaseReference databaseReference;
    AdapterEvents adapterEvents;
    ArrayList<ModelEvents> list;

    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog timePickerDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Upcoming Events");
        actionBar.setDisplayHomeAsUpEnabled(true);



        recyclerView=findViewById(R.id.event_list_recycler_view);
        databaseReference=FirebaseDatabase.getInstance().getReference("Events");
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(UpcomingEvents.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        list = new ArrayList<>();
        adapterEvents= new AdapterEvents(this,list);
        recyclerView.setAdapter(adapterEvents);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    ModelEvents modelEvents = dataSnapshot.getValue(ModelEvents.class);
                    list.add(modelEvents);
                }

                adapterEvents.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //init events list



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.eventaddbutton,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_event_btn:
                showEventAddDialog();
            case android.R.id.home:
                onBackPressed();
                return true;
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
        event_location=view.findViewById(R.id.event_location);


        event_photo_add_btn=view.findViewById(R.id.event_photo_add_btn);
        event_banner_IV=view.findViewById(R.id.event_banner_IV);

        choose_date_event=view.findViewById(R.id.choose_date_event);
        choose_time_event=view.findViewById(R.id.choose_time_event);
        location_tv_event=view.findViewById(R.id.location_tv_event);





        AlertDialog alertDialog = builder.create();

        ev_cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });


        choose_time_event.setOnClickListener(v->{

            timePickerDialog = new TimePickerDialog(UpcomingEvents.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            String time =  ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                            choose_time_event.setText(time);
                            choose_time_event.setTextColor(Color.BLACK);
                        }
                    },0,0,false);

            timePickerDialog.show();

        });
        choose_date_event.setOnClickListener(v-> {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog= new DatePickerDialog(UpcomingEvents.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,onDateSetListener,
                    year,month,day);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month = month+1;
                    String date = dayOfMonth+"/"+month+"/"+year;
                    choose_date_event.setText(date);
                choose_date_event.setTextColor(Color.BLACK);
            }
        };


        ev_update.setOnClickListener(v-> {

            FirebaseAuth mAuth= FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();

            String eventTitle = ev_title.getText().toString();
            String eventDescription = ev_description.getText().toString();
            String eventLocation=event_location.getText().toString();
            String eventDate = choose_date_event.getText().toString();
            String eventTime = choose_time_event.getText().toString();


            if(!eventTitle.equals("")&&!eventDescription.equals("")&&!eventLocation.equals("")&&!eventDate.equals("Choose Date")&&!eventTime.equals("Choose Time")){

                HashMap<Object ,String> hashMap = new HashMap<>();


                String authorID=user.getUid();
                String authorName = user.getDisplayName();




                System.out.println(authorName +"======================================");



                hashMap.put("eventTitle",eventTitle);
                hashMap.put("eventDescription",eventDescription);
                hashMap.put("authorName",authorName);
                hashMap.put("authorID",authorID);
                hashMap.put("eventBanner",pic_url);
                hashMap.put("eventLocation",eventLocation);
                hashMap.put("eventDate",eventDate);
                hashMap.put("eventTime",eventTime);



                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("Events");


                reference.push().setValue(hashMap);

                Toast.makeText(getApplicationContext(), "Event Added Successfully", Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();
                recreate();


            }
            else {
                if(eventTitle.isEmpty())
                {
                    ev_title.setError("Enter Titile");
                }
                if(eventDescription.isEmpty()){
                    ev_description.setError("Enter Description");
                }
                if(eventDate.equals("Choose Date")){
                    choose_date_event.setTextColor(Color.RED);
                }
                if(eventTime.equals("Choose Time")){
                    choose_time_event.setTextColor(Color.RED);
                }
                if(eventLocation.equals("")){
                    event_location.setError("Enter correct Location");
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

