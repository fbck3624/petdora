package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class photo extends AppCompatActivity {

    private static int count=0;
    public static int size=0;
    public static String photo[][];
    public static String nowid;
    LinearLayout l1;
    float m2;
    int w,h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics m=getResources().getDisplayMetrics();
        m2=m.density;
        l1 =(LinearLayout) findViewById(R.id.l1);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        w=(int)(135*m2);
        h=(int)(120*m2);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("photo").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            size=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                size++;
                            }
                            photo = new String[size+1][7];
                            Log.d("TAG", String.valueOf(size));
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("photo").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "main";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG, "size => " + count);
                                String image= document.getData().get("image").toString();
                                String commentid=document.getData().get("commentid").toString();
                                String id=document.getData().get("id").toString();
                                String text=document.getData().get("text").toString();
                                String title=document.getData().get("title").toString();
                                String photoid=document.getId();
                                String nowdate=document.getData().get("time").toString();
                                photo[count][0]=image;
                                photo[count][1]=commentid;
                                photo[count][2]=id;
                                photo[count][3]=text;
                                photo[count][4]=title;
                                photo[count][5]=photoid;
                                photo[count][6]=nowdate;
                            }
                            re();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("comment").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


        Button act= findViewById(R.id.active);
        act.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent fri=new Intent(photo.this,fragment.class);
                startActivity(fri);
            }

        });
        Button fri= findViewById(R.id.best);
        fri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent fri=new Intent(photo.this,MainActivity.class);
                startActivity(fri);
            }

        });
        Button per= findViewById(R.id.personal);
        per.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent fri=new Intent(photo.this,Main5Activity.class);
                startActivity(fri);
            }

        });


        Button tra= findViewById(R.id.translate);
        tra.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(photo.this,translate.class);
                startActivity(change);
            }

        });
    }

    protected void re(){
        for (int i=1;i<=count;i++){
            final int nowcount=i;
            RelativeLayout r1=new RelativeLayout(this);
            ImageButton ib=new ImageButton(this);
            if(photo[nowcount][0]!=" "){
//                Picasso.with(this).load(photo[nowcount][0]).into(ib);   //讀取圖片
            }
            ib.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    System.out.println("now="+nowcount);
                    nowid=photo[nowcount][5];
                    System.out.println(nowid);
                    Intent fri=new Intent(photo.this,see.class);
                    startActivity(fri);
                }

            });
            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams (w,h);
            btParams.leftMargin =(int)(60*m2);
            btParams.topMargin =(int)(15*m2);
            r1.addView(ib,btParams);
            i++;
            System.out.println(i);
            if(i<=count){
                final int nowcount2=i;
                ImageButton ib2=new ImageButton(this);
                ib2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        System.out.println("now="+nowcount2);
                        nowid=photo[nowcount2][5];
                        System.out.println(nowid);

                        Intent fri=new Intent(photo.this,see.class);
                        startActivity(fri);
                    }

                });
                RelativeLayout.LayoutParams btParams2 = new RelativeLayout.LayoutParams (w,h);
                btParams2.leftMargin =(int)(236*m2);
                btParams2.topMargin =(int)(15*m2);
                r1.addView(ib2,btParams2);
            }
            l1.addView(r1);

            ImageButton write =(ImageButton) findViewById(R.id.write);
            write.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    System.out.println("test");
                    Intent fri=new Intent(photo.this,write.class);
                    startActivity(fri);
                }

            });
        }
    }

}
