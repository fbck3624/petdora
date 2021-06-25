package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class noticeAdapter extends RecyclerView.Adapter<noticeAdapter.ViewHolder> {
    private List<note> mfList;
    private Context context;

    public String id;
    int count=0;
    String ID;
    static boolean isnoti=false;
    static String nowid;

    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        ImageView image;
        TextView name,reponse;
        public ViewHolder(View view){
            super(view);
            image=view.findViewById(R.id.image);
            linearLayout=view.findViewById(R.id.line);
            name=view.findViewById(R.id.name);
            reponse=view.findViewById(R.id.reply);
        }

    }


    public noticeAdapter(ArrayList<note> bookList, Context context){
        mfList=bookList;
        this.context=context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item,parent,false);
        RecyclerView.ViewHolder holder=new ViewHolder(view);
        return (ViewHolder) holder;
    }


    public void onBindViewHolder(final ViewHolder holder, int position) {
        final note friends=mfList.get(position);
        System.out.println(friends.getnotice());
        //holder.image.setText(friends.getnotice());
        String im=friends.getImage();
        count++;
        Glide.with(context).load(im)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                //   .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.image);
        String name=friends.getName();
        holder.name.setText(name);
        String notice=friends.getnotice();
        holder.reponse.setText(notice);
        boolean read=friends.getread();
        if(read==false){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#BC89D8FB"));
        }else{
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID= friends.getId();
                String pid=friends.getPhoto();
                System.out.println(pid);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> notice = new HashMap<>();
                notice.put("read", true);
                System.out.println("this is ID :"+ID);
                db.collection("notice").document(ID)
                        .set(notice, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                               // Toast.makeText(noticeAdapter.this, "更新成功", Toast.LENGTH_SHORT).show();
                             //   loading.dismisslog();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                              //  Toast.makeText(registersetup.this, "更新失敗", Toast.LENGTH_SHORT).show();
                            }
                        });



                    isnoti=true;
                    livephoto.livephoto=false;
                    mypostlivephoto.mypost=false;
                    myfavoritelivephoto.myfavorite=false;
                    nowid=friends.getPhoto();
                    System.out.println("這裡是生活照");
                    holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                    v.getContext().startActivity(new Intent(v.getContext(),livephotopost.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return mfList.size();
    }
}


