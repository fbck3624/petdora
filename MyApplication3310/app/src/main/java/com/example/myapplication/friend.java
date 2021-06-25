package com.example.myapplication;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class friend extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    String image;
    TextView name;
    static String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.friend_list, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                            Glide.with(friend.this).load(image)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    .into(showImage);

                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }

                    }
                });
        // 設置點擊生活照事件
        findViewById(R.id.btn_life).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(friend.this, livephoto.class);
                startActivity(intent);
            }
        });
        // 設置點擊好友聊天事件
        findViewById(R.id.btn_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(friend.this, friend.class);
                startActivity(intent);
            }
        });
        // 設置點擊寵物翻譯事件
        findViewById(R.id.btn_tran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(friend.this, translate.class);
                startActivity(intent);
            }
        });
        // 設置點擊活動事件
        findViewById(R.id.btn_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(friend.this, active.class);
                startActivity(intent);
            }
        });
        // 設置點擊個人設置事件
        findViewById(R.id.btn_setup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(friend.this, setup.class);
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
                Intent intent = new Intent(friend.this, login.class);
                startActivity(intent);
            }
        });

//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(friend.this, livephoto.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}