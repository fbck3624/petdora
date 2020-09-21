package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class up extends AppCompatActivity {

    Button button;
    TextView tt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up);
        button=findViewById(R.id.button6);
        tt=findViewById(R.id.textView10);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               // tt.setText("2544");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> city = new HashMap<>();
                city.put("actid", 7);
                city.put("uid", 4);
                db.collection("actattend")
                        .document()//document可填可不填，更新或新增集合!
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

            }

        });


    }
}
