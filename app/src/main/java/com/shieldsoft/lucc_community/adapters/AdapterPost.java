package com.shieldsoft.lucc_community.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shieldsoft.lucc_community.R;
import com.shieldsoft.lucc_community.models.ModelPost;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {

    Context context;
    ArrayList<ModelPost> list;

    public AdapterPost(Context context, ArrayList<ModelPost> list) {
        this.context = context;
        this.list = list;
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
        holder.post_author.setText(modelPost.getAuthorName()+" Author Name");
        holder.post_TV.setText(modelPost.getWoym());
        holder.post_time.setText(modelPost.getPostTime());


        String text = modelPost.getAuthorName()+modelPost.getAuthorID()+modelPost.getWoym()+"---------------->>>>>>>>>>>";


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView user_IV;
        TextView post_author,post_TV,post_time;
        Button post_like_btn,post_comment_btn;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            user_IV=itemView.findViewById(R.id.user_profile_IV);
            post_author=itemView.findViewById(R.id.post_author);
            post_TV=itemView.findViewById(R.id.post_TV);
            post_like_btn=itemView.findViewById(R.id.post_like_btn);
            post_comment_btn=itemView.findViewById(R.id.post_comment_btn);
            post_time=itemView.findViewById(R.id.post_time);


        }
    }


}
