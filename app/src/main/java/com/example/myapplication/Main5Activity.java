package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);


        CardView post= findViewById(R.id.cardView6);
        post.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent po=new Intent(Main5Activity.this,Main7Activity.class);
                startActivity(po);
            }

        });

        Button tra= findViewById(R.id.translate1);
        tra.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(Main5Activity.this,translate.class);
                startActivity(change);
            }

        });

        Button act= findViewById(R.id.active1);
        act.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(Main5Activity.this,fragment.class);
                startActivity(change);
            }

        });

        Button home= findViewById(R.id.first1);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent fri=new Intent(Main5Activity.this,photo.class);
                startActivity(fri);
            }

        });

        Button friend= findViewById(R.id.best1);
        friend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent fri=new Intent(Main5Activity.this,MainActivity.class);
                startActivity(fri);
            }

        });
    }
}
