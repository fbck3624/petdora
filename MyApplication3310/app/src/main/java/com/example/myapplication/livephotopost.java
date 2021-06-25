package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class livephotopost extends AppCompatActivity {
    private static int now;
    String nowcid,nowid;
    String nowimage;
    String name,image,uid;
    Boolean iscollect=false,islove=false,iswrite=false;
    ArrayList collectid = new ArrayList();
    StorageReference storageReference,pic_storage;
    String commentdelete[]=new String[1000];
    String photoname;
    MenuItem photoedit,photodelete;
    String nimage,ncommentid,nid,ntext,ntitle,nphotoid,nnowdate,ncollect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livephotopost);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        final EditText title=findViewById(R.id.title);
        final EditText text=findViewById(R.id.text);
        final EditText id=findViewById(R.id.id);
        final EditText comment=findViewById(R.id.write);
        final LinearLayout c=findViewById(R.id.clayout);
        final ImageView i1=findViewById(R.id.photo);
        final ImageView iv=findViewById(R.id.ImageVie);
        final TextView date=findViewById(R.id.date1);
        final ImageButton collect =(ImageButton) findViewById(R.id.collect);

        collect.setBackgroundResource(R.drawable.houcan);
        System.out.println("livephoto="+livephoto.livephoto);
        System.out.println("mypostlivephoto="+mypostlivephoto.mypost);
        System.out.println("myfavoritelivephoto="+myfavoritelivephoto.myfavorite);

        int size=0;
        if(livephoto.livephoto){
            nowid=livephoto.nowid;
            System.out.println("nowid="+nowid);
            size=livephoto.size;
            for (int i=1; i<=size; i++){
                if(livephoto.photo[i][5]==nowid){
                    nimage=livephoto.photo[i][0];
                    ncommentid=livephoto.photo[i][1];
                    nid=livephoto.photo[i][2];
                    ntext=livephoto.photo[i][3];
                    ntitle=livephoto.photo[i][4];
                    nphotoid=livephoto.photo[i][5];
                    nnowdate=livephoto.photo[i][6];
                    ncollect=livephoto.photo[i][7];
                    now=i;
                    break;
                }
            }
        }
        else if(mypostlivephoto.mypost){
            nowid=mypostlivephoto.nowid;
            System.out.println("nowid="+nowid);
            size=mypostlivephoto.size;
            for (int i=1; i<=size; i++){
                if(mypostlivephoto.photo[i][5]==nowid){
                    nimage=mypostlivephoto.photo[i][0];
                    ncommentid=mypostlivephoto.photo[i][1];
                    nid=mypostlivephoto.photo[i][2];
                    ntext=mypostlivephoto.photo[i][3];
                    ntitle=mypostlivephoto.photo[i][4];
                    nphotoid=mypostlivephoto.photo[i][5];
                    nnowdate=mypostlivephoto.photo[i][6];
                    ncollect=mypostlivephoto.photo[i][7];
                    now=i;
                    break;
                }
            }
        }
        else if(myfavoritelivephoto.myfavorite){
            nowid=myfavoritelivephoto.nowid;
            System.out.println("nowid="+nowid);
            size=myfavoritelivephoto.size;
            for (int i=1; i<=size; i++){
                System.out.println("myfavoritelivephoto.photo[i][5]="+myfavoritelivephoto.photo[i][5]);
                if(myfavoritelivephoto.photo[i][5]==nowid){
                    nimage=myfavoritelivephoto.photo[i][0];
                    ncommentid=myfavoritelivephoto.photo[i][1];
                    nid=myfavoritelivephoto.photo[i][2];
                    ntext=myfavoritelivephoto.photo[i][3];
                    ntitle=myfavoritelivephoto.photo[i][4];
                    nphotoid=myfavoritelivephoto.photo[i][5];
                    nnowdate=myfavoritelivephoto.photo[i][6];
                    ncollect=myfavoritelivephoto.photo[i][7];
                    now=i;
                    break;
                }
            }
        }else if(noticeAdapter.isnoti){
            nowid=noticeAdapter.nowid;
            System.out.println("noticeAdapter nowid="+nowid);
            size=livephoto.size;
            for (int i=1; i<=size; i++){
                if(livephoto.photo[i][5].equals(nowid)){
                    System.out.println("nowid="+livephoto.photo[i][5]);
                    nimage=livephoto.photo[i][0];
                    ncommentid=livephoto.photo[i][1];
                    nid=livephoto.photo[i][2];
                    ntext=livephoto.photo[i][3];
                    ntitle=livephoto.photo[i][4];
                    nphotoid=livephoto.photo[i][5];
                    nnowdate=livephoto.photo[i][6];
                    ncollect=livephoto.photo[i][7];
                    now=i;
                    break;
                }
            }
        }

        if(nphotoid.equals(nowid)){
            title.setText(ntitle);
            text.setText(ntext);
            String pdate=nnowdate;
            String pdate2[]=pdate.split(" ");
            String pdate3[]=pdate2[0].split("-");
            date.setText(pdate3[0]+"/"+pdate3[1]+"/"+pdate3[2]);
            uid=nid;
            if(uid.equals(login.loginid)){
                System.out.println("Menu!!");
                iswrite=true;
            }
            db.collection("user")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(uid.equals(document.getString("uid"))){
                                        name=document.getString("name");
                                        id.setText(name);
                                        image=document.getData().get("photo").toString();
                                        Glide.with(livephotopost.this).load(image)
                                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                                .into(iv);
                                    }
                                }
                            } else {
                                Log.w("TAG", "Error getting documents.", task.getException());
                            }
                        }
                    });

            nowcid=ncommentid;
            nowimage=nimage;

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

            Transformation transformation = new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth = i1.getWidth();
                    int targetHeight = i1.getHeight();
                    int sourceWidth = source.getWidth();
                    int sourceHeight = source.getHeight();

                    if (source.getWidth() == 0) {
                        return source;
                    }
                    if(sourceWidth<sourceHeight){
                        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                        targetHeight = (int) (targetWidth * aspectRatio);
                    }else if(sourceWidth>sourceHeight){
                        double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                        targetWidth = (int) (targetHeight * aspectRatio);
                    }
                    if (targetHeight != 0 && targetWidth != 0) {
                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                        if (result != source) {
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
            Picasso.with(livephotopost.this).load(nowimage).placeholder(R.mipmap.ic_launcher).transform(transformation).into(i1);   //讀取圖片
            String pname=nimage;
            pname=pname.substring(79);
            String[] str=pname.split("\\?");
            System.out.println("str="+str[0]);
            photoname=str[0];
            System.out.println("photoname="+photoname);

            System.out.println(nowimage);
        }

        DisplayMetrics m=getResources().getDisplayMetrics();
        final float m2=m.density;
        System.out.println("now"+nowcid);


        db.collection("comment").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "t";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().get("commentid").toString());
                                if(document.getData().get("commentid").toString().equals(nowcid)) {
                                    commentdelete[i] = document.getId();
                                    final String nowuserid=document.getData().get("id").toString();
                                    final String nowcomment=document.getData().get("comment").toString();
                                    i++;
                                    RelativeLayout r1 = new RelativeLayout(livephotopost.this);
                                    final TextView t1 = new TextView(livephotopost.this);
//                                    for (int x = 0; x < login.i; x++) {
//                                        if (document.getData().get("id").toString().equals(login.uid[x].toString())) {
//                                            t1.setText(login.name[x] + " : " + document.getData().get("comment").toString());
//                                            System.out.println("name="+name);
//                                            break;
//                                        }
//                                    }
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("user")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(nowuserid.equals(document.getString("uid"))){
                                                                t1.setText(document.getString("name")+ " : " + nowcomment);
                                                            }
                                                        }
                                                    } else {
                                                        Log.w("TAG", "Error getting documents.", task.getException());
                                                    }
                                                }
                                            });

                                    RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams((int) (420 * m2), (int) (40 * m2));
                                    btParams.leftMargin = 0;
                                    btParams.topMargin = 0;
                                    r1.addView(t1, btParams);
                                    c.addView(r1);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });





        ImageButton send =(ImageButton) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final String cc=comment.getText().toString();
                if(!cc.equals("")) {
                    String commentid = ncommentid;
                    RelativeLayout r1 = new RelativeLayout(livephotopost.this);
                    final TextView t2 = new TextView(livephotopost.this);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("user")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(login.loginid.equals(document.getString("uid"))){
                                                t2.setText(document.getString("name") + " : " + cc);
                                            }
                                        }
                                    } else {
                                        Log.w("TAG", "Error getting documents.", task.getException());
                                    }
                                }
                            });
                    String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    Map<String, Object> ctext = new HashMap<>();
                    ctext.put("comment", cc);
                    ctext.put("time", nowDate);
                    ctext.put("commentid", commentid);
                    ctext.put("id", login.loginid);
                    ctext.put("read", "0");

                    db.collection("comment")
                            .add(ctext)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                private static final String TAG = "ta";

                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                }
                            });

                    if(!uid.equals(login.loginid)) {
                        Map<String, Object> notice = new HashMap<>();
                        notice.put("notice", "回覆你說：" + cc);
                        notice.put("pid", nowid);
                        notice.put("read", false);
                        notice.put("uid", uid);
                        notice.put("uid2", login.loginid);
                        db.collection("notice")
                                .add(notice)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    private static final String TAG = "ta";

                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error adding document", e);
                                    }
                                });
                }

                RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams((int) (420 * m2), (int) (40 * m2));
                    btParams.leftMargin = 0;
                    btParams.topMargin = 0;
                    r1.addView(t2, btParams);
                    c.addView(r1);
                    comment.setText("");
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
                    db.collection("photo").document(nowid)
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
                                    Toast.makeText(livephotopost.this, "更新失敗", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else if(iscollect){
                    collect.setBackgroundResource(R.drawable.houcan);
                    iscollect=false;
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> p = new HashMap<>();
                    collectid.remove(login.loginid);
                    p.put("collection", collectid);
                    db.collection("photo").document(nowid)
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
                                    Toast.makeText(livephotopost.this, "更新失敗", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(livephotopost.this, livephoto.class);
            startActivity(intent);
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.livephotopost, menu);

        photoedit=menu.findItem(R.id.photoedit);
        photodelete=menu.findItem(R.id.photodelete);
        photoedit.setVisible(false);
        photodelete.setVisible(false);
        if(iswrite){
            photoedit.setVisible(true);
            photodelete.setVisible(true);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
        switch (item.getItemId()) {
            case R.id.photoedit:
                Intent chang = new Intent(livephotopost.this, livephotoupdate.class);
                startActivity(chang);
                return true;


            case R.id.photodelete:
                choose();
                //要寫刪除生活照，原本的要刪除還要跳到update頁面才能刪怪怪的
                return true;

                //分享還沒做



            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void choose() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否確認刪除生活照?")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        pic_storage=storageReference.child("photo/"+photoname);
                        pic_storage.delete();

                        db.collection("photo").document(nowid)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(livephotopost.this, "刪除成功", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                        if(commentdelete!=null) {
                            for (int i = 0; i < commentdelete.length; i++) {
                                if(commentdelete[i]==null){
                                    break;
                                }
                                db.collection("comment").document(commentdelete[i])
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Toast.makeText(livephotopost.this, "commentdelete success", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                            }
                        }

                        Intent fri=new Intent(livephotopost.this,livephoto.class);
                        startActivity(fri);
                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
    }

}