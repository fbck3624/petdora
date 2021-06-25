package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.R.layout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class livephotoall extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    String image;
    TextView name;
    static String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livephotoall);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_livephoto, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.inflateHeaderView(R.layout.nav_header_active);
////        navigationView.inflateMenu(R.menu.activity_active_drawer);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
//        View navHeaderView = navigationView.getHeaderView(0);

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
                                if(document.getLong("uid")==1){
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
                            Glide.with(livephotoall.this).load(image)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                    .into(showImage);

                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }

                    }
                });
        findViewById(R.id.textfriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephotoall.this, livephoto.class);
                startActivity(intent);
            }
        });
        // 設置點擊生活照事件
        findViewById(R.id.btn_life).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephotoall.this, livephoto.class);
                startActivity(intent);
            }
        });
        // 設置點擊好友聊天事件
        findViewById(R.id.btn_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephotoall.this, friend.class);
                startActivity(intent);
            }
        });
        // 設置點擊寵物翻譯事件
        findViewById(R.id.btn_tran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephotoall.this, translate.class);
                startActivity(intent);
            }
        });
        // 設置點擊活動事件
        findViewById(R.id.btn_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(livephotoall.this, active.class);
                startActivity(intent);
            }
        });
        // 設置點擊個人設置事件
        findViewById(R.id.btn_setup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(livephotoall.this, setup.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.livephoto, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.photorelease:
                Intent chang = new Intent(livephotoall.this, livephotorelease.class);
                startActivity(chang);
                return true;


            case R.id.myphoto:
                return true;
            case R.id.mypreasure:



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
