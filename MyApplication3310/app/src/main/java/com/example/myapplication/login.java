package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class login extends AppCompatActivity  {
    Button login;
    EditText email;
    EditText pwd;
    static String[] user_email;
    static String[] user_pwd;
    public static String[] name;
    static String[] user_photo;
    public static Long[] uid;
    public static String loginid;
    static String loginname,loginphoto;
    public static int i; //資料庫長度
    FirebaseAuth firebaseAuth;
    public static SharedPreferences sharedPreferences;
    private FirebaseAuth.AuthStateListener mAthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_email=new String[1000];
        user_pwd=new String[1000];
        user_photo=new String[1000];
        uid=new Long[1000];
        name=new String[1000];
        email=findViewById(R.id.email);
        pwd=findViewById(R.id.pwd);
        login=findViewById(R.id.login);
        firebaseAuth=FirebaseAuth.getInstance();
        final loading loading=new loading(login.this);
        livephoto.isfirst=true;

        sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
//        sharedPreferences.edit()
//                .putString("email","")
//                .putString("pwd","")
//                .putBoolean("ischecked",false)
//                .apply();
        if (sharedPreferences.getBoolean("ischecked",false)){
            loading.StartDialog();
            String myemail=sharedPreferences.getString("email","");
            String mypwd=sharedPreferences.getString("pwd","");
            firebaseAuth.signInWithEmailAndPassword(myemail,mypwd).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println(firebaseAuth.getUid());
                        loginid=firebaseAuth.getUid();
                        Toast.makeText(login.this,"登入成功!歡迎!",Toast.LENGTH_SHORT).show();
                        loading.dismisslog();
                        Intent i=new Intent(login.this, livephoto.class);
                        startActivity(i);
                }
            });
        }


        TextView register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(login.this, register.class);
                startActivity(change);
            }

        });
        TextView forget = findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(login.this, passwdsetup.class);
                startActivity(change);
            }

        });
        mAthStateListener=new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    //  Toast.makeText(login.this,"HII",Toast.LENGTH_SHORT).show();
                    // Intent i=new Intent(login.this,MainActivity4.class);
                    // startActivity(i);
                }else{
                    // Toast.makeText(login.this,"QQ",Toast.LENGTH_SHORT).show();
                }
            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myemail=email.getText().toString();
                String mypwd=pwd.getText().toString();
                //String confirm=confirmpwd.getText().toString();
                if(myemail.isEmpty() ){
                    email.setError("請輸入email");
                    email.requestFocus();
                }else if(mypwd.isEmpty()){
                    pwd.setError("請輸入密碼");
                    pwd.requestFocus();
                }else if(!myemail.isEmpty()&&!mypwd.isEmpty()){
                    loading.StartDialog();
                    firebaseAuth.signInWithEmailAndPassword(myemail,mypwd).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(login.this,"登入失敗,請確認帳密是否有誤",Toast.LENGTH_SHORT).show();
                                loading.dismisslog();
                            }else{
                                sharedPreferences.edit()
                                        .putString("email",email.getText().toString())
                                        .putString("pwd",pwd.getText().toString())
                                        .putBoolean("ischecked",true)
                                        .apply();
                                System.out.println(firebaseAuth.getUid());
                                loginid=firebaseAuth.getUid();
                                loading.dismisslog();
                                Toast.makeText(login.this,"登入成功!歡迎!",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(login.this, livephoto.class);
                                startActivity(i);
                            }
                        }
                    });
                }else{
                    Toast.makeText(login.this,"錯誤!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(mAthStateListener);
    }


}

