package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

public class friendsAdapter extends RecyclerView.Adapter<friendsAdapter.ViewHolder>{
    private List<friends> mfList;
    private Context context;
    public static String nowtalk,nowroom;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image,count;
        TextView name,say,time,unread;
        public ViewHolder(View view){
            super(view);
            image=view.findViewById(R.id.image);
            name=view.findViewById(R.id.name);
            say=view.findViewById(R.id.say);
            time=view.findViewById(R.id.time);
            count=view.findViewById(R.id.count);
            unread=view.findViewById(R.id.unread);
        }
    }

    public friendsAdapter(List<friends> bookList,Context context){
        mfList=bookList;
        this.context=context;
    }
    @Override
    public friendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,parent,false);
        RecyclerView.ViewHolder holder=new ViewHolder(view);
        return (ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(final friendsAdapter.ViewHolder holder, int position) {
        System.out.println("adapter!!!");

        final friends friends=mfList.get(position);
        holder.name.setText(friends.getName());
        holder.say.setText(friends.getSay());
        holder.time.setText(friends.getTime());
        String un=friends.getUnread();
        if(un==null){
            un="0";
        }
        if(Integer.valueOf(un)>0) {
            holder.count.setImageResource(friends.getCount());
            holder.unread.setText(friends.getUnread());
        }
        Glide.with(context).load(friends.getImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowtalk=friends.getName();
                nowroom=friends.getTalkroom();
                frienduser.isfrienduser=false;
                group.isgroup=false;
                System.out.println("isfrienduser="+frienduser.isfrienduser);
                v.getContext().startActivity(new Intent(v.getContext(),friendtalk.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mfList.size();
    }
}


