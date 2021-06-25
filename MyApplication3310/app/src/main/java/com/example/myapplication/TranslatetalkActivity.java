package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TranslatetalkActivity extends AppCompatActivity {
    Button start,stop,enter,reflash;

    public static EditText talk;
    public static boolean pettalk=false;
    Boolean isstart;
    public static String outputFile = null;
    private List<translatetalk> test=new ArrayList<>();
    StorageReference storageReference,audio_storage;
    static String photoname;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    public static String dogemotion=null,wetalk=null;
    //  private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

    //   private PlayButton   playButton = null;
    public static MediaPlayer   player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    static final String SERVER_IP = "140.127.220.66"; // The SERVER_IP must be the same in server and client
    static int PORT ;
   // public static  Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translatetalk);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storageReference= FirebaseStorage.getInstance().getReference();


        System.out.println(outputFile);
//        myAudioRecorder = new MediaRecorder();
//        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        myAudioRecorder.setOutputFile(outputFile);

        reflash=findViewById(R.id.reflash);
        start=findViewById(R.id.petvoice);
        stop=findViewById(R.id.petvoice2);
        talk=findViewById(R.id.input);
        enter=findViewById(R.id.enter);
        System.out.println(translateFragment.people);
        isstart=false;
        if(translateFragment.people==true){
            start.setVisibility(View.GONE);
            stop.setVisibility(View.GONE);
            reflash.setVisibility(View.GONE);
            pettalk=true;

        }
        else{

            pettalk=false;
            talk.setVisibility(View.GONE);
            stop.setVisibility(View.GONE);
            enter.setVisibility(View.GONE);
            reflash.setVisibility(View.GONE);
        }
        start.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                PORT=1234;
                boolean mStartRecording = true;
                System.out.println(mStartRecording);
                System.out.println("startclick");
                stop.setVisibility(View.VISIBLE);
                start.setVisibility(View.GONE);
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
                isstart=true;
                test.clear();


            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogemotion=null;
                stop.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                stopRecording();
                onStop();

                pettalk=true;
                isstart=false;

                test.clear();
                while (dogemotion==null) {
                    //     loading.StartDialog();
                    System.out.println("wait");
                    if(dogemotion!=null){
                        //      loading.dismisslog();
                        System.out.println("break");
                        break;
                    }
                }

                lay();
              //  reflash.setVisibility(View.VISIBLE);



            }
        });
        enter.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                PORT=1200;
                if(!talk.getText().toString().trim().isEmpty() && talk.getText().toString()!=""){
                    dogemotion=null;
                    pettalk=false;
                    System.out.println(talk.getText().toString());
                   tran();

                    while (dogemotion==null) {
                        //     loading.StartDialog();
                       System.out.println("wait");
                        if(dogemotion!=null){
                            //      loading.dismisslog();
                            lay();
                            System.out.println("break");
                            break;
                        }
                    }

                  //  reflash.setVisibility(View.VISIBLE);
//                    talk.setVisibility(View.GONE);
//                    enter.setVisibility(View.GONE);
                    System.out.println("talk!!");
                }
            }
        });
//        reflash.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent pick=new Intent(TranslatetalkActivity.this,translateFragment.class);
//                startActivity(pick);
//            }
//        });

        // Record to the external cache directory for visibility
        outputFile = getExternalCacheDir().getAbsolutePath();
        outputFile += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


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


    public void tran(){
        ConnectPyTask task = new ConnectPyTask();
        ConnectPyTask.context = getApplicationContext();
        task.execute(talk.getText().toString());//傳送data過去
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public static void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    public static void startPlaying() {
        player = new MediaPlayer();
        try {
            MediaPlayer m = new MediaPlayer();
            //透過網址就能播放在storage中的音檔
            m.setDataSource("https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/audio%2F"+photoname+"?alt=media");
//        m.setDataSource(outputFile);
            m.prepare();
            m.start();
         /*   player.setDataSource(fileName);
            player.prepare();
            player.start();*/
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public static void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(outputFile);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        String rn=randomname();
        photoname=rn+".mp3";
        audio_storage=storageReference.child("audio/"+photoname);
        Uri file = Uri.fromFile(new File(outputFile));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/mpeg")
                .build();
        UploadTask uploadTask = storageReference.child("audio/"+photoname).putFile(file, metadata);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
//                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
            }
        });
        ConnectPyTask task = new ConnectPyTask();
        ConnectPyTask.context = getApplicationContext();
        task.execute(photoname);//傳送data過去

        recorder = null;
    }


    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }



    public void lay(){
        initBooks();
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.id_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(TranslatetalkActivity.this);  //LinearLayoutManager中定製了可擴充套件的佈局排列介面，子類按照介面中的規範來實現就可以定製出不同排雷方式的佈局了
//        layoutManager.setStackFromEnd(true);//顯示在底部
        //配置佈局，預設為vert.ical（垂直佈局），下邊這句將佈局改為水平佈局
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        translateAdapter adapter=new translateAdapter(test);
        recyclerView.setAdapter(adapter);
    }

    private void initBooks(){
        if(isstart==false) {
            translatetalk c = new translatetalk("1", talk.getText().toString(), "1", "1");
            talk.setText("");
            test.add(c);
        }
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
    static class ConnectPyTask extends AsyncTask<String, Void, String>
    {
        public static  Context context = null;
        public float startTime = 0, endTime = 0;
        @Override
        protected String doInBackground(String... data) {
            try {
                dogemotion=null;
                startTime = System.currentTimeMillis();
                System.out.println(PORT);
                Socket socket = new Socket(SERVER_IP, PORT); //Server IP and PORT

                Scanner sc = new Scanner(socket.getInputStream());
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                // System.out.println(data[0]);
                printWriter.write(data[0]); // Send Data
                printWriter.flush();
                dogemotion=sc.next();
                System.out.println("emotion:"+dogemotion);



               // socket.close();

                //  System.out.println(x);
            }catch (Exception e){

                Log.d("Exception", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            endTime = System.currentTimeMillis();
            String execTime = String.valueOf((endTime - startTime)/1000.0f);
            Toast.makeText(context, "Time execution is: " + execTime + "s", Toast.LENGTH_SHORT).show();
        }
    }


}