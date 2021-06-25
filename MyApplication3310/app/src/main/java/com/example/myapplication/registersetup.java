package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class registersetup extends AppCompatActivity {
    int Preset=0,whatsex;
    FirebaseFirestore data;
    TextView gender,birthday;
    EditText name,context;
    ImageView iv_personal_icon;
    Button button;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    public static final int CROP_PHOTO = 2;
    private ImageView showImage;
    String ID,image,uuid;
    StorageReference storageRef,mountainsRef,storageReference;
//    private Uri imageUri;
//    private static final int RESULT = 1;
//    private String filename;
//    Intent intent;
//    boolean mCircleSeparator = false;
//    private TextView lbl_imgpath;
    String data_list;
    int PICK_CONTACT_REQUEST=1;
    Uri uri;
    StorageReference pic_storage;
    Boolean photouri=false;
    ProgressBar progressBar;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registersetup);
//        showImage = (ImageView) findViewById(R.id.iv_personal_icon);
        storageReference = FirebaseStorage.getInstance().getReference();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calendar=Calendar.getInstance();

        iv_personal_icon = findViewById(R.id.iv_personal_icon);
        name=findViewById(R.id.name);
        context=findViewById(R.id.context);
        gender=findViewById(R.id.gender);
        birthday = findViewById(R.id.birthday);
        progressBar=findViewById(R.id.progressBar);

//        uuid = UUID.randomUUID().toString();
//        final FirebaseStorage storage = FirebaseStorage.getInstance("gs://lalala-c7bcf.appspot.com");//最外面的網址
//        storageRef  = storage.getReference("user");//資料夾!
//        StorageReference spaceRef = storageRef.child("images/"+uuid);
//        String path = spaceRef.getPath();
//        mountainsRef = storageRef.child(uuid+".jpg");
        data = FirebaseFirestore.getInstance();

        data.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d("HII", document.getId() + " => " + document.getLong("actid"));
                                //if(login.loginid==document.getLong("uid")){
                                if(document.getString("uid").equals(register.myid)){
                                    ID=document.getId();
                                    name.setText(document.getString("name"));
                                }

                            }

                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }

                    }
                });


        iv_personal_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent,1);
            }

        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] strings={"男","女"};

                AlertDialog.Builder builder=new AlertDialog.Builder(registersetup.this);
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
                        dialog.dismiss();
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


        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String name1 = name.getText().toString();
                final String gender1 = gender.getText().toString();
                final String birthday1 = birthday.getText().toString();
                final String context1 = context.getText().toString();

                if (name1.equals("")) {
                    Toast.makeText(registersetup.this, "暱稱請勿空白", Toast.LENGTH_SHORT).show();
                } else if (gender1.equals("")) {
                    Toast.makeText(registersetup.this, "性別請勿空白", Toast.LENGTH_SHORT).show();
                }else if (birthday1.equals("")) {
                    Toast.makeText(registersetup.this, "生日請勿空白", Toast.LENGTH_SHORT).show();
                }else if (context1.equals("")) {
                    Toast.makeText(registersetup.this, "簡介請勿空白", Toast.LENGTH_SHORT).show();
                } else if(photouri==false){
                    Toast.makeText(registersetup.this, "請上傳頭像", Toast.LENGTH_SHORT).show();
                }
                else {
                    final loading loading=new loading(registersetup.this);
                  // progressBar.setVisibility(View.VISIBLE);
                    loading.StartDialog();
                    String rn=randomname();
                    final String photoname=rn+".jpg";
                    pic_storage=storageReference.child("user/"+photoname);
                    pic_storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(registersetup.this, "上傳成功", Toast.LENGTH_SHORT).show();
                            //圖片上傳需要一點時間 直接跳轉會來不及顯示讀片
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> user = new HashMap<>();
                            user.put("bir", date);
                            user.put("context",context1);
                            user.put("photo", "https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/user%2F"+photoname+"?alt=media");
                            user.put("sex", gender1);
                            user.put("name", name1);

                            db.collection("user").document(ID)
                                    .set(user, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Toast.makeText(registersetup.this, "更新成功", Toast.LENGTH_SHORT).show();
                                            loading.dismisslog();
                                            Intent fri = new Intent(registersetup.this, login.class);
                                            startActivity(fri);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(registersetup.this, "更新失敗", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });

                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
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
        } else if(requestCode==PICK_CONTACT_REQUEST){
            uri=data.getData();
//            iv_personal_icon.setImageURI(uri);
            Glide.with(registersetup.this).load(uri)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(iv_personal_icon);
            ContentResolver contentResolver=getContentResolver();
            MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
            data_list=mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
            photouri=true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showPicture(String img_uri) {
        showImage.setImageBitmap(BitmapFactory.decodeFile(img_uri));
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
}
