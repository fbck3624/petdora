package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class praticipate extends AppCompatActivity {

    String[] act_title, act_address, act_context, act_sender,act_documentid,attend_documentid;
    Date[] act_date;
    Long[] postid,act_id,att_id,att_uid;
    static int count, att_count;
    static String[] newdate = new String[1000];
    int width;
    int height;
    private LinearLayout linearLayout;
    RelativeLayout layout;
    static int click;
    int yesjoin;
    Long[] joinid=new Long[1000];
    Long[] actid=new Long[1000];
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_praticipate);
        super.onCreate(savedInstanceState);
        linearLayout = (LinearLayout) findViewById(R.id.line);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        layout = new RelativeLayout(this);
        act_title = new String[1000];
        act_documentid=new String[1000];
        act_sender = new String[1000];
        postid = new Long[1000];
        act_id = new Long[1000];
        act_address = new String[1000];
        act_context = new String[1000];
        act_date = new Date[1000];
        att_id=new Long[1000];
        att_uid=new Long[1000];
        attend_documentid=new String[1000];



        Button per= findViewById(R.id.back);
        per.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(praticipate.this, fragment.class);
                startActivity(change);
            }

        });

        Button send= findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(praticipate.this, person.class);
                startActivity(change);
            }

        });
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection("actattend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            att_count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d("HII", document.getId() + " => " + document.getLong("actid"));
                               // att_id[att_count]=document.getLong("actid");
                                att_uid[att_count]=document.getLong("uid");
                                if(att_uid[att_count]== login.loginid){
                                    att_id[att_count]=document.getLong("actid");
                                }
                               // Log.d("22", "user:" + att_id[att_count]);
                                att_count++;

                            }
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("active")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                count=0;
                                              //  int x=0;
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //act_id[count] = document.getLong("actid");

                                                   // Long id=document.getLong("actid");
                                                    for(int x=0;x<att_count;x++){
                                                        Log.d("22", "user:" + x);
                                                        if(att_id[x]==document.getLong("actid")) {
                                                            act_documentid[count] = document.getId();
                                                            act_title[count] = document.getString("title");
                                                            postid[count] = document.getLong("postid");
                                                            act_address[count] = document.getString("address");
                                                            act_context[count] = document.getString("detail");
                                                            act_date[count] = document.getDate("posttime");
                                                            act_id[count] = document.getLong("actid");
                                                            postid[count] = document.getLong("postid");
                                                           // Log.d("2202", "user:" + act_id[count]);
                                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                                                            newdate[count] = sdf.format(act_date[count]);
                                                            //act_context[i].substring(3,8);
                                                            if (act_context[count].length() >= 15) {
                                                                act_context[count] = act_context[count].substring(0, 15) + "....";
                                                            } else {
                                                                act_context[count] = act_context[count];
                                                            }
                                                            count++;
                                                        }
                                                      //  Log.d("count", "user:" +count);

                                                    }
                                                }
                                            } else {
                                                Log.w("000", "Error getting documents.", task.getException());
                                            }
                                            rr();
                                        }

                                    });
                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }
                       // rr();
                    }
                });

    }


    public void rr() {

        Button Btn[] = new Button[count];
        int j = -1;
        int f = Btn.length;
        //    Log.d("22", "f:" + f);
        for (int z = 0; z <= count; z++) {
            for (int x = 0; x <= login.i; x++) {
                Log.d("200", "count" + ":" + postid[z]);
                Log.d("2020", "count" + ":" + com.example.myapplication.login.uid[x]);
                if (postid[z] == com.example.myapplication.login.uid[x]) {
                    act_sender[z] = com.example.myapplication.login.name[x];

                }
            }

        }
//???Spannable????????????
        for (int i = 0; i <= Btn.length - 1; i++) {
            Btn[i] = new Button(this);
            Btn[i].setId(2000 + i);
            Btn[i].setBackgroundColor(Color.parseColor("#FF9224"));
            String str = act_sender[i] + "???" + newdate[i] + "\n";
            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#9D9D9D")), 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ssb.setSpan(new RelativeSizeSpan(0.6f), 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            ssb.setSpan((new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL)), 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            String str1 = act_title[i] + "\n";
            SpannableStringBuilder builder = new SpannableStringBuilder(str1);
            builder.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, str1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setSpan(new RelativeSizeSpan(1.5f), 0, str1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            String str2 = act_context[i];
            SpannableStringBuilder context = new SpannableStringBuilder(str2);
            context.setSpan(new ForegroundColorSpan(Color.parseColor("#9D9D9D")), 0, str2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            context.setSpan(new RelativeSizeSpan(0.7f), 0, str2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            //AlignmentSpan.Standard(Layout.Alignment align)???????????????????????????(ALIGN_NORMAL)?????????(ALIGN_CENTER)?????????(ALIGN_OPPOSITE)?????????
            Btn[i].setText(TextUtils.concat(ssb, builder,context));//??????????????????????????????Button???!

//????????????
            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams((width - 50) / 2 - 80, 250);
            //??????????????????????????????
            if (i % 2 == 0) {
                j++;
                f--;
            }


            if (f < 1) {


                if (i % 2 != 0) {//??????????????????3???,???2???,?????????1???,???????????????,????????????????????????????????????????????????????????????
                    j++;
                }
              /*  if (i%3 == 1) {
                      j++;
                     }*/
            }

            btParams.leftMargin = 20 + ((width - 50) / 2-40 + 40) * (i % 2);  //???????????????
            btParams.topMargin = 60 + 6 * 55 * j;  //???????????????
            // x=20 + ((width - 50) / 2 + 20) * (i % 2);

            layout.addView(Btn[i], btParams);   //???????????????layout??????
        }
        //this.setContentView(layout); //?????????????????????,?????????????????????????????????,?????????????????????????????????
        //??????????????????
        linearLayout.addView(layout);

        for (int k = 0; k <= Btn.length - 1; k++) {
            //???????????????findId,?????????????????????????????????????????????????????????Id
            Btn[k].setTag(k);                //???????????????????????????,????????????????????????????????????

            Btn[k].setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (Integer) v.getTag();
                    for(int j=0;j< fragment.count;j++){
                        if(fragment.postid[j]==postid[i]){
                            Intent change = new Intent(praticipate.this, cancel.class);
                            startActivity(change);
                            com.example.myapplication.fragment.click = j;
                        }
                    }

                    //?????????i?????????????????????,????????????????????????,??????????????????????????????,?????????????????????
                    //????????????????????????????????????????????????????????????
                    // hasjoin=false;
                    // Toast.makeText(getApplicationContext(), "d:" + i, Toast.LENGTH_SHORT).show();
                    //   System.out.println(i+x);
                }
            });
        }
    }
}
