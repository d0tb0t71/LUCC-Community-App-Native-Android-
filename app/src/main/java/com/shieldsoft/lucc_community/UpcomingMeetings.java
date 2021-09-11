package com.shieldsoft.lucc_community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shieldsoft.lucc_community.adapters.AdapterMeetings;
import com.shieldsoft.lucc_community.models.ModelMeetings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UpcomingMeetings extends AppCompatActivity {

    RecyclerView recyclerView;
    Button m_cancel,m_add;
    EditText meeting_title,meeting_description,meeting_location;
    TextView choose_time_meeting,choose_date_meeting;
    TimePickerDialog timePickerDialog;


    DatePickerDialog.OnDateSetListener onDateSetListener;

    DatabaseReference databaseReference;
    AdapterMeetings adapterMeetings;
    ArrayList<ModelMeetings> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_meetings);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Upcoming Meetings");
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.meeting_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        databaseReference =FirebaseDatabase.getInstance().getReference("Meetings");
        LinearLayoutManager layoutManager = new LinearLayoutManager(UpcomingMeetings.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapterMeetings = new AdapterMeetings(this,list);
        recyclerView.setAdapter(adapterMeetings);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ModelMeetings modelMeetings = dataSnapshot.getValue(ModelMeetings.class);
                    list.add(modelMeetings);

                }

                adapterMeetings.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meeting_add_button,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.add_meeting:
                showMeetingAddDialog();
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMeetingAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view= LayoutInflater.from(this).inflate(R.layout.add_meeting_dialog,findViewById(R.id.meeting_dialog));
        builder.setView(view);

        m_cancel=view.findViewById(R.id.m_cancel);
        m_add=view.findViewById(R.id.m_add);

        meeting_title=view.findViewById(R.id.meeting_title);
        meeting_description=view.findViewById(R.id.meeting_description);
        meeting_location=view.findViewById(R.id.meeting_location);

        choose_time_meeting=view.findViewById(R.id.choose_time_meeting);
        choose_date_meeting=view.findViewById(R.id.choose_date_meeting);

        AlertDialog alertDialog = builder.create();

        m_cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });


        choose_time_meeting.setOnClickListener(v->{

            timePickerDialog = new TimePickerDialog(UpcomingMeetings.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            String time =  ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                            choose_time_meeting.setText(time);
                            choose_date_meeting.setTextColor(Color.BLACK);
                        }
                    },0,0,false);

            timePickerDialog.show();

        });
        choose_date_meeting.setOnClickListener(v-> {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog= new DatePickerDialog(UpcomingMeetings.this,
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
                choose_date_meeting.setText(date);
                choose_date_meeting.setTextColor(Color.BLACK);
            }
        };


        m_add.setOnClickListener(v-> {

            FirebaseAuth mAuth= FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();

            String meetingTitle = meeting_title.getText().toString();
            String meetingDescription = meeting_description.getText().toString();
            String meetingLocation = meeting_location.getText().toString();
            String meetingDate = choose_date_meeting.getText().toString();
            String meetingTime = choose_time_meeting.getText().toString();


            if(!meetingTitle.equals("")&&!meetingDescription.equals("")&&!meetingLocation.equals("")&&!meetingDate.equals("Choose Date")&&!meetingTime.equals("Choose Time")){

                HashMap<Object ,String> hashMap = new HashMap<>();

                String authorID=user.getUid();
                String authorName = user.getDisplayName();


                hashMap.put("meetingTitle",meetingTitle);
                hashMap.put("meetingDescription",meetingDescription);
                hashMap.put("authorName",authorName);
                hashMap.put("authorID",authorID);
                hashMap.put("meetingLocation",meetingLocation);
                hashMap.put("metingDate",meetingDate);
                hashMap.put("meetingTime",meetingTime);



                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference reference=database.getReference("Meetings");


                reference.push().setValue(hashMap);

                Toast.makeText(getApplicationContext(), "Meeting Added Successfully", Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();
                recreate();


            }
            else {
                if(meetingTitle.isEmpty())
                {
                    meeting_title.setError("Enter Titile");
                }
                if(meetingDescription.isEmpty()){
                    meeting_description.setError("Enter Description");
                }
                if(meetingDate.equals("Choose Date")){
                    choose_date_meeting.setTextColor(Color.RED);
                }
                if(meetingTime.equals("Choose Time")){
                    choose_time_meeting.setTextColor(Color.RED);
                }
                if(meetingLocation.equals("")){
                    meeting_location.setError("Enter correct Location");
                }

            }


        });



        alertDialog.show();

    }
}