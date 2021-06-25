package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    EditText name,email,pwd,pwd2;
    Button register;
    TextView t1;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    static String[] maxid = new String[1];
    static String myid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").orderBy("uid", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("maxid", document.getId() + " => " + document.getData().get("uid").toString());
                                maxid[0] =document.getData().get("uid").toString();
                            }
                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }
                    }
                });

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        pwd=findViewById(R.id.pwd);
        pwd2=findViewById(R.id.pwd2);
        register=findViewById(R.id.register);
        t1=findViewById(R.id.textView);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
       // progressBar=findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();

//        if (fAuth.getCurrentUser()!=null){
//            startActivity(new Intent(getApplicationContext(),login.class));
//            finish();
//        }
        final loading loading=new loading(register.this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.StartDialog();
                String name1=name.getText().toString().trim();
                String email1=email.getText().toString().trim();
                String password=pwd.getText().toString().trim();
                String password2=pwd2.getText().toString().trim();

                if(TextUtils.isEmpty(name1)){
                    name.setError("請輸入暱稱!");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(email1)){
                    email.setError("請輸入email!");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    pwd.setError("請輸入密碼!");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    pwd2.setError("請再次輸入密碼!");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(password.length()<6){
                    pwd.setError("密碼長度需大於6個字!");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if(!password.equals(password2)){
                    pwd2.setError("密碼不一致!!");
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(email1,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email.getText().toString());
                                user.put("name", name.getText().toString());
                                user.put("uid", firebaseAuth.getUid());
                                // user.put("pwd",pwd.getText().toString() );
                                user.put("address", "");
                                ArrayList collectid = new ArrayList();
                                user.put("collection", collectid);
                                user.put("bir", "");
                                user.put("context", "");
                                user.put("notice", "12:00");
                                user.put("pet", "");
                                ArrayList petname = new ArrayList();
                                user.put("petname", petname);
                                user.put("photo", "");
                                user.put("sex", "");

                                db.collection("user")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            private static final String TAG = "test";

                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(register.this, "User created", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),registersetup.class));
                                                loading.dismisslog();
                                                myid=firebaseAuth.getUid();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            private static final String TAG = "test";
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });


                            }else {
                                Toast.makeText(register.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                //   progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }




             //   progressBar.setVisibility(View.VISIBLE);



            }
        });

    }
}
