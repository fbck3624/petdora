package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class friendacceptAdapter extends RecyclerView.Adapter<friendacceptAdapter.ViewHolder> {
    private List<friendaccept> mfList;
    private Context context;

    public String id;
    int count=0;
    String ID;

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        Button accept,refuse;
        public ViewHolder(View view){
            super(view);
            image=view.findViewById(R.id.image);

            name=view.findViewById(R.id.name);
            accept=view.findViewById(R.id.accept);
            refuse=view.findViewById(R.id.refuse);
        }

    }


    public friendacceptAdapter(ArrayList<friendaccept> bookList, Context context){
        mfList=bookList;
        this.context=context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_accept_item,parent,false);
        RecyclerView.ViewHolder holder=new ViewHolder(view);
        return (ViewHolder) holder;
    }


    public void onBindViewHolder(final ViewHolder holder, int position) {
        final friendaccept friends=mfList.get(position);
        ID= friends.getId();
        String im=friends.getImage();
        count++;
        Glide.with(context).load(im)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                //   .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.image);
        String name=friends.getName();
        holder.name.setText(name);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return mfList.size();
    }
}
