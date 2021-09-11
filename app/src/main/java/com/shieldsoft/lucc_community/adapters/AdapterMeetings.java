package com.shieldsoft.lucc_community.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shieldsoft.lucc_community.R;
import com.shieldsoft.lucc_community.models.ModelMeetings;

import java.util.ArrayList;

public class AdapterMeetings extends RecyclerView.Adapter<AdapterMeetings.MyViewHolder> {

    Context context;
    ArrayList<ModelMeetings> list;

    public AdapterMeetings(Context context, ArrayList<ModelMeetings> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.meeting_list_single_view,parent,false);
        return new MyViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelMeetings modelMeetings = list.get(position);
        holder.meeting_title_tv.setText(modelMeetings.getMeetingTitle());
        holder.meeting_description_tv.setText(modelMeetings.getMeetingDescription());
        holder.time_tv_meeting.setText("Time : "+modelMeetings.getMeetingTime());
        holder.date_tv_meeting.setText("Date : "+modelMeetings.getMeetingTitle());
        holder.location_tv_meeting.setText("Location : "+modelMeetings.getMeetingLocation());

        holder.delete_meeting.setOnClickListener(v -> {

            Toast.makeText(v.getContext(), "Clicked on : "+modelMeetings.getMeetingTitle(), Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView meeting_title_tv,meeting_description_tv,time_tv_meeting,date_tv_meeting,location_tv_meeting;
        ImageView delete_meeting;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            meeting_title_tv=itemView.findViewById(R.id.meeting_title_tv);
            meeting_description_tv=itemView.findViewById(R.id.meeting_description_tv);
            time_tv_meeting= itemView.findViewById(R.id.time_tv_meeting);
            date_tv_meeting = itemView.findViewById(R.id.date_tv_meeting);
            location_tv_meeting= itemView.findViewById(R.id.location_tv_meeting);

            delete_meeting = itemView.findViewById(R.id.meeting_delete_btn);



        }
    }
}
