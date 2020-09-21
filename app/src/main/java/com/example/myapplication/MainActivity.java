package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pick= findViewById(R.id.button2);
        pick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(change);
            }

        });

        CardView card= findViewById(R.id.cardView);
        card.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent enroom = new Intent(MainActivity.this, Main4Activity.class);
                startActivity(enroom);
            }

        });


        CardView card1= findViewById(R.id.cardView1);
        card1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent enroom1 = new Intent(MainActivity.this, Main4Activity.class);
                startActivity(enroom1);
            }

        });
        CardView card2= findViewById(R.id.cardView2);
        card2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent enroom2 = new Intent(MainActivity.this, Main4Activity.class);
                startActivity(enroom2);
            }

        });
        CardView card3= findViewById(R.id.cardView4);
        card3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent enroom3 = new Intent(MainActivity.this, Main4Activity.class);
                startActivity(enroom3);
            }

        });
        Button test= findViewById(R.id.button1);
        test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent tes = new Intent(MainActivity.this, photo.class);
                startActivity(tes);
            }

        });
    }
}
