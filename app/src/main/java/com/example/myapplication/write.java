package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class write extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

       ImageButton fri= findViewById(R.id.back);
        fri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent fri=new Intent(write.this,photo.class);
                startActivity(fri);
            }

        });
    }
}
