package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ViewHolder> {

    private List<chat> mfList;
    String nowtime="0";
    private Context context;
    public static String friendname;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView say,time,unread,say2,time2,date,name,readcount,isgroup;
        ImageView image;
        CardView carddate,cardisgroup;
        public ViewHolder(View view){
            super(view);
            say=view.findViewById(R.id.say);
            time=view.findViewById(R.id.time);
            unread=view.findViewById(R.id.read);
            say2=view.findViewById(R.id.say2);
            time2=view.findViewById(R.id.time2);
            name=view.findViewById(R.id.name);
            image=view.findViewById(R.id.image);
            readcount=view.findViewById(R.id.readcount);

            date=view.findViewById(R.id.date);
            carddate=view.findViewById(R.id.carddate);
            isgroup=view.findViewById(R.id.isgroup);
            cardisgroup=view.findViewById(R.id.cardisgroup);
        }
    }


    public chatAdapter(List<chat> bookList,Context context){
        mfList=bookList;
        this.context=context;
    }
    public chatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        RecyclerView.ViewHolder holder=new chatAdapter.ViewHolder(view);
        return (chatAdapter.ViewHolder) holder;
    }


    public void onBindViewHolder(final chatAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        System.out.println("position="+position);

        final chat chat=mfList.get(position);
        String name=chat.getName();
        System.out.println("name="+name+",loginid="+login.loginid);
        String rr=chat.getUnread();
        if(rr.equals("0")){
            holder.unread.setVisibility(View.INVISIBLE);
            holder.readcount.setVisibility(View.INVISIBLE);
        }else if(rr.equals("1")){
            holder.readcount.setText(chat.getUnread());
            holder.unread.setVisibility(View.VISIBLE);
            holder.readcount.setVisibility(View.INVISIBLE);
            if(group.isgroup){
                holder.readcount.setVisibility(View.VISIBLE);
            }
        }else{
            holder.readcount.setText(chat.getUnread());
            holder.unread.setVisibility(View.VISIBLE);
            holder.readcount.setVisibility(View.VISIBLE);
        }

        String t=chat.getTime();
        System.out.println("time="+t);
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final String date[]=t.split(" ");
        String time2[]=date[1].split(":");
        System.out.println("nowtime="+nowtime+",date="+date[0]+",time="+time2[0]+":"+time2[1]);
        System.out.println("nowDate="+nowDate);
        if(nowtime.equals(date[0])){
            holder.date.setVisibility(View.INVISIBLE);
            holder.carddate.setVisibility(View.INVISIBLE);
        }else{
            nowtime=date[0];
            if(nowtime.equals(nowDate)){
                holder.date.setText("今天");
            }else{
                holder.date.setText(date[0]);
            }
        }
        final String tt;
        tt=time2[0]+":"+time2[1];

        if(!name.equals(login.loginid.toString())) {
            friendname=chat.getName();
            System.out.println("friendname="+friendname);
        }

        if(chat.getSay().contains("is已加入群組")){
            System.out.println("is已加入群組=");
            holder.say2.setVisibility(View.INVISIBLE);
            holder.time2.setVisibility(View.INVISIBLE);
            holder.name.setVisibility(View.INVISIBLE);
            holder.image.setVisibility(View.INVISIBLE);
            holder.say.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);
            holder.unread.setVisibility(View.INVISIBLE);
            holder.readcount.setVisibility(View.INVISIBLE);

            final String[] name1 = new String[1];
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("user").whereEqualTo("uid",chat.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    name1[0] =document.getData().get("name").toString();
                                    System.out.println("已加入"+name1[0]);
                                    holder.isgroup.setText(tt+"\n"+ name1[0] +"已加入群組");}
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }else if(chat.getSay().contains("is已退出群組")){
            holder.say2.setVisibility(View.INVISIBLE);
            holder.time2.setVisibility(View.INVISIBLE);
            holder.name.setVisibility(View.INVISIBLE);
            holder.image.setVisibility(View.INVISIBLE);
            holder.say.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);
            holder.unread.setVisibility(View.INVISIBLE);
            holder.readcount.setVisibility(View.INVISIBLE);

            final String[] name1 = new String[1];
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("user").whereEqualTo("uid",chat.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    name1[0] = document.getData().get("name").toString();
                                    holder.isgroup.setText(tt+"\n"+ name1[0] +"已退出群組");
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }else if(name.equals(login.loginid.toString())){
            holder.say.setText(chat.getSay());
            System.out.println("chat="+chat.getSay());
            holder.time.setText(tt);
            holder.say2.setVisibility(View.INVISIBLE);
            holder.time2.setVisibility(View.INVISIBLE);
            holder.name.setVisibility(View.INVISIBLE);
            holder.image.setVisibility(View.INVISIBLE);
            holder.isgroup.setVisibility(View.INVISIBLE);
            holder.cardisgroup.setVisibility(View.INVISIBLE);
        }else{
            holder.say2.setText(chat.getSay());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("user").whereEqualTo("uid",chat.getName())
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
            System.out.println("chat="+chat.getSay());
            holder.time2.setText(tt);
            holder.say.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);
            holder.unread.setVisibility(View.INVISIBLE);
            holder.readcount.setVisibility(View.INVISIBLE);
            holder.isgroup.setVisibility(View.INVISIBLE);
            holder.cardisgroup.setVisibility(View.INVISIBLE);
        }



//        String un=chat.getUnread();
//        if(Integer.valueOf(un)>0) {
//            holder.unread.setText(chat.getUnread());
//        }
    }

    public int getItemCount() {
        return mfList.size();
    }

}

