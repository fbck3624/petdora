package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class updateact extends AppCompatActivity {
    Intent intent;
    int PICK_CONTACT_REQUEST = 1;
    Uri uri;
    ImageButton add,start,finish;
    String data_list,documentID;
    static int act_id;
    TextView starttime,finishtime;
    EditText title,lastlocation,context,max;
    TextView notnull;
    String uuid,longaddress;
    StorageReference storageRef,mountainsRef;
    Button sdate,fdate;
    private Date startTime = new Date();
    private Date endTime = new Date();
    private DatePickerDialog datePickerDialog;
    TimePickerDialog picker;
    private NumberPicker mNumberPicker ;
    private Calendar calendar;
    private TimePickerDialog timePickerDialog;
    private TimePickerView pvTime;
    public static boolean update=false;
    boolean photoclick=false;
    StorageReference storageReference,pic_storage;
    String mtalkroom="0";
    final loading loading=new loading(updateact.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendar = Calendar.getInstance();
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection("active").orderBy("actid", Query.Direction.DESCENDING).limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                act_id=Integer.valueOf(document.getData().get("actid").toString());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        data.collection("group")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "group";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(Integer.valueOf(mtalkroom) < Integer.valueOf(document.getData().get("talkroom").toString())){
                                    mtalkroom=document.getData().get("talkroom").toString();
                                }
                            }
                            mtalkroom=Integer.toString(Integer.valueOf(mtalkroom)+1);
                            getmaxroom();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        storageReference= FirebaseStorage.getInstance().getReference();

        title=findViewById(R.id.title);
        start=findViewById(R.id.start);
        finish=findViewById(R.id.finish);
        lastlocation=findViewById(R.id.lastlocation);
        context=findViewById(R.id.context);
        max=findViewById(R.id.max);
        starttime=findViewById(R.id.starttime);
        finishtime=findViewById(R.id.finishtime);
        add = findViewById(R.id.photo);
        Places.initialize(getApplicationContext(),"AIzaSyCwE6QqfBf8u9Br-J-QAEJENrdD5B4BVu4");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(activeAdapter.clickid.equals(String.valueOf(document.getLong("actid")))){
                                    title.setText(document.getString("title"));
                                    documentID=document.getId();
                                    uuid = UUID.randomUUID().toString();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                                    String sdate = sdf.format(document.getDate("sdate"));
                                    String fdate=sdf.format(document.getDate("fdate"));
                                    starttime.setText(sdate);
                                    finishtime.setText(fdate);
                                    lastlocation.setText(document.getString("address"));
                                    context.setText(document.getString("detail"));
                                    lastlocation.setFocusable(false);
                                    start.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showCalenderDialog();
                                        }
                                    });
                                    finish.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showCalenderDialog2();
                                        }
                                    });
                                    lastlocation.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
                                            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(updateact.this);
                                            startActivityForResult(intent, 100);
                                        }
                                    });
                                    Glide.with(context).load(document.getString("photo"))
                                            .into(add);

                                    add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            intent = new Intent();
                                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.setType("image/*");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            startActivityForResult(intent, 1);
                                        }

                                    });
                                    add.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    max.setText(String.valueOf(document.getLong("maxmember")));
                                    Button submit = findViewById(R.id.submit);
                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (title.getText().toString().equals("")
                                                    || starttime.getText().toString().equals("")
                                                    || finishtime.getText().toString().equals("")
                                                    || lastlocation.getText().toString().equals("")
                                                    || context.getText().toString().equals("")
                                                    || max.getText().toString().equals("")) {
                                                Toast.makeText(updateact.this, "請確實填寫", Toast.LENGTH_LONG).show();

                                            } else {
                                                DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                                                Date sdate = new Date();
                                                Date fdate = new Date();

                                                try {
                                                    sdate = sdf.parse(starttime.getText().toString());

                                                    fdate = sdf.parse(finishtime.getText().toString());


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                Map<String, Object> active = new HashMap<>();

                                                Log.d("2202", "count" + ":" + act_id + 1);
                                                // active.put("actid", act_id + 1);
                                                active.put("address", lastlocation.getText().toString());
                                                active.put("longaddress",longaddress);
                                                active.put("detail", context.getText().toString());
                                                active.put("fdate", fdate);
                                                active.put("maxmember", Integer.parseInt(max.getText().toString()));
                                                active.put("sdate", sdate);
                                                active.put("title", title.getText().toString());


                                                db.collection("active").document(documentID)
                                                        .set(active, SetOptions.merge())
                                                        .addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {
                                                                Toast.makeText(updateact.this, "更新成功", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(updateact.this, "更新失敗", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                                Intent pick = new Intent(updateact.this, active.class);
                                                startActivity(pick);
                                            }

                                        }

                                    });


                                }

                            }



                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void setSupportActionBar(Toolbar toolbar) {

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data!=null) {
            if (requestCode == PICK_CONTACT_REQUEST) {
                uri = data.getData();
                Log.d("22", "uri:" + uri);
                add.setImageURI(uri);
                ContentResolver contentResolver = getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                data_list = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            } else if (requestCode == 100) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                lastlocation.setText(place.getName());
                longaddress = place.getAddress();
                // textView1.setText(String.format("Locality Name :%s",place.getName()));
                Log.d("name", String.valueOf(requestCode));
                // textView2.setText(String.valueOf(place.getLatLng()));
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    private String getTime(Date date) {
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private void showCalenderDialog(){
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String calender = year + "/" + (month + 1) + "/" + dayOfMonth ;
                //   birthday.setText(calender);
                Log.e("TAG", "calender : " + calender);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog( updateact.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                // time.setText(sHour + ":" + sMinute);
                                //  timer=time.getText().toString();
                                String time;
                                if(sHour<10){
                                    if(sMinute<10){
                                        time="0"+sHour + ":0" + sMinute;
                                    }else{
                                        time="0"+sHour + ":" + sMinute;
                                    }
                                }else{
                                    if(sMinute<10){
                                        time=sHour + ":0" + sMinute;
                                    }else{
                                        time=sHour + ":" + sMinute;
                                    }
                                }

                                starttime.setText(calender+"  "+time);
                            }
                        }, hour, minutes, true);
                picker.show();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }

    private void getmaxroom(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("friend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "friend";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(Integer.valueOf(mtalkroom) < Integer.valueOf(document.getData().get("talkroom").toString())){
                                    mtalkroom=document.getData().get("talkroom").toString();
                                }
                            }
                            mtalkroom=Integer.toString(Integer.valueOf(mtalkroom)+1);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void showCalenderDialog2(){
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String calender = year + "/" + (month + 1) + "/" + dayOfMonth ;
                //   birthday.setText(calender);
                Log.e("TAG", "calender : " + calender);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog( updateact.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                // time.setText(sHour + ":" + sMinute);
                                //  timer=time.getText().toString();
                                String time;
                                if(sHour<10){
                                    if(sMinute<10){
                                        time="0"+sHour + ":0" + sMinute;
                                    }else{
                                        time="0"+sHour + ":" + sMinute;
                                    }
                                }else{
                                    if(sMinute<10){
                                        time=sHour + ":0" + sMinute;
                                    }else{
                                        time=sHour + ":" + sMinute;
                                    }
                                }
                                finishtime.setText(calender+"  "+time);
                                System.out.println(finishtime.getText());

                            }
                        }, hour, minutes, true);
                picker.show();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }




}