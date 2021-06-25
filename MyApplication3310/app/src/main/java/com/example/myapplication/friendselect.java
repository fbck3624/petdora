package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class friendselect extends AppCompatActivity {
    ImageView image;
    TextView name,introduction;
    String uid;
    String mtalkroom="0",mtalkid="0";
    String friend[]=new String[1000];
    String nowfriend[]=new String[1000];
    String randomid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendselect);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = findViewById(R.id.iv_personal_icon);
        name = findViewById(R.id.name);
        introduction = findViewById(R.id.textView);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("friend").whereEqualTo("name",login.loginid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "group";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nowfriend[i]=document.getString("friend");
                                System.out.println("nowfriend[i]"+nowfriend[i]);

                                i++;
                            }
                            allfriend();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


        db.collection("group")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "group";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(Integer.valueOf(mtalkroom) < Integer.valueOf(document.getData().get("talkroom").toString())){
                                    mtalkroom=document.getData().get("talkroom").toString();
                                }
                            }
                            mtalkroom=Integer.toString(Integer.valueOf(mtalkroom)+1);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        Button accept= findViewById(R.id.button_yes);
        accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("friend")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            private static final String TAG = "friend";
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(Integer.valueOf(mtalkroom) < Integer.valueOf(document.getData().get("talkroom").toString())){
                                            mtalkroom=document.getData().get("talkroom").toString();
                                        }
                                        if(Integer.valueOf(mtalkid) < Integer.valueOf(document.getData().get("friend_id").toString())){
                                            mtalkid=document.getData().get("friend_id").toString();
                                        }
                                    }
                                    mtalkroom=Integer.toString(Integer.valueOf(mtalkroom)+1);
                                    mtalkid=Integer.toString(Integer.valueOf(mtalkid)+1);
                                    Boolean isfriend=false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getData().get("name").toString().equals(login.loginid.toString())
                                                && document.getData().get("friend").toString().equals(uid.toString())) {
                                            isfriend=true;
                                            break;
                                        }
                                    }
                                    if(!isfriend) {
                                        addfriend();
                                    }else{
                                        Intent back = new Intent(friendselect.this, friend.class);
                                        startActivity(back);
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });

            }

        });
        Button reject= findViewById(R.id.button_no);
        reject.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent back = new Intent(friendselect.this, friend.class);
                startActivity(back);
            }

        });

    }

    public void addfriend(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> photo = new HashMap<>();
        photo.put("name", login.loginid.toString());
        photo.put("friend",uid.toString());
        photo.put("friend_id", mtalkid);
        photo.put("talkroom", mtalkroom);

        db.collection("friend")
                .add(photo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    private static final String TAG = "test";

                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    private static final String TAG = "test";
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        Map<String, Object> photo2 = new HashMap<>();
        photo2.put("name", uid.toString());
        photo2.put("friend",login.loginid.toString());
        photo2.put("friend_id", Integer.toString(Integer.valueOf(mtalkid)+1));
        photo2.put("talkroom", mtalkroom);

        db.collection("friend")
                .add(photo2)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    private static final String TAG = "test";

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent back = new Intent(friendselect.this, friend.class);
                        startActivity(back);
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    private static final String TAG = "test";
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void allfriend(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "user";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    boolean isfriend=false;
                                    for (int x=0;x<nowfriend.length;x++) {
                                        if(nowfriend[x]==null){
                                            break;
                                        }
                                        if (!document.getData().get("uid").toString().equals(login.loginid.toString()) && !document.getData().get("uid").toString().equals(nowfriend[x])) {
                                            isfriend=false;
                                        }else{
                                            isfriend=true;
                                            break;
                                        }
                                    }
                                    if (!isfriend){
                                        friend[i] = document.getData().get("name").toString();
                                        System.out.println("friend[i]" + friend[i]);
                                        i++;
                                    }
                            }
                            randomfriend();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void randomfriend(){
        int size=0;
        for(int i=0;i<friend.length;i++){
            if (friend[i]==null){
                break;
            }
            size++;
        }
        int r=(int)(Math.random()*size);
        randomid=friend[r];

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "user";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("name").toString().equals(randomid)) {
                                    String im=document.getData().get("photo").toString();
                                    Glide.with(friendselect.this).load(im)//讀取圖片
                                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                            .into(image);
                                    name.setText(document.getData().get("name").toString());
                                    introduction.setText(document.getData().get("context").toString());
                                    uid=document.getString("uid");
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}