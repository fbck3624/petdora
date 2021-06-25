package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class livephotorelease extends AppCompatActivity {
    private ImageView imageview;
    String data_list;
    int PICK_CONTACT_REQUEST=1;
    Uri uri;
    StorageReference storageReference,pic_storage;
    Boolean photouri=false;
    Button send;
    ProgressBar progressBar;
    final loading loading=new loading(livephotorelease.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livephotorelease);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText title=findViewById(R.id.title);
        final EditText text=findViewById(R.id.text);
        final String[] maxid = new String[1];
        storageReference= FirebaseStorage.getInstance().getReference();
        imageview=(ImageView)findViewById(R.id.imageView2);
//        progressBar=findViewById(R.id.progressBar);

        ImageButton image =(ImageButton) findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1);
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("photo").orderBy("commentid", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "commentid";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData().get("commentid").toString());
                                maxid[0] =document.getData().get("commentid").toString();
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        send =(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                send.setEnabled(false);
//                progressBar.setVisibility(View.VISIBLE);
                final String t1 = title.getText().toString();
                final String t2 = text.getText().toString();
                System.out.println(t1);
                System.out.println(t2);
                if (t1.equals("")) {
                    Toast.makeText(livephotorelease.this, "標題請勿空白", Toast.LENGTH_SHORT).show();
                } else if (t2.equals("")) {
                    Toast.makeText(livephotorelease.this, "內容請勿空白", Toast.LENGTH_SHORT).show();
                } else if(photouri==false){
                    Toast.makeText(livephotorelease.this, "請上傳圖片", Toast.LENGTH_SHORT).show();
                }
                else {
                    loading.StartDialog();
                    String rn=randomname();
                    final String photoname=rn+".jpg";
                    pic_storage=storageReference.child("photo/"+photoname);
                    pic_storage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(livephotorelease.this, "上傳成功", Toast.LENGTH_SHORT).show();
                            if(maxid[0]==null){
                                maxid[0]="0";
                            }

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> photo = new HashMap<>();
                            photo.put("id", login.loginid);
                            photo.put("image", "https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/photo%2F"+photoname+"?alt=media");
                            photo.put("title", t1);
                            photo.put("text", t2);
                            photo.put("commentid", (Integer.valueOf(maxid[0]) + 1));
                            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            photo.put("time", nowDate);
                            ArrayList collectid = new ArrayList();
                            photo.put("collection", collectid);
                            db.collection("photo")
                                    .add(photo)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        private static final String TAG = "test";

                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        private static final String TAG = "test";
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                            loading.dismisslog();
                            Intent fri = new Intent(livephotorelease.this, livephoto.class);
                            startActivity(fri);
                        }
                    });

                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        } else if (requestCode == PICK_CONTACT_REQUEST) {
                uri = data.getData();
                imageview.setImageURI(uri);
                ContentResolver contentResolver = getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                data_list = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
                photouri = true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}