package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class frienduser extends AppCompatActivity {
    ImageView iv_personal_icon;
    TextView name;
    Button btn_delete,btn_talk;
    String friendid=myfriendsAdapter.nowdriend;
    static Boolean isfrienduser=false;
    static String nowroom=myfriendsAdapter.nowroom;
    String nowid[]=new String[2];
    String talkid[]=new String[1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frienduser);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isfrienduser=false;
        nowroom=myfriendsAdapter.nowroom;
        iv_personal_icon=findViewById(R.id.iv_personal_icon);
        name=findViewById(R.id.name);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").whereEqualTo("uid",friendid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "user";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    String image=document.getData().get("photo").toString();
                                    Glide.with(frienduser.this).load(image)//讀取圖片
                                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                            .into(iv_personal_icon);
                                    name.setText(document.getData().get("name").toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("friend").whereEqualTo("talkroom",nowroom)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "user";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nowid[i]=document.getId();
                                i++;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("talk").whereEqualTo("talkroom",nowroom)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "user";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                talkid[i]=document.getId();
                                i++;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        btn_delete=findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose();
            }
        });
        btn_talk=findViewById(R.id.btn_talk);
        btn_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isfrienduser=true;
                group.isgroup=false;
                startActivity(new Intent(v.getContext(),friendtalk.class));
            }
        });


    }

    private void choose() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否確認刪除好友?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        for (int i=0;i<nowid.length;i++) {
                            db.collection("friend").document(nowid[i])
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
//                                            Toast.makeText(frienduser.this, "刪除成功", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                        }

                        for (int i=0;i<talkid.length;i++) {
                            if(talkid[i]==null){
                                break;
                            }
                            db.collection("talk").document(talkid[i])
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
//                                            Toast.makeText(frienduser.this, "刪除成功", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                        }

                        Intent fri=new Intent(frienduser.this,friend.class);
                        startActivity(fri);
                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
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