package com.example.myapplication;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class translateAdapter extends RecyclerView.Adapter<translateAdapter.ViewHolder>{
    private List<translatetalk> mfList;
    String nowtime="0";
    public static String friendname;
    public String[] emotion={"喜","怒","哀"};
    public String[] happy={"我想出去玩","最喜歡主人了","好開心","好舒服，繼續","我想吃零食","耶~要出門了","好棒，吃飯了","我還要玩","媽媽回來了","走帶我去散步吧"};
    public String[] angry={"你想做甚麼!!","這是我的地盤","我生氣了","不要搶我東西","肚子餓了，我的食物呢?","小心被我咬","外面有狀況","不要煩我","都不跟我玩","不要靠近我"};
    public String[] cry={"陪我玩嘛","我好傷心喔","我還想要玩","我還不想回家","想睡覺了","不要走啦繼續玩嘛","都沒有人理我","不要欺負我","好累喔，肚子好餓","我想找媽媽"};
    int random,random2 ;
    public static MediaPlayer player = null;
    public String filepath;
    String Path="main/res/drawable/h.jpg";

    static class ViewHolder extends RecyclerView.ViewHolder{
        static TextView say,say2;

        public ViewHolder(View view){
            super(view);
            say=view.findViewById(R.id.say);
            say2=view.findViewById(R.id.say2);


        }
    }

    public translateAdapter(List<translatetalk> bookList){
        mfList=bookList;
    }
    public translateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.translate,parent,false);
        RecyclerView.ViewHolder holder=new translateAdapter.ViewHolder(view);
        return (translateAdapter.ViewHolder) holder;
    }


    public void onBindViewHolder(final translateAdapter.ViewHolder holder, int position) {
        final translatetalk talk=mfList.get(position);
        random = (int) (Math.random()*3);

        if(TranslatetalkActivity.pettalk==true){
           // holder.say.setText("pls wait");
            while (TranslatetalkActivity.dogemotion==null){

                System.out.println("等待");
            }
            if(TranslatetalkActivity.dogemotion.equals("happy")){
                random2 = (int) (Math.random()*4);

                holder.say.setBackgroundResource(R.drawable.ic_play_circle);
                holder.say2.setText("寵物的情緒是"+ TranslatetalkActivity.dogemotion+"\n"+happy[random2]);
                 TranslatetalkActivity.dogemotion=null;
                System.out.println("dogsay2"+ TranslatetalkActivity.dogemotion);
            }else if(TranslatetalkActivity.dogemotion.equals("mad")){

                random2 = (int) (Math.random()*4);
                holder.say.setBackgroundResource(R.drawable.ic_play_circle);
                holder.say2.setText("寵物的情緒是"+TranslatetalkActivity.dogemotion+"\n"+angry[random2]);
                 TranslatetalkActivity.dogemotion=null;
                System.out.println("dogsay2"+ TranslatetalkActivity.dogemotion);
            }else {
                random2 = (int) (Math.random()*4);
                holder.say.setBackgroundResource(R.drawable.ic_play_circle);
                holder.say2.setText("寵物的情緒是"+TranslatetalkActivity.dogemotion+"\n"+cry[random2]);
                  TranslatetalkActivity.dogemotion=null;
                System.out.println("dogsay2"+ TranslatetalkActivity.dogemotion);
            }
            holder.say.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player = new MediaPlayer();
                    try {
                        MediaPlayer m = new MediaPlayer();
                        //透過網址就能播放在storage中的音檔
                        m.setDataSource("https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/audio%2F"+ TranslatetalkActivity.photoname+"?alt=media");
//        m.setDataSource(outputFile);
                        m.prepare();
                        m.start();
         /*   player.setDataSource(fileName);
            player.prepare();
            player.start();*/
                    } catch (IOException e) {
                        Log.e("LOG_TAG", "prepare() failed");
                    }

                }
            });


//            holder.cardView2.setVisibility(View.INVISIBLE);
        }else{
          //  holder.say.setText("pls wait");
            while (TranslatetalkActivity.dogemotion==null){

                System.out.println("等待");
            }
            System.out.println("now:"+TranslatetalkActivity.dogemotion);
            if(TranslatetalkActivity.dogemotion.equals("h")){
                random2 = (int) (Math.random()*11)+1;
                holder.say.setText(talk.getSay());
                holder.say2.setBackgroundResource(R.drawable.ic_play_circle);
                //TranslatetalkActivity.dogemotion=null;
                holder.say2.setOnClickListener(new View.OnClickListener() {
                    //  boolean mStartPlaying = true;
                    public void onClick(View v) {
                        // TranslatetalkActivity.onPlay(mStartPlaying);
                        System.out.println("happy");
                        player = new MediaPlayer();
                        try {
                            MediaPlayer m = new MediaPlayer();
                            TranslatetalkActivity.dogemotion=null;
                            //透過網址就能播放在storage中的音檔
                            m.setDataSource("https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/audio%2Fhappy%2Fhappy"+random2+".wav?alt=media");
//        m.setDataSource(outputFile);
                            m.prepare();
                            m.start();
         /*   player.setDataSource(fileName);
            player.prepare();
            player.start();*/
                        } catch (IOException e) {
                            Log.e("LOG_TAG", "prepare() failed");
                        }

                      //  filepath="https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/audio%2Fi51tAtkVR5350.mp3?alt=media&token=92c06d23-987b-47ac-8fcf-af8205cae37a";
                    }
                });

            }else if(TranslatetalkActivity.dogemotion.equals("s")){

                random2 = (int) (Math.random()*12)+1;
                holder.say.setText(talk.getSay());
                holder.say2.setBackgroundResource(R.drawable.ic_play_circle);
               // TranslatetalkActivity.dogemotion=null;
                holder.say2.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
//                        boolean mStartPlaying = true;
//                        filepath="https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/audio%2Fi51tAtkVR5350.mp3?alt=media&token=92c06d23-987b-47ac-8fcf-af8205cae37a";
//                        onPlay(mStartPlaying);
//                        mStartPlaying = !mStartPlaying;
                        System.out.println("sad");
                        player = new MediaPlayer();
                        try {
                            MediaPlayer m = new MediaPlayer();
                            TranslatetalkActivity.dogemotion=null;
                            //透過網址就能播放在storage中的音檔
                            m.setDataSource("https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/audio%2Fsad%2Fsad"+random2+".wav?alt=media");
//        m.setDataSource(outputFile);
                            m.prepare();
                            m.start();
         /*   player.setDataSource(fileName);
            player.prepare();
            player.start();*/
                        } catch (IOException e) {
                            Log.e("LOG_TAG", "prepare() failed");
                        }


                    }
                });
            }else {
                random2 = (int) (Math.random()*14)+1;
                holder.say.setText(talk.getSay());
                holder.say2.setBackgroundResource(R.drawable.ic_play_circle);

                holder.say2.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View v) {
//                        boolean mStartPlaying = true;
//                        filepath="https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/audio%2Fi51tAtkVR5350.mp3?alt=media&token=92c06d23-987b-47ac-8fcf-af8205cae37a";
//                        onPlay(mStartPlaying);
//                        mStartPlaying = !mStartPlaying;
                        System.out.println("mad");
                        TranslatetalkActivity.dogemotion=null;
                        player = new MediaPlayer();
                        try {
                            MediaPlayer m = new MediaPlayer();
                            //透過網址就能播放在storage中的音檔
                            m.setDataSource("https://firebasestorage.googleapis.com/v0/b/lalala-c7bcf.appspot.com/o/audio%2Fmad%2Fmad"+random2+".wav?alt=media");
//        m.setDataSource(outputFile);
                            m.prepare();
                            m.start();
         /*   player.setDataSource(fileName);
            player.prepare();
            player.start();*/
                        } catch (IOException e) {
                            Log.e("LOG_TAG", "prepare() failed");
                        }
                    }
                });
            }
        }

//        String un=chat.getUnread();
//        if(Integer.valueOf(un)>0) {
//            holder.unread.setText(chat.getUnread());
//        }
    }

    public void onPlay(boolean start) {
        if (start) {
            startPlaying(filepath);
        } else {
            stopPlaying();
        }
    }

    public void startPlaying(String file) {
        player = new MediaPlayer();
        try {
            MediaPlayer m = new MediaPlayer();
            //透過網址就能播放在storage中的音檔
            m.setDataSource(file);
//        m.setDataSource(outputFile);
            m.prepare();
            m.start();
         /*   player.setDataSource(fileName);
            player.prepare();
            player.start();*/
        } catch (IOException e) {
            Log.e("2", "prepare() failed");
        }
    }

    public void stopPlaying() {
        player.release();
        player = null;
    }
    public int getItemCount() {
        return mfList.size();
    }

}
