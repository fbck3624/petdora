package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
public class friendtalk extends AppCompatActivity {
    public static Handler mHandler = new Handler();
    EditText editText;
    String tmp;
    Socket clientSocket;
    private ImageView imageview;
    String data_list;
    int PICK_CONTACT_REQUEST=1;
    Uri uri;
    StorageReference storageReference,pic_storage;
    private List<chat> test=new ArrayList<>();
    public String[][] talk = new String[1000][4];
    String talkroom;
    final String maxid[]=new String[1];
    String nowDate;
    String isread="0";
    String notread[][]=new String[1000][2];
    ArrayList readid = new ArrayList();
    ArrayList readname=new ArrayList();
    ArrayList groupname = new ArrayList();
    String groupid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendtalk);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference= FirebaseStorage.getInstance().getReference();

        System.out.println("isfrienduser="+frienduser.isfrienduser);
        talkroom=friendsAdapter.nowroom;
        if(frienduser.isfrienduser){
            talkroom=frienduser.nowroom;
        }else if(group.isgroup){
            talkroom=groupsAdapter.nowroom;
        }
        System.out.println("talkroom"+talkroom);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("talk").whereEqualTo("talkroom",talkroom)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "talk";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!document.getData().get("name").toString().equals(login.loginid.toString())
                                        && !document.getData().get("read").toString().contains(login.loginid)) {
                                    notread[i][0]=document.getId();
                                    notread[i][1]=document.getData().get("read").toString();
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    i++;
                                }
                            }
                            getchat();
                            readupdate();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        editText=findViewById(R.id.input);
        Thread t = new Thread(readData);
        t.start();

        Button send=findViewById(R.id.enter);
        send.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if(clientSocket==null){

                }else if(clientSocket.isConnected()){

                    if(editText.getText().toString().equals("") || editText.getText().toString().trim().isEmpty()) {
//                        System.out.println("length="+editText.getText().toString().length());
                    }else{
                        Thread thread = new Thread() {
                            public void run() {
                                try {
                                    BufferedWriter bw;
                                    bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                                    bw.write(talkroom+":"+login.loginid + ":" + editText.getText() + "\n");
                                    bw.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();

                        editText.setText("");
                    }
                }
            }
        });



    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){

            if(clientSocket==null){
                if(group.isgroup){
                    Intent babc = new Intent(friendtalk.this, group.class);
                    startActivity(babc);
                }else {
                    Intent babc = new Intent(friendtalk.this, friend.class);
                    startActivity(babc);
                }
            }else if(clientSocket.isConnected()){
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            BufferedWriter bw;
                            // 取得網路輸出串流
                            bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                            // 寫入訊息
                            bw.write(talkroom+":"+login.loginid + ":" + "is offline!" + "\n");
                            // 立即發送
                            bw.flush();

                            clientSocket.close();
                            if(group.isgroup){
                                Intent babc = new Intent(friendtalk.this, group.class);
                                startActivity(babc);
                            }else {
                                Intent babc = new Intent(friendtalk.this, friend.class);
                                startActivity(babc);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }

        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_CONTACT_REQUEST){
            uri=data.getData();
            imageview.setImageURI(uri);
            ContentResolver contentResolver=getContentResolver();
            MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
            data_list=mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Runnable updateText = new Runnable() {
        public void run() {
            if(tmp!=null) {
                String write[] = tmp.split(":");
                String room = write[0];
                String name = write[1];
                String say = write[2];
                System.out.println("room=" + room + ",talkroom=" + talkroom);

                if(room.equals(talkroom) && !name.equals(login.loginid.toString())) {
                    if (say.equals("is online!")) {
                        System.out.println("say="+say);
                        readid.add(name);
//                        getchat();
//                        isread = "1";
                    } else if (say.equals("is offline!")) {
                        System.out.println("say="+say);
                        readid.remove(name);
//                        isread = "0";
                    }else if(say.equals("if online?")){
                        System.out.println("say="+say);
                        if(clientSocket.isConnected()){
                            Thread thread = new Thread() {
                                public void run() {
                                    try {
                                        BufferedWriter bw;
                                        bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                                        bw.write(talkroom+":"+login.loginid + ":" + "is online!" + "\n");
                                        bw.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                        }
                    }
                }

//                System.out.println("isread="+isread);
                nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String t[] = nowDate.split(" ");
                String t2[] = t[1].split(":");

                if (room.equals(talkroom) && !say.equals("is online!") && !say.equals("is offline!") && !say.equals("if online?")) {
                    chat c = new chat(name, say, nowDate, isread);
                    test.add(c);
                    lay();
                }

                if (name.equals(login.loginid.toString()) && !say.equals("is online!") && !say.equals("is offline!") && !say.equals("if online?")) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("talk").orderBy("talkid", Query.Direction.DESCENDING).limit(1)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                private static final String TAG = "talkid";

                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData().get("talkid").toString());
                                            maxid[0] = document.getData().get("talkid").toString();
                                        }
                                        updatetalk();
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                }
                            });
                }
            }
        }
    };

    // 取得網路資料
    private Runnable readData = new Runnable() {
        public void run() {
            // server端的IP
            InetAddress serverIp;

            try {
                // 以內定(本機電腦端)IP為Server端
                serverIp = InetAddress.getByName("140.127.220.66");
                int serverPort = 20000;
                clientSocket = new Socket(serverIp, serverPort);

                // 取得網路輸入串流
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
                if(clientSocket.isConnected()){
                    Thread thread = new Thread() {
                        public void run() {
                            try {
                                BufferedWriter bw;
                                bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                                bw.write(talkroom+":"+login.loginid + ":" + "is online!" + "\n");
                                bw.flush();

                                bw.write(talkroom+":"+" " + ":" + "if online?" + "\n");
                                bw.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
                // 當連線後
                while (clientSocket.isConnected()) {
                    // 取得網路訊息
                    tmp = br.readLine();
                    System.out.println("socket read="+tmp);
                    // 如果不是空訊息則
                    if(tmp!="")
                        // 顯示新的訊息
                        mHandler.post(updateText);
                }

            } catch (IOException e) {

            }
        }
    };

    private void lay(){
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.id_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(friendtalk.this);  //LinearLayoutManager中定製了可擴充套件的佈局排列介面，子類按照介面中的規範來實現就可以定製出不同排雷方式的佈局了
//        layoutManager.setStackFromEnd(true);//顯示在底部
        //配置佈局，預設為vert.ical（垂直佈局），下邊這句將佈局改為水平佈局
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter adapter=new chatAdapter(test,this);
        recyclerView.setAdapter(adapter);
    }

    private void initBooks(){
        for (int i=0;i<talk.length;i++){
            if(talk[i][0]==null){
                break;
            }
            chat c = new chat(talk[i][0],talk[i][1],talk[i][2],talk[i][3]);
            test.add(c);
        }
    }

    private void getchat(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("talk").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("talkroom").equals(talkroom)){
                                    talk[i][0]=document.getData().get("name").toString();
                                    talk[i][1]=document.getString("talk");
                                    talk[i][2]=document.getString("time");
                                    String read=document.getData().get("read").toString();
                                    if(read!="[]") {
                                        String read2[] = read.split(",");
                                        talk[i][3] = String.valueOf(read2.length);
                                    }else{
                                        talk[i][3] = String.valueOf(0);
                                    }
                                    i++;
                                }
                                Log.d("talk=", document.getId() + " => " + document.getData());

                            }
                            initBooks();
                            lay();
//                            Log.d("TAG", );
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void updatetalk(){
        String write[] = tmp.split(":");
        Map<String, Object> photo = new HashMap<>();
        String id=login.loginid.toString();
        photo.put("name", id);
        photo.put("read", readid);
        photo.put("talk", write[2]);
        photo.put("talkroom", write[0]);
        photo.put("talkid", (Integer.valueOf(maxid[0]) + 1));
        photo.put("time", nowDate);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("talk")
                .add(photo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    private static final String TAG = "test";

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(friendtalk.this, "success!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
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

    public void readupdate(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for(int i=0;i<notread.length;i++) {
            if(notread[i][0]==null){
                break;
            }
            Map<String, Object> p = new HashMap<>();
            if (notread[i][1] != "[]") {
                String pcollect = notread[i][1];
                String pcollect2 = pcollect.replace("[", "");
                String pcollect3 = pcollect2.replace("]", "");
                String pcollect4[] = pcollect3.split(", ");

                System.out.println("joinid=" + pcollect4[0]);

                for (int k = 0; k < pcollect4.length; k++) {
                    readname.add(pcollect4[k]);
                }
            }
            if(!readname.contains(login.loginid)) {
                readname.add(login.loginid);
            }
            p.put("read", readname);
            db.collection("talk").document(notread[i][0])
                    .set(p, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
//                            Toast.makeText(friendtalk.this, "read!!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if(group.isgroup){
            getMenuInflater().inflate(R.menu.friendtalk, menu);
        }else{
            menu.clear();
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
        switch (item.getItemId()){
            case R.id.leavegroup:
                choose();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void choose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否確認退出群組?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("group")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    private static final String TAG = "group";
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if(talkroom.equals(document.getData().get("talkroom").toString())) {
                                                    groupid = document.getId();
                                                    String njoin = document.getData().get("name").toString();
                                                    if (njoin != "[]") {
                                                        String pcollect = njoin;
                                                        String pcollect2 = pcollect.replace("[", "");
                                                        String pcollect3 = pcollect2.replace("]", "");
                                                        String pcollect4[] = pcollect3.split(", ");

                                                        System.out.println("joinid=" + pcollect4[0]);

                                                        for (int k = 0; k < pcollect4.length; k++) {
                                                            groupname.add(pcollect4[k]);
                                                        }
                                                    }
                                                }
                                            }
                                            leavegroup();
                                        } else {
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }
                                    }
                                });
                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
    }

    private void leavegroup(){
        Map<String, Object> p = new HashMap<>();
        groupname.remove(login.loginid);
        p.put("name", groupname);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("group").document(groupid)
                .set(p, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

//                                    Toast.makeText(livephotopost.this, "更新成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(friendtalk.this, "更新失敗", Toast.LENGTH_SHORT).show();
                    }
                });
        Map<String, Object> photo = new HashMap<>();
        photo.put("name", login.loginid);
        ArrayList readid = new ArrayList();
        photo.put("read", readid);
        photo.put("talk", login.loginid+"is已退出群組");
        photo.put("talkroom", talkroom);
        photo.put("talkid", "");
        String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        photo.put("time", nowDate);
        db.collection("talk")
                .add(photo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    private static final String TAG = "test";

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent fri=new Intent(friendtalk.this,friend.class);
                        startActivity(fri);
//                        Toast.makeText(friendtalk.this, "success!!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
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
}