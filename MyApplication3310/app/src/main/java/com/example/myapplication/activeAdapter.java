package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class activeAdapter extends RecyclerView.Adapter<activeAdapter.ViewHolder> {
    private List<myacts> mfList;
    private Context context;
    public static String petname;
    public static String documentid,clickid;
    int count=0;


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton image, image2;
        TextView name, name2;
        public static LinearLayout linearLayout;;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            image2 = view.findViewById(R.id.image2);
            name2 = view.findViewById(R.id.name2);
            linearLayout=view.findViewById(R.id.linear);

        }

    }


    public activeAdapter(List<myacts> bookList, Context context) {
        mfList = bookList;
        this.context = context;

    }

    public activeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_partofactive, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return (ViewHolder) holder;
    }



    public void onBindViewHolder(final activeAdapter.ViewHolder holder, int position) {

        final myacts friends = mfList.get(position);
        holder.name.setText(friends.getName());
        String im = friends.getImage();

        count++;
        Glide.with(context).load(im)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                //   .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.image);
//        System.out.println(com.example.myapplication.ui.home.HomeFragment.yes==true);
        System.out.println(count==com.example.myapplication.ui.home.HomeFragment.count/2+1);
//        if(com.example.myapplication.ui.home.HomeFragment.yes==true && count==com.example.myapplication.ui.home.HomeFragment.count/2+1){
//
//            holder.linearLayout.setVisibility(View.GONE);//消失掰掰
//        }

        if(count==com.example.myapplication.ui.home.HomeFragment.count/2+1){

            holder.linearLayout.setVisibility(View.GONE);//消失掰掰
        }

        holder.name2.setText(friends.getName2());
        String im2 = friends.getImage2();

        Glide.with(context).load(im2)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                //.apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.image2);
      /*  Picasso.with(context).load(im)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.image);*/

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   petname=friends.getName();
                //   System.out.println(petname);
                clickid=friends.getId();
                System.out.println(clickid);
                documentid=friends.getDocument1();
                System.out.println("id"+documentid);
                Intent intent = new Intent(context, postinthat.class);
                context.startActivity(intent);

                // v.getContext().startActivity(new Intent(v.getContext(),PetsetupActivity.class));
            }
        });
        holder.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   petname=friends.getName2();

                clickid=friends.getId2();
                documentid=friends.getDocument2();
                System.out.println("id2"+documentid);
                Intent intent = new Intent(context, postinthat.class);
                context.startActivity(intent);

                // v.getContext().startActivity(new Intent(v.getContext(),PetsetupActivity.class));
            }
        });
    }


    public int getItemCount() {
        return mfList.size();
    }
}