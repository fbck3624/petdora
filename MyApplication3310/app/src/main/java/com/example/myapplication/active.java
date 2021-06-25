package com.example.myapplication;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.slideshow.SlideshowFragment;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class active extends AppCompatActivity {
    boolean photoIsOn = true;
    boolean activeIsOn=false;
    private AppBarConfiguration mAppBarConfiguration;
    String image;
    TextView name;
    static String ID;
    int read=1;
    static MenuItem note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTheme(R.style.AppThemetwo);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        name=findViewById(R.id.name);
        postwriting.update=false;
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
                            Glide.with(active.this).load(image)
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

                Intent intent = new Intent(active.this, livephoto.class);
                startActivity(intent);
            }
        });
        // 設置點擊好友聊天事件
        findViewById(R.id.btn_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(active.this, friend.class);
                startActivity(intent);
            }
        });
        // 設置點擊寵物翻譯事件
        findViewById(R.id.btn_tran).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(active.this, translate.class);
                startActivity(intent);
            }
        });
        // 設置點擊活動事件
        findViewById(R.id.btn_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(active.this, active.class);
                startActivity(intent);
            }
        });
        // 設置點擊個人設置事件
        findViewById(R.id.btn_setup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(active.this, setup.class);
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
                Intent intent = new Intent(active.this, login.class);
                startActivity(intent);
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(active.this, livephoto.class);
            startActivity(intent);
        }
        return false;
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//
//        getMenuInflater().inflate(R.menu.active, menu);
//
//        note = menu.findItem(R.id.action_notifications);
//
//        FirebaseFirestore data = FirebaseFirestore.getInstance();
//        data.collection("notice").whereEqualTo("uid",login.loginid)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if(document.getBoolean("read")==false){
//                                    // note.setVisibility(View.GONE);
//                                    read=0;
//                                    System.out.println("read"+read);
//                                    note.setIcon(R.drawable.note);
//
//
//                                }else{
//                                    //notread.setVisibility(View.GONE);
//                                    System.out.println("read"+read);
//                                }
//                            }
//
//                        } else {
//                            Log.w("TAG", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent chang = new Intent(active.this, postwriting.class);
                startActivity(chang);
                return true;

            case R.id.mypost:
                System.out.println("mypost");
                Intent change1 = new Intent(active.this, mypostact.class);
                startActivity(change1);
                return true;
            case R.id.meattend:
                System.out.println("meattend");
                Intent change = new Intent(active.this, myattendact.class);
                startActivity(change);
                return true;
            case R.id.myfavoritepost:
                System.out.println("myfavoritepost");
                Intent change2 = new Intent(active.this, myfavoritepost.class);
                startActivity(change2);
                return true;
            case R.id.action_notifications:
             //   System.out.println("這是活動");
                Intent chang3 = new Intent(active.this, notice.class);
                startActivity(chang3);
                return true;

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

