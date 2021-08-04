package com.shieldsoft.lucc_community.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shieldsoft.lucc_community.ChatBoxActivity;
import com.shieldsoft.lucc_community.R;
import com.shieldsoft.lucc_community.models.ModelUsers;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder>{

    Context context;
    List<ModelUsers> usersList;

    //constructor


    public AdapterUsers(Context context, List<ModelUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout (row user)

        View view= LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);


        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //get data
        String userUID=usersList.get(position).getUid();
        String userImage=usersList.get(position).getImage();
        String userName=usersList.get(position).getName();
        String userEmail=usersList.get(position).getEmail();
        String userLUID=usersList.get(position).getLu_id();


        //set data

        holder.mNameTV.setText(userName);
        holder.mEmailTV.setText("Email: "+userEmail);
        holder.mLu_id.setText("LU ID: "+userLUID);

        try{

            Picasso.get().load(userImage)
                    .placeholder(R.drawable.ic_baseline_perm_identity_24)
                    .into(holder.mAvatarIV);

        }catch (Exception e){
            

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent=new Intent(context, ChatBoxActivity.class);
               intent.putExtra("userUID",userUID);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);

                
            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mAvatarIV;
        TextView mNameTV,mEmailTV,mLu_id;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mAvatarIV=itemView.findViewById(R.id.profile_image);
            mEmailTV=itemView.findViewById(R.id.emailTV);
            mNameTV=itemView.findViewById(R.id.nameTV);
            mLu_id=itemView.findViewById(R.id.lu_idTV);


        }
    }



}
