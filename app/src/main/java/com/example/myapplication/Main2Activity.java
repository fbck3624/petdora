package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageButton pick= findViewById(R.id.imageButton);
        pick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pick=new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(pick);
            }

        });
        ImageButton pick1= findViewById(R.id.imageButton3);
        pick1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pick1=new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(pick1);
            }

        });
        ImageButton pick2= findViewById(R.id.imageButton4);
        pick2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pick2=new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(pick2);
            }

        });
        ImageButton pick3= findViewById(R.id.imageButton5);
        pick3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent pick3=new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(pick3);
            }

        });
    }
}
