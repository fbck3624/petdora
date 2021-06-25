package com.example.myapplication;

import android.app.AlarmManager;
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
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class ExampleService extends Service{
    private static final String TAG = "ExampleService";
    String time;
    private Calendar calendar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate() {
        Log.i(TAG, "ExampleService-onCreate");
//        Intent notificationIntent = new Intent(this, livephoto.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        Notification notification = new NotificationCompat.Builder(this, "default")
//                .setSmallIcon(R.drawable.loginlogo)
//                .setContentTitle("title")
//                .setContentText("text")
//                .setAutoCancel(true)
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
        //執行檔案的下載或者播放等操作
        AlarmManager manager =(AlarmManager) getSystemService(ALARM_SERVICE);

        calendar= Calendar.getInstance();
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int second =calendar.get(Calendar.SECOND);
        time=livephoto.time;
        if(TimesetupActivity.istimesetup){
            time=TimesetupActivity.time2;
        }
        Log.i(TAG, "ExampleService-time="+time);

        String t[]=time.split(":");

        int minutes=0;
        if(Integer.valueOf(t[0])>=hr){
            if(Integer.valueOf(t[1])>min) {
                minutes = (Integer.valueOf(t[0]) - hr) * 60 * 60 * 1000 + (Integer.valueOf(t[1]) - min) * 60 * 1000 - (second*1000);
                Log.i(TAG, "ExampleService-time="+minutes);
                long trigger= SystemClock.elapsedRealtime()+minutes;
                Log.i(TAG, "ExampleService-realtime="+trigger);

                Intent i = new Intent(ExampleService.this,AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(ExampleService.this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,trigger,pi);
            }else{

            }

        }else{

        }



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

        AlarmManager manager =(AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(ExampleService.this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ExampleService.this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pi);
    }

}
