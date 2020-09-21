package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class tryit extends AppCompatActivity {


    private LinearLayout linearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tryit);
        linearLayout = (LinearLayout)findViewById(R.id.l1);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //自定義layout元件
        RelativeLayout layout = new RelativeLayout(this);
        //-----------------------------------------------------------------------------------------
        String str = "你好，安卓-》Hello,Android!";
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        //下面修改字體顏色，將顏色放到一個陣列裡，也可以不放，直接用
        ForegroundColorSpan[] colorSpan = {new ForegroundColorSpan(Color.RED),
                new ForegroundColorSpan(Color.GREEN), new ForegroundColorSpan(Color.BLUE),
                new ForegroundColorSpan(Color.WHITE), new ForegroundColorSpan(Color.BLACK)};
        builder.setSpan(CharacterStyle.wrap(new ForegroundColorSpan(Color.RED)), 5, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new RelativeSizeSpan(0.5f), 5, 10, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ///多種效果可疊加上去!!!

        //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE --- 不包含两端start和end所在的端点              (a,b)
        //Spanned.SPAN_EXCLUSIVE_INCLUSIVE --- 不包含端start，但包含end所在的端点       (a,b]
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE --- 包含两端start，但不包含end所在的端点   [a,b)
        //Spanned.SPAN_INCLUSIVE_INCLUSIVE--- 包含两端start和end所在的端点                     [a,b]
        //-------------------------------------------------------------------------------------------------將字串做特殊改動!!
        //這裡建立17個按鈕,每行放置4個按鈕
        Button Btn[] = new Button[17];
        int j = -1;
        int f = Btn.length;
        for (int i = 0; i <= Btn.length - 1; i++) {
            Btn[i] = new Button(this);
            Btn[i].setId(2000 + i);
            Btn[i].setText(builder);//將修改完成的字串放到Button上!


            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams((width - 50) / 2-50, 200);
            //設定按鈕的寬度和高度
            if (i % 2 == 0) {
                j++;
            }

            f--;
            if (f < 1) {


                if (i % 2 != 0) {//最後一行不是3個,是2個,或者是1個,就無法整除,就會報錯。加了這一個判斷之後就會消除錯誤
                    j++;
                }
              /*  if (i%3 == 1) {
                      j++;
                     }*/
            }
            btParams.leftMargin = 20 + ((width - 50) / 2-50 + 20) * (i % 2);   //橫座標定位
            btParams.topMargin = 20 + 5 * 55 * j;   //縱座標定位
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
                    //這裡的i不能在外部定義,因為內部類的關係,內部類好多繁瑣的東西,要好好研究一番

                    Toast.makeText(getApplicationContext(), "d:" + i, Toast.LENGTH_SHORT).show();
                    //   System.out.println(i+x);
                }
            });
        }
    }
}
