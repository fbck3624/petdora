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

public class myfriendsAdapter extends RecyclerView.Adapter<myfriendsAdapter.ViewHolder> {
    private List<myfriends> mfList;
    private Context context;
    public static String nowdriend,nowroom;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;
        public ViewHolder(View view){
            super(view);
            image=view.findViewById(R.id.image);
            name=view.findViewById(R.id.name);
        }
    }

    public myfriendsAdapter(List<myfriends> bookList,Context context){
        mfList=bookList;
        this.context=context;
    }

    @Override
    public myfriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfriend_item,parent,false);
        RecyclerView.ViewHolder holder=new myfriendsAdapter.ViewHolder(view);
        return (ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(final myfriendsAdapter.ViewHolder holder, int position) {
        System.out.println("adapter!!!");
        final myfriends friends=mfList.get(position);
        holder.name.setText(friends.getName());

        Glide.with(context).load(friends.getImage())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowdriend=friends.getFriendid();
                nowroom=friends.getNowroom();
                group.isgroup=false;
                frienduser.isfrienduser=false;
                v.getContext().startActivity(new Intent(v.getContext(),frienduser.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mfList.size();
    }
}
