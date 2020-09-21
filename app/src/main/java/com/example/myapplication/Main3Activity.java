package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Button ba= findViewById(R.id.button);
        ba.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent home = new Intent(Main3Activity.this, MainActivity.class);
                startActivity(home);
            }

        });

        Button batalk= findViewById(R.id.buttontalk);
        batalk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent talkroom = new Intent(Main3Activity.this, Main4Activity.class);
                startActivity(talkroom);
            }

        });
    }
}
