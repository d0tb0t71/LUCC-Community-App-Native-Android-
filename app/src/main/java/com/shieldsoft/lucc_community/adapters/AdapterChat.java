package com.shieldsoft.lucc_community.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shieldsoft.lucc_community.R;
import com.shieldsoft.lucc_community.models.ModelChat;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {


    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;

    FirebaseUser firebaseUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
            return new MyHolder(view);
        }else{
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        //get data
        String mssg = chatList.get(position).getMessage();
        String timeStamp=chatList.get(position).getTimestamp();


        //convert time to dd/mm/yy hh:mm am/pm
        Calendar calendar= Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));

        String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();
        holder.mssg_TV.setText(mssg);
        holder.time_TV.setText(dateTime);



        try{
            Picasso.get().load(imageUrl).into(holder.chat_profile_IV);
        }catch (Exception e){

        }

        //click to show delete dialog
        /*holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete this message?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

            }
        });*/



        //set seen/delivered status of mssg
        if(position==chatList.size()-1){
            if(chatList.get(position).isSeen()){
                holder.isSeen_TV.setText("Seen");
            }else{
                holder.isSeen_TV.setText("Delivered");
            }
        }
        else
        {
            holder.isSeen_TV.setVisibility(View.GONE);
        }


    }

    /*private void deleteMessage(int position) {

        String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();



        String msgTimeStamp= chatList.get(position).getTimestamp();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Chats");

        Query query=databaseReference.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()){

                    if(ds.child("sender").getValue().equals(myUID)){

                        ds.getRef().removeValue();
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("message","This was deleted...");
                        ds.getRef().updateChildren(hashMap);
                        Toast.makeText(context.getApplicationContext(), "You can only delete your message", Toast.LENGTH_SHORT).show();

                    }
                    else{

                        Toast.makeText(context.getApplicationContext(), "You can only delete your message", Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }*/

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;

        }

    }

    class MyHolder extends RecyclerView.ViewHolder{


        //Views
        ImageView chat_profile_IV;
        TextView mssg_TV,time_TV,isSeen_TV;
        LinearLayout messageLayout;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            chat_profile_IV=itemView.findViewById(R.id.chat_profile_IV);
            mssg_TV=itemView.findViewById(R.id.mssg_TV);
            time_TV=itemView.findViewById(R.id.time_TV);
            isSeen_TV=itemView.findViewById(R.id.isSeen_TV);
            messageLayout=itemView.findViewById(R.id.messageLayout);


        }
    }

}



