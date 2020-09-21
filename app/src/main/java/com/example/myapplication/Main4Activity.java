package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Button bab =findViewById(R.id.buttongo);
        bab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent babc = new Intent(Main4Activity.this, MainActivity.class);
                startActivity(babc);
            }

        });
    }
}
