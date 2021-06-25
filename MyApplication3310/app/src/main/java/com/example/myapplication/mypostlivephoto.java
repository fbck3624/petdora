package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class mypostlivephoto extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private static int count=0;
    public static int size=1000;
    public static String photo[][];
    public static String nowid;
    public static boolean mypost;
    String friendid[]=new String[1000];
    LinearLayout l1;
    float m2;
    int w,h;
    String image;
    TextView name;
    static String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypostlivephoto);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics m=getResources().getDisplayMetrics();
        m2=m.density;
        l1 =(LinearLayout) findViewById(R.id.line);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        w=(int)(135*m2);
        h=(int)(120*m2);
        mypost=false;
        photo = new String[1000][8];
        myphoto();

    }

    protected void re(){
        for (int i=1;i<=count;i++){
            final int nowcount=i;
            RelativeLayout r1=new RelativeLayout(this);
            final ImageButton ib=new ImageButton(this);
            ib.setBackgroundColor(Color.TRANSPARENT);
            System.out.println(photo[nowcount][0]);
            Transformation transformation = new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth = ib.getWidth();
                    int targetHeight = ib.getHeight();
                    int sourceWidth = source.getWidth();
                    int sourceHeight = source.getHeight();

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
            Picasso.with(this).load(photo[nowcount][0]).placeholder(R.mipmap.ic_launcher).transform(transformation).into(ib);   //讀取圖片  fit()
            ib.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    System.out.println("now="+nowcount);
                    nowid=photo[nowcount][5];
                    System.out.println(nowid);
                    mypost=true;
                    myfavoritelivephoto.myfavorite=false;
                    livephoto.livephoto=false;
                    Intent fri=new Intent(mypostlivephoto.this,livephotopost.class);
                    startActivity(fri);
                }

            });
            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams (w,h);
            btParams.leftMargin =(int)(30*m2);
            btParams.topMargin =(int)(15*m2);
            r1.addView(ib,btParams);
            i++;
            System.out.println(i);
            if(i<=count){
                final int nowcount2=i;
                final ImageButton ib2=new ImageButton(this);
                ib2.setBackgroundColor(Color.TRANSPARENT);
                Transformation transformation2 = new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        int targetWidth = ib2.getWidth();
                        int targetHeight = ib2.getHeight();
                        int sourceWidth = source.getWidth();
                        int sourceHeight = source.getHeight();

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
                Picasso.with(this).load(photo[nowcount2][0]).placeholder(R.mipmap.ic_launcher).transform(transformation2).into(ib2);
                ib2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        System.out.println("now="+nowcount2);
                        nowid=photo[nowcount2][5];
                        System.out.println(nowid);
                        mypost=true;
                        myfavoritelivephoto.myfavorite=false;
                        livephoto.livephoto=false;
                        Intent fri=new Intent(mypostlivephoto.this,livephotopost.class);
                        startActivity(fri);
                    }

                });
                RelativeLayout.LayoutParams btParams2 = new RelativeLayout.LayoutParams (w,h);
                btParams2.leftMargin =(int)(200*m2);
                btParams2.topMargin =(int)(15*m2);
                r1.addView(ib2,btParams2);
            }
            l1.addView(r1);
        }
    }

    public void myphoto(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("photo").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "main";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("id").toString().equals(login.loginid.toString())) {
                                    count++;
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    Log.d(TAG, "size => " + count);
                                    String image = document.getData().get("image").toString();
                                    String commentid = document.getData().get("commentid").toString();
                                    String id = document.getData().get("id").toString();
                                    String text = document.getData().get("text").toString();
                                    String title = document.getData().get("title").toString();
                                    String photoid = document.getId();
                                    String nowdate = document.getData().get("time").toString();
                                    String collect = document.getData().get("collection").toString();
                                    photo[count][0] = image;
                                    photo[count][1] = commentid;
                                    photo[count][2] = id;
                                    photo[count][3] = text;
                                    photo[count][4] = title;
                                    photo[count][5] = photoid;
                                    photo[count][6] = nowdate;
                                    photo[count][7] = collect;
                                }
                            }
                            re();
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
