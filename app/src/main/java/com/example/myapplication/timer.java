package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class timer extends AppCompatActivity {

    private EditText et_startTime;
    private EditText et_endTime;

    private Date startTime = new Date();
    private Date endTime = new Date();


    private TimePickerView pvTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){  //隱藏標題!
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_timer);

        et_startTime = findViewById(R.id.et_startTime);
        et_endTime = findViewById(R.id.et_endTime);

        et_startTime.setOnClickListener(textViewClickListener);
        et_endTime.setOnClickListener(textViewClickListener);

        initTimePicker();

    }
    View.OnClickListener textViewClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.et_startTime:
                    if (pvTime != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(startTime);
                        pvTime.setDate(calendar);
                        pvTime.show(view);
                    }
                    break;
                case R.id.et_endTime:
                    if (pvTime != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(endTime);
                        pvTime.setDate(calendar);
                        pvTime.show(view);
                    }
                    break;
            }
        }
    };

    private void initTimePicker() {

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //如果是開始時間的EditText
                if(v.getId() == R.id.et_startTime){
                    startTime = date;
                }else {
                    endTime = date;
                }
                EditText editText = (EditText)v;
                editText.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{true, true, true, true, true, false})
                .isDialog(true)
                .build();


        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改動畫樣式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部顯示
            }
        }
    }

    private String getTime(Date date) {//可根據需要自行擷取資料顯示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
