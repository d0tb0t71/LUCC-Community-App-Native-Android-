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
import com.shieldsoft.lucc_community.models.ModelEvents;

import java.util.ArrayList;

public class AdapterEvents extends RecyclerView.Adapter<AdapterEvents.MyViewHolder> {

    Context context;
    ArrayList<ModelEvents> list;

    public AdapterEvents(Context context, ArrayList<ModelEvents> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.event_list_single_view,parent,false);

        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelEvents modelEvents = list.get(position);
        holder.ev_title.setText(modelEvents.getEventTitle());
        holder.ev_description.setText(modelEvents.getEventDescription());
        holder.time_tv_event.setText("Time : "+modelEvents.getEventTime());
        holder.date_tv_event.setText("Date : "+modelEvents.getEventDate());
        holder.location_tv_event.setText("Location : "+modelEvents.getEventLocation());


        holder.delete_events.setOnClickListener(v-> {

            Toast.makeText(context, "Clicked on :" + modelEvents.getEventTitle(), Toast.LENGTH_SHORT).show();

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ev_title,ev_description,time_tv_event,date_tv_event,location_tv_event;
        ImageView delete_events;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ev_title = itemView.findViewById(R.id.event_title_tv);
            ev_description = itemView.findViewById(R.id.event_description_tv);
            delete_events = itemView.findViewById(R.id.event_delete_btn);
            time_tv_event = itemView.findViewById(R.id.time_tv_event);
            date_tv_event = itemView.findViewById(R.id.date_tv_event);
            location_tv_event = itemView.findViewById(R.id.location_tv_event);



        }
    }

}
