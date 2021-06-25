package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Petsetup extends AppCompatActivity {
    int Preset=0,whatsex;
    FirebaseFirestore data;
    TextView nameID,gender,birthday,introduction;
    EditText setname,setintroduction;
    ImageView imageview;
    ImageButton im;
    Button name,gen,bir,in,nameok,inok;
    String ID,uuid;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Date date;
    String newname,url;
    StorageReference mountainsRef,storageReference,pic_storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petsetup);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();

        uuid = UUID.randomUUID().toString();
        storageReference= FirebaseStorage.getInstance().getReference();
        data = FirebaseFirestore.getInstance();

        final String petname=petAdapter.petname;
        final loading loading=new loading(Petsetup.this);
        gender=findViewById(R.id.gender);
        birthday=findViewById(R.id.birthday);

      //  imageview=findViewById(R.id.imageview);

     //   inok=findViewById(R.id.inok);

        setname=findViewById(R.id.setname);
        setname.setVisibility(View.VISIBLE);
       // setname.setVisibility(View.GONE);
        setintroduction=findViewById(R.id.setintroduction);
       // setintroduction.setVisibility(View.GONE);
        im=findViewById(R.id.im);
        data.collection("pet")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("nameID").equals(petname)){
                                    ID= document.getId();
                                    setname.setText(document.getString("nameID"));
                                    gender.setText(document.getString("gender"));
                                    Date birth=document.getDate("birthday");
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                    birthday.setText(sdf.format(birth));
                                    setintroduction.setText(document.getString("introduction"));
                                    /*Picasso.with(PetsetupActivity.this).load(document.getString("image"))
                                            .placeholder(R.mipmap.ic_launcher)
                                            .into(imageview);*/
                                    url=document.getString("image");
                                    Glide.with(Petsetup.this).load(url)
                                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                            .into(im);
                                }
                            }
                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }

                    }
                });

        gen=findViewById(R.id.gen);
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] strings={"男","女"};

                AlertDialog.Builder builder=new AlertDialog.Builder(Petsetup.this);
                builder.setSingleChoiceItems(strings, Preset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        whatsex=which;//把預設值改成選擇的
                        Log.d("22", String.valueOf(Preset));


                    }
                });
                builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(whatsex==0){
                            gender.setText("男");
                        }else if(whatsex==1){
                            gender.setText("女");
                        }
                        dialog.dismiss();//結束對話框

                    }
                });
                builder.show();

            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalenderDialog();
            }
        });


        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent,1);
            }

        });
        Button submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.StartDialog();
                Map<String, Object> update = new HashMap<>();
                update.put("nameID",setname.getText().toString());
                update.put("gender",gender.getText().toString());
                date = new Date();
//注意format的格式要與日期String的格式相匹配
                DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    date = sdf.parse(birthday.getText().toString());
                    System.out.println(date.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                update.put("birthday",date);
                update.put("introduction",setintroduction.getText().toString());
                update.put("image",url);
                data.collection("pet").document(ID)
                        .set(update, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(Petsetup.this, "更新成功", Toast.LENGTH_SHORT).show();
                                loading.dismisslog();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Petsetup.this, "更新失敗", Toast.LENGTH_SHORT).show();
                            }
                        });
                Intent intent = new Intent(Petsetup.this, PetchooseActivity.class);
                startActivity(intent);

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
        switch (item.getItemId()){
            case R.id.pet_delete:
                choose();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCalenderDialog(){
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String ca = year + "/" + (month + 1) + "/" + dayOfMonth ;
                birthday.setText(ca);
                Log.e("TAG", "calender : " + ca);

                date = new Date();
//注意format的格式要與日期String的格式相匹配
                DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    date = sdf.parse(birthday.getText().toString());
                    System.out.println(date.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                calendar.setTime(date);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        } else if(requestCode==1){
            loading.StartDialog();
            Uri uri=data.getData();
            newname=randomname();
            url="https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/pet%2F"+newname+".jpg"+"?alt=media";
            //final String newurl="https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/pet%2F"+newname+".jpg"+"?alt=media";
            mountainsRef=storageReference.child("pet/"+newname+".jpg");
            mountainsRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loading.dismisslog();
                    Glide.with(Petsetup.this).load(url)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(im);
                }
            });


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    protected String randomname(){
        int z;
        StringBuilder sb =new StringBuilder();
        int i;
        for(i=0;i<10;i++){
            z=(int)((Math.random()*7)%3);
            if(z==1){
                sb.append((int)(Math.random()*10)+48);
            }else if(z==2){
                sb.append((char)((Math.random()*26)+65));
            }else{
                sb.append((char)((Math.random()*26)+97));
            }
        }
        return sb.toString();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.petsetup, menu);
        return true;
    }

    private void choose() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("是否確認刪除寵物?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("pet").document(ID)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(Petsetup.this, "刪除成功", Toast.LENGTH_SHORT).show();
                                        Intent fri=new Intent(Petsetup.this,PetchooseActivity.class);
                                        startActivity(fri);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
    }

}