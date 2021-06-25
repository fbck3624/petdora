package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class group extends AppCompatActivity {
    private List<groups> bookList=new ArrayList<>();
    private String[][] group,talk,card;
    int readcount=0;
    int size2,fr;
    int c=0;
    static Boolean isgroup=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        findViewById(R.id.friend_talk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(group.this, friend.class);
                startActivity(intent);
                //Toast.makeText(mContext,getString(R.string.btn_start),Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.friend_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(group.this, newfriend.class);
                startActivity(intent);
                //Toast.makeText(mContext,getString(R.string.btn_start),Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.friend_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(group.this, myfriend.class);
                startActivity(intent);
                //Toast.makeText(mContext,getString(R.string.btn_start),Toast.LENGTH_SHORT).show();
            }
        });

        group = new String[1000][3];
        card = new String[1000][6];
        talk = new String[1000][6];

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("group").whereArrayContains("name",login.loginid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "main";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String iamge= document.getData().get("image").toString();
                                String groupname=document.getData().get("groupname").toString();
                                String talkroom=document.getData().get("talkroom").toString();
                                group[count][0]=iamge;
                                group[count][1]=groupname;
                                group[count][2]=talkroom;
                                Log.d("friend=", document.getData().toString());
                                count++;
                            }
                            gettalk();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void lay(){
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.id_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        groupsAdapter adapter1=new groupsAdapter(bookList,this);
        recyclerView.setAdapter(adapter1);
    }

    private void initBooks(){
        for (int i=0;i<card.length;i++){
            if(card[i][0]==null) {
                break;
            }
//            System.out.println("card="+card[i][0]+" "+card[i][1]+" "+card[i][2]);
            groups group = new groups(card[i][0], card[i][1], card[i][2], card[i][3], R.drawable.unread,card[i][4],card[i][5]);
            bookList.add(group);
        }
        lay();
    }
    private void gettalk(){
        System.out.println("gettalk");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("talk")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "talk2";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(int i=0;i<group.length;i++){
                                if(group[i][0]==null) {
                                    break;
                                }
                                final String groupimage=group[i][0];
                                final String groupname=group[i][1];
                                final String room=group[i][2];
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getData().get("talkroom").toString().equals(room) &&
                                            !document.getData().get("talk").toString().contains("is已加入群組") &&
                                            !document.getData().get("talk").toString().contains("is已退出群組")) {
                                        if(fr<group.length){
                                            String talktext=document.getData().get("talk").toString();
                                            String time=document.getData().get("time").toString();
                                            String talkroom=document.getData().get("talkroom").toString();
                                            talk[i][0]=groupname;
                                            talk[i][1]=talktext;
                                            talk[i][2]=talkroom;
                                            talk[i][3]=time;
                                            talk[i][4]=groupimage;
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            break;
                                        }
                                    }
                                }
                            }
                            readcount();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void readcount(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("talk").orderBy("talkroom")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "readcount";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(int i=0;i<group.length;i++){
                                if(group[i][0]==null) {
                                    break;
                                }
                                Log.d("read=", "readcount");
                                readcount=0;
                                final String room=group[i][2];
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    System.out.println("read=  loginid"+login.loginid+","+document.getData().get("name").toString());
                                    System.out.println("read=  talkroom"+room+","+document.getData().get("talkroom").toString());
                                    System.out.println("read=  read"+login.loginid+","+document.getData().get("read").toString());
                                    if(!document.getData().get("name").toString().equals(login.loginid) &&
                                            document.getData().get("talkroom").toString().equals(room) &&
                                            !document.getData().get("read").toString().contains(login.loginid) &&
                                            !document.getData().get("talk").toString().contains("is已加入群組") &&
                                            !document.getData().get("talk").toString().contains("is已退出群組")) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        readcount++;
                                        talk[i][5] = String.valueOf(readcount);
                                        Log.d("read=", talk[i][5]);
                                    }
                                }
                            }
                            re();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(group.this, friend.class);
            startActivity(intent);
        }
        return false;
    }

    private void re(){
        System.out.println("getre");
        c=0;
        for (int i = 0; i < group.length; i++) {
            if (group[i][0] == null) {
                break;
            }
            if (talk[i][0] == null) {
                card[c][0] = group[i][1];
                card[c][1] = null;
                card[c][2] = group[i][0];
                card[c][3] = null;
                card[c][4] = null;
                card[c][5] = group[i][2];
                System.out.println("1card"+i+"= " + c + " => " + card[c][0] + " => " + card[c][1] + " => " + card[c][2] + " => " + card[c][3] + " => " + card[c][4] + " => " + card[c][5]);
                c++;
            } else{
                card[c][0] = talk[i][0];
                card[c][1] = talk[i][1];
                card[c][2] = talk[i][4];
                card[c][3] = talk[i][3];
                card[c][4] = talk[i][5];
                card[c][5] = talk[i][2];
                System.out.println("2card"+i+"= " + c + " => " + card[c][0] + " => " + card[c][1] + " => " + card[c][2] + " => " + card[c][3] + " => " + card[c][4] + " => " + card[c][5]);
                c++;
        }
        }
        initBooks();
    }
}
