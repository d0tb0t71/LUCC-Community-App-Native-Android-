package com.shieldsoft.lucc_community.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shieldsoft.lucc_community.DashboardActivity;
import com.shieldsoft.lucc_community.R;
import com.shieldsoft.lucc_community.models.ModelPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {

    Context context;
    ArrayList<ModelPost> list;
    String myUid;


    public AdapterPost(Context context, ArrayList<ModelPost> list) {
        this.context = context;
        this.list = list;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.row_post,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelPost modelPost = list.get(position);
        holder.post_author.setText(modelPost.getAuthorName());
        holder.post_TV.setText(modelPost.getWoym());
        holder.post_time.setText(modelPost.getPostTime());
        String pId= modelPost.getAuthorID();
        String imageUrl= modelPost.getImageUrl();

        if(pId.equals(myUid)){
            holder.more_button.setVisibility(View.VISIBLE);
        }
        else{
            holder.more_button.setVisibility(View.GONE);
        }

        try {
            Picasso.get().load(imageUrl).into(holder.user_profile_IV);

        } catch (Exception e) {

        }

        holder.more_button.setOnClickListener(v->{
            showMoreOptions(holder.more_button,myUid,pId);


        });

        holder.post_like_btn.setOnClickListener(v->
        {
            Toast.makeText(context.getApplicationContext(), "Like Button Clicked", Toast.LENGTH_SHORT).show();
        });

    }

    private void showMoreOptions(ImageView more_button,String myUid,String pId){

        PopupMenu popupMenu = new PopupMenu(context,more_button, Gravity.END);

        if(pId.equals(myUid)){
            popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");
            popupMenu.getMenu().add(Menu.NONE,1,0,"Edit");
        }
        else{
            more_button.setVisibility(View.GONE);
        }


        //item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id= item.getItemId();
                if(id==0){
                    beginDelete(pId);
                }
                else if(id==1){
                    Toast.makeText(context.getApplicationContext(), "Edit Clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        popupMenu.show();

    }

    private void beginDelete(String pId) {

        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("authorID").equalTo(pId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    dataSnapshot.getRef().removeValue();
                    Toast.makeText(context.getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    ((DashboardActivity)context).recreate();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView user_IV;
        TextView post_author,post_TV,post_time;
        Button post_like_btn,post_comment_btn;
        ImageView more_button,user_profile_IV;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            user_IV=itemView.findViewById(R.id.user_profile_IV);
            post_author=itemView.findViewById(R.id.post_author);
            post_TV=itemView.findViewById(R.id.post_TV);
            post_like_btn=itemView.findViewById(R.id.post_like_btn);
            post_comment_btn=itemView.findViewById(R.id.post_comment_btn);
            post_time=itemView.findViewById(R.id.post_time);
            more_button=itemView.findViewById(R.id.more_button);
            user_profile_IV=itemView.findViewById(R.id.user_profile_IV);


        }
    }


}
