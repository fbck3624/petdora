package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.fragment.newdate;

public class cancel extends AppCompatActivity {
    TextView title,sender,address,context,date,canjoin,joinus;
    Button doyoujoin;
    ImageView im1,photo;
    int click= fragment.click;
    String act_sender,user_photo;
    static String[] act_title,act_address,act_context,act_documentid,attend_documentid,act_photo;
    static Date[] act_date;
    static Long[] postid,max,att_id,att_uid,act_id;
    int att_count,count;
    int joinpeople;
    String docuname= fragment.docu_id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);
        doyoujoin=findViewById(R.id.button);
        title=findViewById(R.id.title);
        sender=findViewById(R.id.sender);
        address=findViewById(R.id.address);
        context=findViewById(R.id.contet);
        date=findViewById(R.id.date);
        canjoin=findViewById(R.id.canjoin);
        joinus=findViewById(R.id.joinus);
        im1=findViewById(R.id.image1);
        photo=findViewById(R.id.image);
        reload();
//判斷自己是否有參加該活動!
        if(fragment.hasjoin==true){
            doyoujoin.setText("取消參與活動");

        }else{
            doyoujoin.setText("參與活動");
        }

        Button pick= findViewById(R.id.back);
        pick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pick=new Intent(cancel.this,fragment.class);
                startActivity(pick);
            }

        });
//參加鍵ㄉ監聽
        Button pick1= findViewById(R.id.button);
        pick1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               // Intent pick=new Intent(cancel.this,fragment.class);
               // startActivity(pick);
                //按下參加，新增資料!
                if(doyoujoin.getText().equals("參與活動")){
                    Log.d("first", "att" + att_count);
                    docuname=String.valueOf(att_count);
                    doyoujoin.setText("取消參與活動");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> city = new HashMap<>();
                    city.put("actid", com.example.myapplication.fragment.act_id[click]);
                    city.put("uid", login.loginid);
                    db.collection("actattend")
                            .document(docuname)//document可填可不填，更新或新增集合!
                            .set(city)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error writing document", e);
                                }
                            });
                    reload();//reload表示重讀資料庫

                }else{
                    //取消參加，刪除資料!
                    doyoujoin.setText("參與活動");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("actattend").document(docuname)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error deleting document", e);
                                }
                            });
                    reload();
                }
            }

        });

    }
    //讀取資料庫ㄉ涵式
    public void reload(){
        attend_documentid=new String[1000];
        act_documentid=new String[1000];
        act_title = new String[1000];
        //act_sender = new String[1000];
        postid = new Long[1000];
        max=new Long[1000];
        act_id=new Long[1000];
        att_id=new Long[1000];
        att_uid=new Long[1000];
        act_address = new String[1000];
        act_context = new String[1000];
        act_photo = new String[1000];
        act_date = new Date[1000];



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        count=0;
                        if (task.isSuccessful()) {
                            //   Long x;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d("HII", i+":"+document.getId() + " => " + document.getString("petname"));
                                act_documentid[count] = document.getId();
                                act_title[count] = document.getString("title");
                                postid[count] = document.getLong("postid");
                                act_address[count] = document.getString("address");
                                act_context[count] = document.getString("detail");
                                act_date[count] = document.getDate("posttime");
                                act_id[count] = document.getLong("actid");
                                act_photo[count] = document.getString("photo");
                                max[count] = document.getLong("maxmember");

                                //x = document.getLong("actid");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                                newdate[count] = sdf.format(act_date[count]);
                                //act_context[i].substring(3,8);

                                Log.d("post", "title" + ":" + postid[count]);

                                count++;
                                //   Log.d("22", "count:" + count);

                            }

                            for (int z = 0; z <= count; z++){
                                if (postid[click] == com.example.myapplication.login.uid[z]) {
                                   // Log.d("2202", "postid" + ":" + postid[click]);
                                   // Log.d("2202", "uid" + ":" + com.example.myapplication.login.uid[z]);
                                    act_sender= com.example.myapplication.login.name[z];
                                    user_photo= login.user_photo[z];
                                }
                            }

                                title.setText(act_title[click]);
                                sender.setText(act_sender);
                                sender.setTextSize(12);
                                address.setText("地點:"+act_address[click]);
                                address.setTextSize(15);
                                context.setText("活動內容:"+act_context[click]);
                                context.setTextSize(15);
                                date.setText("時間:"+ newdate[click]);
                                date.setTextSize(15);
                                canjoin.setText("可報名人數:"+(max[click]));
                                canjoin.setTextSize(15);
                                //縮放圖片!!
                            Transformation transformation = new Transformation() {
                                @Override
                                public Bitmap transform(Bitmap source) {

                                    int targetWidth = im1.getWidth();
                                    if (source.getWidth() == 0) {
                                        return source;
                                    }
                                    //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                                    int targetHeight = (int) (targetWidth * aspectRatio);
                                    if (targetHeight != 0 && targetWidth != 0) {
                                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                                        if (result != source) {
                                            // Same bitmap is returned if sizes are the same
                                            source.recycle();
                                        }
                                        return result;
                                    } else {
                                        return source;
                                    }

                                }

                                @Override
                                public String key() {
                                    return "transformation" + " desiredWidth";
                                }
                            };
                                im1.getBackground().setAlpha(0);
                                Picasso.with(cancel.this)
                                        .load(act_photo[click])
                                        .transform(transformation)
                                        .into(im1);
                            Transformation transformation2 = new Transformation() {
                                @Override
                                public Bitmap transform(Bitmap source) {

                                    int targetWidth = photo.getWidth();
                                    if (source.getWidth() == 0) {
                                        return source;
                                    }
                                    //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                                    int targetHeight = (int) (targetWidth * aspectRatio);
                                    if (targetHeight != 0 && targetWidth != 0) {
                                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                                        if (result != source) {
                                            // Same bitmap is returned if sizes are the same
                                            source.recycle();
                                        }
                                        return result;
                                    } else {
                                        return source;
                                    }

                                }

                                @Override
                                public String key() {
                                    return "transformation" + " desiredWidth";
                                }
                            };
                            photo.getBackground().setAlpha(0);
                            Picasso.with(cancel.this)
                                    .load(user_photo)
                                    .transform(transformation2)
                                    .into(photo);

                                FirebaseFirestore data = FirebaseFirestore.getInstance();
                                data.collection("actattend")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                att_count=0;
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        // Log.d("HII", document.getId() + " => " + document.getLong("actid"));
                                                        att_id[att_count]=document.getLong("actid");
                                                        att_uid[att_count]=document.getLong("uid");
                                                        attend_documentid[att_count]=document.getId();

                                                        att_count++;
                                                        Log.d("0800", "att" + att_count);
                                                    }
                                                    joinpeople=0;
                                                    for(int att=0;att<=att_count;att++){
                                                        Log.d("00", "att" + act_id[click]);

                                                        if(att_id[att]==act_id[click]){
                                                            joinpeople++;
                                                        }

                                                    }
                                                    joinus.setText("報名人數:"+joinpeople);
                                                    joinus.setTextSize(15);

                                                } else {
                                                    Log.w("000", "Error getting documents.", task.getException());
                                                }

                                            }
                                        });
                            }

                        else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }


                    }
                });


    }


}
