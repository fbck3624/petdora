package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class postinthat extends AppCompatActivity {
    String userphoto;
    ImageButton userimg,actphoto;
    TextView name,title,address,canjoin,join,context;
    String start,finish,size;
    List<Integer> joinpeople=new ArrayList<Integer>();
    Button havejoin;
    boolean how;
    String ID;
    String uid=UUID.randomUUID().toString();
    boolean ISyou=false;
    String njoin,groupid,talkroom;
    ArrayList joinid = new ArrayList();
    boolean isjoin=false;
    Boolean iscollect=false;
    ArrayList collectid = new ArrayList();
    ImageButton collect;
    String activeid;
    Date sdate,fdate;
    Date nowtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postinthat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userimg=findViewById(R.id.image0);
        name=findViewById(R.id.sender1);
        title=findViewById(R.id.title1);
        havejoin=findViewById(R.id.button25);
        havejoin.setEnabled(false);
        address=findViewById(R.id.address1);
        canjoin=findViewById(R.id.canjoin1);
        join=findViewById(R.id.joinus1);
        context=findViewById(R.id.contet1);
        actphoto=findViewById(R.id.image11);
        collect =(ImageButton) findViewById(R.id.collect);

//        ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(postinthat.this, active.class);
                // perform whatever you want on back arrow click
            }
        });

//參加鍵ㄉ監聽
        reload();


        System.out.println("id="+activeAdapter.clickid);




      //  Button pick1= findViewById(R.id.button25);
        havejoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Intent pick=new Intent(cancel.this,fragment.class);
                // startActivity(pick);
                //按下參加，新增資料!

                if(how==true){
                    havejoin.setText("我想報名");
                    System.out.println("this"+ID);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("actattend").document(ID)
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
                    how=false;
                    reload();

                }
                else{
                    havejoin.setText("取消報名");
                    how=true;
                    Map<String, Object> city = new HashMap<>();
                    city.put("actid",Integer.valueOf(activeAdapter.clickid));
                    city.put("uid", login.loginid);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("actattend")
                            .document(ID)//document可填可不填，更新或新增集合!
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

                    if (!isjoin) {
                        Map<String, Object> p = new HashMap<>();
                        joinid.add(login.loginid);
                        p.put("name", joinid);
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
                                        Toast.makeText(postinthat.this, "更新失敗", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Map<String, Object> photo = new HashMap<>();
                        photo.put("name", login.loginid);
                        ArrayList readid = new ArrayList();
                        photo.put("read", readid);
                        photo.put("talk", login.loginid+"is已加入群組");
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

                    reload();

                }

            }

        });

        collect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!iscollect){
                    collect.setBackgroundResource(R.drawable.blueshoucang);
                    iscollect=true;
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> p = new HashMap<>();
                    collectid.add(login.loginid);
                    p.put("collection", collectid);
                    db.collection("active").document(activeid)
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
//                                    Toast.makeText(postinthat.this, "更新失敗", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else if(iscollect){
                    collect.setBackgroundResource(R.drawable.houcan);
                    iscollect=false;
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> p = new HashMap<>();
                    collectid.remove(login.loginid);
                    p.put("collection", collectid);
                    db.collection("active").document(activeid)
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
//                                    Toast.makeText(livephotopost.this, "更新失敗", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

        });

    }





 public void reload(){

     FirebaseFirestore data = FirebaseFirestore.getInstance();
     //資料寫死 記得改
     data.collection("active")
             .get()
             .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     if (task.isSuccessful()) {

                         for (QueryDocumentSnapshot document : task.getResult()) {
                             // System.out.println("click"+activeAdapter.clickid);
                             //    System.out.println(document.getLong("actid"));
                             if(activeAdapter.clickid.equals(String.valueOf(document.getLong("actid")))){
                                 title.setText(document.getString("title"));
                                 address.setText("地址:"+document.getString("address"));
                                 //     joinpeople = (List<Integer>) document.get("attend");

                                 activeid=document.getId();
                                 String ncollect=document.getData().get("collection").toString();
                                 if(ncollect!="[]") {
                                     String pcollect = ncollect;
                                     String pcollect2 = pcollect.replace("[","");
                                     String pcollect3 = pcollect2.replace("]","");
                                     String pcollect4[] = pcollect3.split(", ");

                                     System.out.println("pcollect4="+pcollect4[0]);

                                     for(int k=0;k<pcollect4.length;k++) {
                                         if(login.loginid.toString().equals(pcollect4[k])){
                                             collect.setBackgroundResource(R.drawable.blueshoucang);
                                             iscollect=true;
                                         }
                                         collectid.add(pcollect4[k]);
                                     }
                                 }

                                 // System.out.println(joinpeople.size());
                                 //  size=String.valueOf(joinpeople.size());
                                 canjoin.setText("可報名人數:"+String.valueOf(document.getLong("maxmember")));
                                 // join.setText("已報名人數:"+String.valueOf(joinpeople.size()));

                                 String ph=document.getString("photo");
                                 Glide.with(context).load(ph)
                                         .into(actphoto);
                                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                 sdate=document.getDate("sdate");
                                 fdate=document.getDate("fdate");

                                 start = sdf.format(sdate);
                                 finish =sdf.format(fdate);
                                 context.setText("活動內容:\n"+document.getString("detail")+"\n開始時間:"+start+"\n結束時間:"+finish);
                                 final String postid=document.getString("postid");
                                 FirebaseFirestore db = FirebaseFirestore.getInstance();
                                 db.collection("user")
                                         .get()
                                         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                             @Override
                                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                 if (task.isSuccessful()) {

                                                     for (QueryDocumentSnapshot document : task.getResult()) {
                                                         if(postid.equals(document.getString("uid"))){
                                                                 name.setText(document.getString("name"));
                                                                 userphoto=document.getString("photo");
                                                                 Glide.with(context).load(userphoto)
                                                                         .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                                                         .into(userimg);
                                                                 if(login.loginid.equals(document.getString("uid"))){
                                                                     System.out.println("I am user");
                                                                     ISyou=true;

                                                                 }
                                                                 else{
                                                                  ///   ISyou=false;
                                                                     System.out.println("I am not user");
                                                                     //編輯活動
                                                                     //刪除活動消失QQ
                                                                 }

                                                         }



                                                     }



                                                 } else {
                                                     Log.w("TAG", "Error getting documents.", task.getException());
                                                 }
                                             }
                                         });

                             }

                         }


                     } else {
                         Log.w("TAG", "Error getting documents.", task.getException());
                     }
                 }
             });
     FirebaseFirestore db = FirebaseFirestore.getInstance();
     db.collection("actattend")
             .get()
             .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     if (task.isSuccessful()) {
                         int count=0;
                         how=false;
                         for (QueryDocumentSnapshot document : task.getResult()) {
                            // System.out.println(String.valueOf(document.getLong("actid")));
                             if(activeAdapter.clickid.equals(String.valueOf(document.getLong("actid")))){
                                 count++;
                                 join.setText("已報名人數:"+count);
                                 if(login.loginid.equals(document.getString("uid"))){
                                     how=true;
                                     ID=document.getId();
                                     System.out.println("A"+ID);
                                 }

                             }
                             else{

                                 join.setText("已報名人數:"+count);
                                 System.out.println("random"+ID);
                             }

                         }
                         long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
                         SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                         nowtime=new Date(time);

                         System.out.println(nowtime);
                         if (fdate.before(nowtime)) {
                             havejoin.setText("報名已結束");
                             havejoin.setEnabled(false);
                         }else{
                             havejoin.setEnabled(true);
                             if(how==true){
                                 havejoin.setBackgroundResource(R.drawable.bluebutton);
                                 havejoin.setTextColor(Color.parseColor("#76D2F1"));
                                 havejoin.setText("取消報名");
                             }
                             else{
                                 havejoin.setBackgroundResource(R.drawable.yellowbutton);
                                 havejoin.setTextColor(Color.parseColor("#FFDC35"));
                                 havejoin.setText("我想報名");
                                 ID=uid;
                             }
                         }


                     } else {
                         Log.w("TAG", "Error getting documents.", task.getException());
                     }
                 }
             });

     data.collection("group")
             .get()
             .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 private static final String TAG = "group";
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     if (task.isSuccessful()) {
                         for (QueryDocumentSnapshot document : task.getResult()) {
                             if(activeAdapter.clickid.equals(String.valueOf(document.getLong("actid")))) {
                                 groupid = document.getId();
                                 talkroom = document.getData().get("talkroom").toString();
                                 njoin = document.getData().get("name").toString();
                                 if (njoin != "[]") {
                                     String pcollect = njoin;
                                     String pcollect2 = pcollect.replace("[", "");
                                     String pcollect3 = pcollect2.replace("]", "");
                                     String pcollect4[] = pcollect3.split(", ");

                                     System.out.println("joinid=" + pcollect4[0]);

                                     for (int k = 0; k < pcollect4.length; k++) {
                                         if (pcollect4[k].equals(login.loginid)) {
                                             isjoin = true;
                                         }
                                         joinid.add(pcollect4[k]);
                                     }
                                 }
                             }
                         }
                        // havejoin.setEnabled(true);
                     } else {
                         Log.w(TAG, "Error getting documents.", task.getException());
                     }
                 }
             });

 }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (ISyou == false) {
                getMenuInflater().inflate(R.menu.postinthat, menu);
                //這個事先找到postinthat.menu
                //return super.onPrepareOptionsMenu(menu);
                MenuItem photoedit = menu.findItem(R.id.actchange);
                MenuItem delete = menu.findItem(R.id.actdelete);
                 photoedit.setVisible(false);
                    delete.setVisible(false);
                    this.invalidateOptionsMenu();
                }
        else{
            getMenuInflater().inflate(R.menu.postinthat, menu);
        }

         return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        if(item.getItemId() == android.R.id.home)
        {
          //  finish();
            Intent dele = new Intent(postinthat.this, active.class);
            startActivity(dele);
            return true;

        }

        switch (item.getItemId()) {
            case R.id.actchange:

                //編輯活動up.class記得要補上去還沒寫
                postwriting.update=true;
                Intent chang = new Intent(postinthat.this, updateact.class);
                startActivity(chang);
                return true;

            case R.id.actdelete:
                //刪除還沒東西
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("active").document(activeAdapter.documentid)
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
                Intent dele = new Intent(postinthat.this, active.class);
                startActivity(dele);


                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }





}