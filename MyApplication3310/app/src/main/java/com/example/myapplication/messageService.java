package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.textclassifier.TextSelection;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class messageService extends Service{
    private static final String TAG = "ExampleService";
    private String talkroom[]=new String[1000];
    private String talkdata[][]=new String[1000][3];
    int i=0,j=0;
    boolean isidexist=false;
    static boolean isfirst=true;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate() {
        Log.i(TAG, "ExampleService-onCreate");
        isfirst=true;
//        Intent notificationIntent = new Intent(this, livephoto.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        Notification notification = new NotificationCompat.Builder(this, "default")
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.loginlogo)
//                .setContentTitle("title")
//                .setContentText("text")
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setWhen(System.currentTimeMillis())
//                .setContentIntent(pendingIntent)
//                .build();
//        startForeground(0, notification);
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "ExampleService-onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                //執行檔案的下載或者播放等操作
                db.collection("friend").whereEqualTo("name",login.loginid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @RequiresApi(api = Build.VERSION_CODES.O_MR1)
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        talkroom[i]=document.getData().get("talkroom").toString();
                                        Log.d("ExampleService", talkroom[i]);
                                        i++;
                                    }
                                    db.collection("group").whereArrayContains("name",login.loginid)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                @RequiresApi(api = Build.VERSION_CODES.O_MR1)
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            talkroom[i]=document.getData().get("talkroom").toString();
                                                            Log.d("ExampleService", talkroom[i]);
                                                            i++;
                                                        }
                                                        gettalk2();
                                                    } else {
                                                        Log.w("000", "Error getting documents.", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.w("000", "Error getting documents.", task.getException());
                                }

                            }
                        });
//            }
//        }.start();




        /*
         * 這裡返回狀態有三個值，分別是:
         * 1、START_STICKY：當服務程序在執行時被殺死，系統將會把它置為started狀態，但是不儲存其傳遞的Intent物件，之後，系統會嘗試重新建立服務;
         * 2、START_NOT_STICKY：當服務程序在執行時被殺死，並且沒有新的Intent物件傳遞過來的話，系統將會把它置為started狀態，
         *   但是系統不會重新建立服務，直到startService(Intent intent)方法再次被呼叫;
         * 3、START_REDELIVER_INTENT：當服務程序在執行時被殺死，它將會在隔一段時間後自動建立，並且最後一個傳遞的Intent物件將會再次傳遞過來。
         */
        return START_REDELIVER_INTENT;

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "ExampleService-onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "ExampleService-onDestroy");
        super.onDestroy();
    }

    private void gettalk2(){
        db.collection("talk")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                        for (int x=0;x<talkroom.length;x++) {
                                            if (talkroom[x]==null){
                                                break;
                                            }
                                            if (dc.getDocument().getData().get("talkroom").toString().equals(talkroom[x])) {
                                                if(!dc.getDocument().getData().get("name").toString().equals(login.loginid)
                                                        && !dc.getDocument().getData().get("read").toString().contains(login.loginid)){
                                                    final String talk=dc.getDocument().getData().get("talk").toString();
                                                    if(talk.contains("is已加入群組") || talk.contains("is已退出群組")){

                                                    }else {
                                                        db.collection("user").whereEqualTo("uid", dc.getDocument().getData().get("name").toString())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                            if(document.getData().get("read").toString().contains(login.loginid)) {
                                                                                if (isfirst == false) {
                                                                                    Log.d(TAG, "isfirst: " + isfirst);
                                                                                    String name = document.getData().get("name").toString();
                                                                                    notification(name, talk);
                                                                                }
//                                                                            }
                                                                            }
                                                                        } else {
                                                                            Log.w("TAG", "Error getting documents.", task.getException());
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                    }
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }


//                        List<String> cities = new ArrayList<>();
//                        for (QueryDocumentSnapshot doc : value) {
//                            if (doc.getId() != null) {
//                                cities.add(doc.getId());
//                                for (int x=0;x<talkroom.length;x++) {
//                                if (talkroom[x]==null){
//                                    break;
//                                }
//                                if (doc.get("talkroom").toString().equals(talkroom[x])) {
//                                    if(doc.get("name").toString()!=login.loginid){
//                                        final String talk=doc.get("talk").toString();
//                                        db.collection("user").whereEqualTo("uid",doc.get("name").toString())
//                                                .get()
//                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                        if (task.isSuccessful()) {
//                                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                String name=document.getData().get("name").toString();
//                                                                notification(name, talk);}
//                                                        } else {
//                                                            Log.w("TAG", "Error getting documents.", task.getException());
//                                                        }
//                                                    }
//                                                });
//                                    }
//                                }
//                            }
//
//                            }
//                        }
//                        Log.d(TAG, "Current cites: " + cities);
                    }
                });

        isfirst=false;
        livephoto.isfirst=false;
    }

//    private void gettalk(){
//        for (int x=0;x<talkroom.length;x++){
//            if (talkroom[x]==null){
//                break;
//            }
//            Log.i(TAG, "ExampleService-gettalk"+talkroom[x]);
//
//            db.collection("talk").whereEqualTo("talkroom",talkroom[x])
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                Log.i(TAG, "ExampleService-gettalk2222");
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    if (!document.getData().get("name").toString().equals(login.loginid)) {
//                                        talkdata[j][0] = document.getData().get("name").toString();
//                                        talkdata[j][1] = document.getData().get("talk").toString();
//                                        talkdata[j][2] = document.getId();
//                                        Log.d("ExampleService", talkdata[j][2]);
//                                        j++;
//                                    }
//                                }
//                                check();
//                            } else {
//                                Log.w("000", "Error getting documents.", task.getException());
//                            }
//                        }
//                    });
//
//
//        }
//    }
//
//    private void check(){
//        Thread thread = new Thread() {
//            public void run() {
//                while (true) {
//                    for (int x = 0; x < talkroom.length; x++) {
//                        if (talkroom[x] == null) {
//                            break;
//                        }
//                        db.collection("talk").whereEqualTo("talkroom", talkroom[x])
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            Log.i(TAG, "ExampleService-gettalk onStartCommand");
//                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                if (!document.getData().get("name").toString().equals(login.loginid)) {
//                                                    for (int a = 0; a < talkdata.length; a++) {
//                                                        if (talkdata[a][2] == null) {
//                                                            break;
//                                                        }
//                                                        if (document.getId().equals(talkdata[a][2])) {
//                                                            isidexist = true;
//                                                            break;
//                                                        } else {
//                                                            isidexist = false;
//                                                        }
//                                                    }
//
//                                                    if (!isidexist) {
//                                                        final String talk=document.getData().get("talk").toString();
//                                                        db.collection("user").whereEqualTo("uid",document.getData().get("name").toString())
//                                                                .get()
//                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                                        if (task.isSuccessful()) {
//                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                                String name=document.getData().get("name").toString();
//                                                                                notification(name, talk);}
//                                                                        } else {
//                                                                            Log.w("TAG", "Error getting documents.", task.getException());
//                                                                        }
//                                                                    }
//                                                                });
//                                                        talkdata[j][0] = document.getData().get("name").toString();
//                                                        talkdata[j][1] = document.getData().get("talk").toString();
//                                                        talkdata[j][2] = document.getId();
//                                                        Log.d("ExampleService", talkdata[j][2]);
//                                                        j++;
//                                                    }
//                                                }
//                                            }
//
//                                        } else {
//                                            Log.w("000", "Error getting documents.", task.getException());
//                                        }
//                                    }
//                                });
//                    }
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        thread.start();
//
//    }
    private void notification(String title,String text) {
        Intent intent = new Intent(this, friend.class);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "default";
            String channelName = "默認通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(friend.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.loginlogo)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();

        manager.notify(1, notification);
    }
}

