package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class notiAdapter extends RecyclerView.Adapter<notiAdapter.ViewHolder> {
    private List<notis> mfList;
    private Context context;
    static boolean isnoti=false;
    static String commentid;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,reply;
        ImageView image;
        LinearLayout l1;
        public ViewHolder(View view){
            super(view);
            name=view.findViewById(R.id.name);
            reply=view.findViewById(R.id.reply);
            image=view.findViewById(R.id.image);
            l1=view.findViewById(R.id.l1);
        }
    }


    public notiAdapter(List<notis> bookList,Context context){
        mfList=bookList;
        this.context=context;
    }
    public notiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item,parent,false);
        RecyclerView.ViewHolder holder=new notiAdapter.ViewHolder(view);
        return (notiAdapter.ViewHolder) holder;
    }


    public void onBindViewHolder(final notiAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        System.out.println("position="+position);

        final notis noti=mfList.get(position);
        holder.reply.setText(noti.getReply());
        if (noti.getRead().equals("0")){
            holder.l1.setBackgroundColor(Color.parseColor("#BC89D8FB"));
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").whereEqualTo("uid",noti.getName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                holder.name.setText(document.getData().get("name").toString());
                                Glide.with(context).load(document.getData().get("photo").toString())
                                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                        .into(holder.image);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isnoti=true;
                livephoto.livephoto=false;
                mypostlivephoto.mypost=false;
                myfavoritelivephoto.myfavorite=false;
                commentid=noti.getCommentid();
                holder.l1.setBackgroundColor(Color.parseColor("#FFFFFF"));

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> p = new HashMap<>();
                p.put("read","1");
                db.collection("comment").document(noti.getId())
                        .set(p, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                v.getContext().startActivity(new Intent(v.getContext(),livephotopost.class));
            }
        });
    }

    public int getItemCount() {
        return mfList.size();
    }

}
