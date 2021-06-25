package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class livephoto extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private static int count=0;
    public static int size=0;
    public static String photo[][];
    public static String nowid;
    public static boolean livephoto=false;
    public static boolean islivephototimesetup=false;
    static boolean isfirst=true;
//    MenuItem note,notread;
    static MenuItem note;
    TextView textall,textfriend;
    String friendid[]=new String[1000];
    LinearLayout l1;
    float m2;
    int w,h;
    String image;
    TextView name;
    static String ID;
    static String time;
    static Intent intent2;
    Drawable drawable;
    int read=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livephoto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        livephoto=false;
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_livephoto, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
       // note=findViewById(R.id.action_notifications);
        //notread=findViewById(R.id.action_notread);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

//        if(isfirst) {
//            Intent intent = new Intent(livephoto.this, messageService.class);
//            startService(intent);
//        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").whereEqualTo("uid",login.loginid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                time=document.getData().get("notice").toString();
                                islivephototimesetup=true;
                                intent2 = new Intent(livephoto.this,ExampleService.class);
                                startService(intent2);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


        name=findViewById(R.id.name);
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d("HII", document.getId() + " => " + document.getLong("actid"));
                                //if(login.loginid==document.getLong("uid")){
                                if(document.getString("uid").equals(login.loginid)){
                                    ID=document.getId();
                                    name.setText(document.getString("name"));
                                    image=document.getString("photo");
                                    Log.d("HII", image);
                                }

                            }

                            Log.d("0800", "att" + image);
                           /* Picasso.with(UserActivity.this).load(image)
                                    .fit()
                                    .transform(new CircleTransform())
                                    .into(showImage);*/
                            ImageView showImage=findViewById(R.id.iv_personal_icon);
                            Glide.with(livephoto.this).load(image)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    .into(showImage);

                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }

                    }
                });
        DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics m=getResources().getDisplayMetrics();
        m2=m.density;
        l1 =(LinearLayout) findViewById(R.id.l1);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        w=(int)(135*m2);
        h=(int)(120*m2);

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
                            photo = new String[size+1][8];
                            Log.d("TAG", String.valueOf(size));
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        allphoto();

        textall=findViewById(R.id.textall);
        textall.setEnabled(false);
        textall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textall.setEnabled(false);
                textfriend.setEnabled(true);
                photo = new String[size+1][8];
                l1.removeAllViews();
                allphoto();
            }
        });
        textfriend=findViewById(R.id.textfriend);
        textfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textfriend.setEnabled(false);
                textall.setEnabled(true);
                photo = new String[size+1][8];
                l1.removeAllViews();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("friend").whereEqualTo("name",login.loginid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int i=0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("friend", document.getId() + " => " + document.getData());
                                        friendid[i]=document.getData().get("friend").toString();
                                        i++;
                                    }
                                    friendphoto();
                                } else {
                                    Log.w("TAG", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });


        // 設置點擊生活照事件
        findViewById(R.id.btn_life).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephoto.this, livephoto.class);
                startActivity(intent);
            }
        });
        // 設置點擊好友聊天事件
        findViewById(R.id.btn_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephoto.this, friend.class);
                startActivity(intent);
            }
        });
        // 設置點擊寵物翻譯事件
        findViewById(R.id.btn_tran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephoto.this, translate.class);
                startActivity(intent);
            }
        });
        // 設置點擊活動事件
        findViewById(R.id.btn_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(livephoto.this, active.class);
                startActivity(intent);
            }
        });
        // 設置點擊個人設置事件
        findViewById(R.id.btn_setup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephoto.this, setup.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.sharedPreferences.edit()
                        .putString("email","")
                        .putString("pwd","")
                        .putBoolean("ischecked",false)
                        .apply();
                Intent intent = new Intent(livephoto.this, login.class);
                startActivity(intent);
            }
        });


    }

    private long exittime=0;
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){

        }

        return false;
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
                    livephoto=true;
                    mypostlivephoto.mypost=false;
                    myfavoritelivephoto.myfavorite=false;
                    Intent fri=new Intent(livephoto.this,livephotopost.class);
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
                        livephoto=true;
                        mypostlivephoto.mypost=false;
                        myfavoritelivephoto.myfavorite=false;
                        Intent fri=new Intent(livephoto.this,livephotopost.class);
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

    public void allphoto(){
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
                                String collect=document.getData().get("collection").toString();
                                photo[count][0]=image;
                                photo[count][1]=commentid;
                                photo[count][2]=id;
                                photo[count][3]=text;
                                photo[count][4]=title;
                                photo[count][5]=photoid;
                                photo[count][6]=nowdate;
                                photo[count][7]=collect;
                            }
                            re();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void friendphoto(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("photo").orderBy("time")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "friendphoto";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            count=0;
                            for(int i=0;i<friendid.length;i++){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getData().get("id").toString().equals(friendid[i])) {
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
                                        String collect=document.getData().get("collection").toString();
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
                            }
                            re();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.livephoto, menu);
        note = menu.findItem(R.id.action_notifications);
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection("notice").whereEqualTo("uid",login.loginid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getBoolean("read")==false){
                                    // note.setVisibility(View.GONE);
                                    read=0;
                                    note.setIcon(R.drawable.note);
                                    System.out.println("read"+read);
                                }else{
                                    //notread.setVisibility(View.GONE);
                                    System.out.println("read"+read);
                                }
                            }

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.photorelease:
                Intent chang = new Intent(livephoto.this, livephotorelease.class);
                startActivity(chang);
                return true;
            case R.id.action_notifications:
                Intent chang3 = new Intent(livephoto.this, notice.class);
                startActivity(chang3);

                return true;

            case R.id.myphoto:
                Intent chang1 = new Intent(livephoto.this, mypostlivephoto.class);
                startActivity(chang1);
                return true;
            case R.id.mypreasure:
                Intent chang2 = new Intent(livephoto.this, myfavoritelivephoto.class);
                startActivity(chang2);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}