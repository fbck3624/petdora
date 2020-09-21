package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class see extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);
        ImageButton fri= findViewById(R.id.imageButton3);
        fri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent fri=new Intent(see.this,photo.class);
                startActivity(fri);
            }

        });

        ImageButton del= findViewById(R.id.imageButton12);
        del.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent fri=new Intent(see.this,photo.class);
                startActivity(fri);
            }

        });
    }
}
