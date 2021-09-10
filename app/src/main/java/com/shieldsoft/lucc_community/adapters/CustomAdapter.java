package com.shieldsoft.lucc_community.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shieldsoft.lucc_community.R;
import com.shieldsoft.lucc_community.models.ContestModel;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<ContestModel> localDataSet;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final TextView textUrl;
        private final TextView startTime;
        private final TextView textDuration;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textName = (TextView) view.findViewById(R.id.contestName);
            textUrl = (TextView) view.findViewById(R.id.contestUrl);
            startTime = (TextView) view.findViewById(R.id.startTime);
            textDuration = (TextView) view.findViewById(R.id.duration);
            imageView = view.findViewById(R.id.imageView);


        }



        public TextView getTextView() {
            return textName;
        }
    }




    public CustomAdapter(ArrayList<ContestModel> dataSet , Context context) {
        localDataSet = dataSet;
        this.context =context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.individual_contest, viewGroup, false);

        return new ViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {


        viewHolder.getTextView().setText("Name : " + localDataSet.get(position).name);
        viewHolder.textUrl.setText("Open Link");
        viewHolder.startTime.setText("Start time : " + localDataSet.get(position).startTime);
        viewHolder.textDuration.setText("Duration : " + localDataSet.get(position).duration);


        String nameOfSite  = localDataSet.get(position).url;

        if (nameOfSite.contains("codeforces")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.code_forces));
        } else if (nameOfSite.contains("codechef")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.code_chef));
        } else if (nameOfSite.contains("topcoder")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.top_coder));
        } else if (nameOfSite.contains("atcoder")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.at_coder));
        } else if (nameOfSite.contains("csacademy")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.cs_academy));
        } else if (nameOfSite.contains("hackerrank")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.hacker_rank));
        } else if (nameOfSite.contains("hackerearth")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.hacker_earth));
        } else if (nameOfSite.contains("leetcode")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.leet_code));
        }else if (nameOfSite.contains("toph")) {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.toph));
        }
        else {
            viewHolder.imageView.setImageDrawable(context.getDrawable(R.drawable.unknown));
        }

        viewHolder.textUrl.setPaintFlags(viewHolder.textUrl.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        viewHolder.textUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(localDataSet.get(position).url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

