package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Main7Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        ImageButton back= findViewById(R.id.imageButton11);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent per=new Intent(Main7Activity.this,Main5Activity.class);
                startActivity(per);
            }

        });

        ImageButton add= findViewById(R.id.imageView15);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent per=new Intent(Main7Activity.this,write.class);
                startActivity(per);
            }

        });

        ImageButton pic= findViewById(R.id.imageView14);
        pic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent per=new Intent(Main7Activity.this,see.class);
                startActivity(per);
            }

        });

        ImageButton pic1= findViewById(R.id.imageView13);
        pic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent per=new Intent(Main7Activity.this,see.class);
                startActivity(per);
            }

        });
    }
}
