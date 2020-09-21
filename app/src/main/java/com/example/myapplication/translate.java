package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class translate extends AppCompatActivity {
    private Button lan,voice,back;
    private ImageButton send,image2;
    private EditText input;
    private TextView text1;
    private ImageView image1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        input=(EditText)findViewById(R.id.input);
        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent change=new Intent(translate.this,photo.class);
                startActivity(change);            }
        });
        lan=(Button)findViewById(R.id.lan);
        lan.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                choose();
            }
        });
        text1=(TextView)findViewById(R.id.text1);
        image1=(ImageView)findViewById(R.id.image1);
        image2=(ImageButton)findViewById(R.id.image2);
        send=(ImageButton)findViewById(R.id.send);
        send.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                chat();
            }
        });
    }
    private void chat() {
        if(input.getText().equals("")==false) {
            text1.setText(input.getText());
            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            input.setText("");
        }
    }
    private void choose() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("請選擇寵物語言")
                .setPositiveButton("貓", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 撌行���瘜�
                        lan.setText("貓");
                    }
                })
                .setNegativeButton("狗", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // �����瘜�
                        lan.setText("狗");
                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
    }
}

