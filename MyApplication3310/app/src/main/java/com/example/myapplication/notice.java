package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class notice extends AppCompatActivity {


    private ArrayList<note> bookList=new ArrayList<>();
    public static String[][] notice;
    public static boolean[][] read;
    public static int count=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        notice=new String[100][5];
        read=new boolean[100][1];
        count=0;
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection("notice")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //記得改登入
                                if(document.getString("uid").equals(login.loginid)){
                                    System.out.println(document.getString("notice"));
                                    notice[i][0]=document.getString("notice");
                                    notice[i][1]=document.getId();
                                    System.out.println("notice:"+notice[i][1]);
                                    System.out.println("i:"+i);

                                   // System.out.println(notice[i][2]);
                                    notice[i][3]=document.getString("uid2");
                                    notice[i][4]=document.getString("pid");
                                    read[i][0]=document.getBoolean("read");
                                    i++;
                                    count++;
                                }

                            }
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("user")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                int i=0;
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    System.out.println("uid-1"+document.getString("uid"));
                                                    for(int x=0;x<count;x++){
                                                        if(document.getString("uid").equals(notice[x][3])){
                                                            System.out.println("uid-2"+document.getString("uid"));
                                                            notice[x][3]=document.getString("name");
                                                            notice[x][2]=document.getString("photo");
                                                            // System.out.println("img:"+notice[i][2]);
                                                            i++;
                                                    }


                                                    }

                                                }
                                                lay();
                                            } else {
                                                Log.w("TAG", "Error getting documents.", task.getException());
                                            }
                                        }
                                    });



                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    private void initBooks() {
        System.out.println("count="+count);
        for (int i = 0; i < count; i++) {
            note friends = new note(notice[i][0],notice[i][1],notice[i][2],notice[i][3],notice[i][4],read[i][0]);
            System.out.println("notice"+notice[i][1]);
           // System.out.println("notice"+notice[1][1]);
            //pets fr=new pets(petdata[i+1][0], petdata[i+1][1]);
            System.out.println(i+":"+friends);
            bookList.add(friends);
           // System.out.println("HEE+1");

        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    public void lay(){
        initBooks();
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.id_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        noticeAdapter adapter=new noticeAdapter(bookList,this);
        recyclerView.setAdapter(adapter);
    }
}