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

import static java.lang.Thread.sleep;

public class fragment extends AppCompatActivity {
  //  TextView title,sender,address,context,date;
    static String[] act_title,act_address,act_context,act_sender,act_documentid,attend_documentid;
    static Date[] act_date;
    static Long[] postid,max,att_id,att_uid,act_id;
    private LinearLayout linearLayout;
    static int count,att_count;
    static String[] newdate=new String[1000];
    RelativeLayout layout;
    int width ;
    int height;
    static int click;
    static int joinpeople=0;//計算參加人數
    static boolean hasjoin;//看自己有沒有參加~
    static String docu_id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        linearLayout = (LinearLayout) findViewById(R.id.scroll);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        //宣告一堆陣列  存資料!
        attend_documentid=new String[1000];
        act_documentid=new String[1000];
        act_title = new String[1000];
        act_sender = new String[1000];
        postid = new Long[1000];
        max=new Long[1000];
        act_id=new Long[1000];
        att_id=new Long[1000];
        att_uid=new Long[1000];
        act_address = new String[1000];
        act_context = new String[1000];
        act_date = new Date[1000];
        //定義Layout!
        layout = new RelativeLayout(this);
        //取資料拉拉拉
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            long x;
                            count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d("HII", i+":"+document.getId() + " => " + document.getString("petname"));
                                act_documentid[count]=document.getId();
                                act_title[count] = document.getString("title");
                                postid[count] = document.getLong("postid");
                                act_address[count] = document.getString("address");
                                act_context[count] = document.getString("detail");
                                act_date[count] = document.getDate("posttime");
                                act_id[count]=document.getLong("actid");
                                max[count]=document.getLong("maxmember");
                                x = document.getLong("actid");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                                newdate[count] = sdf.format(act_date[count]);
                                //act_context[i].substring(3,8);
                               // Log.d("22", "count:" + act_context[count]);
                                if(act_context[count].length()>=15){
                                    act_context[count] = act_context[count].substring(0, 15) + "....";
                                }
                                else{
                                    act_context[count]=act_context[count];
                                }
                               // Log.d("2202", "count" + ":" + act_id[count]);

                                count++;
                             //   Log.d("22", "count:" + count);

                            }
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
                                                    att_id[att_count]=document.getLong("actid");
                                                    att_uid[att_count]=document.getLong("uid");
                                                    attend_documentid[att_count]=document.getId();
                                                    Log.d("0800", "att" + att_id[att_count]);
                                                    att_count++;
                                                    // Log.d("22", "user:" + att_uid[att_count]);

                                                }
                                            } else {
                                                Log.w("000", "Error getting documents.", task.getException());
                                            }

                                        }
                                    });
                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }
                       rr();
                    }
                });





//到這邊都是取資料-----拉線-----


//按鈕觸發------------------
        Button pick= findViewById(R.id.send);
        pick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(fragment.this, release.class);
                startActivity(change);
            }

        });

        Button per= findViewById(R.id.personal);
        per.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(fragment.this, person.class);
                startActivity(change);
            }

        });


        Button home= findViewById(R.id.back);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent change = new Intent(fragment.this, photo.class);
                startActivity(change);
            }

        });


    }
    //----------------
    //rr這個涵式為了避免資料尚未讀完就放按鈕，會出大事!所以讀完資料才呼叫rr放按鈕
        public void rr() {
            Button Btn[] = new Button[count];
            int j = -1;
            int f = Btn.length;
            Log.d("22", "f:" + f);
             for (int z = 0; z <= count; z++) {
               for(int x = 0; x<= login.i; x++){
                   if (postid[z] == com.example.myapplication.login.uid[x]) {
                       act_sender[z] = com.example.myapplication.login.name[x];
                   }
               }

            }
//用Spannable修改字串
            for (int i = 0; i <= Btn.length -1; i++) {
                Btn[i] = new Button(this);
                Btn[i].setId(2000 + i);
                Btn[i].setBackgroundColor(Color.parseColor("#5151A2"));
                String str=act_sender[i]+"●"+newdate[i]+"\n";
                SpannableStringBuilder ssb = new SpannableStringBuilder(str);
                ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#9D9D9D")), 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.setSpan(new RelativeSizeSpan(0.6f), 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssb.setSpan((new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL)), 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                String str1=act_title[i]+"\n";
                SpannableStringBuilder builder = new SpannableStringBuilder(str1);
                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, str1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                builder.setSpan(new RelativeSizeSpan(1.5f), 0, str1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                builder.setSpan(new StyleSpan(Typeface.BOLD), 0, str1.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                String str2=act_context[i];
                SpannableStringBuilder context = new SpannableStringBuilder(str2);
                context.setSpan(new ForegroundColorSpan(Color.parseColor("#9D9D9D")), 0, str2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                context.setSpan(new RelativeSizeSpan(0.7f), 0, str2.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                //AlignmentSpan.Standard(Layout.Alignment align)实现，段落可以靠左(ALIGN_NORMAL)，居中(ALIGN_CENTER)，靠右(ALIGN_OPPOSITE)对齐。
                Btn[i].setText(TextUtils.concat(ssb,builder,context));//將修改完成的字串放到Button上!

//放按鈕嚕
                RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams((width - 50) / 2-80, 250);
                //設定按鈕的寬度和高度
                if (i % 2 == 0) {
                    j++;
                    f--;
                }


                if (f < 1) {


                    if (i % 2 != 0) {//最後一行不是3個,是2個,或者是1個,就無法整除,就會報錯。加了這一個判斷之後就會消除錯誤
                        j++;
                    }
              /*  if (i%3 == 1) {
                      j++;
                     }*/
                }

                btParams.leftMargin = 20 + ((width - 50) / 2-40 + 40) * (i % 2);  //橫座標定位
                btParams.topMargin = 60 + 6 * 55 * j;   //縱座標定位
                // x=20 + ((width - 50) / 2 + 20) * (i % 2);

                layout.addView(Btn[i], btParams);   //將按鈕放入layout元件
            }
            //this.setContentView(layout); //替換之前的佈局,要是有自定義了佈局的話,就要記得將這句話注視掉
            //批量設定監聽
            linearLayout.addView(layout);

            for (int k = 0; k <= Btn.length - 1; k++) {
                //這裡不需要findId,因為建立的時候已經確定哪個按鈕對應哪個Id
                Btn[k].setTag(k);                //為按鈕設定一個標記,來確認是按下了哪一個按鈕

                Btn[k].setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = (Integer) v.getTag();
                        Intent change = new Intent(fragment.this, cancel.class);
                        startActivity(change);
                        //這裡的i不能在外部定義,因為內部類的關係,內部類好多繁瑣的東西,要好好研究一番
                        click=i;//把被點到的按鈕編號存到全域變數中，之後用
                        hasjoin=false;
                        for(int att=0;att<=att_count;att++){
                            if(att_uid[att]== login.loginid && act_id[i]==att_id[att]){
                                hasjoin=true;
                                docu_id=attend_documentid[att];
                                Log.d("22", "user:" + att_uid[att]);
                                break;
                            }
                        }
                        for(int att=0;att<=att_count;att++){
                            if(att_id[att]==act_id[i]){
                                joinpeople++;
                            }

                        }

                       // Toast.makeText(getApplicationContext(), "d:" + i, Toast.LENGTH_SHORT).show();
                        //   System.out.println(i+x);
                    }
                });
            }
        }

    }


