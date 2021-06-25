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
import java.util.List;

public class noti extends AppCompatActivity {
    private List<notis> bookList=new ArrayList<>();
    RecyclerView recyclerView;
    private String[][] comment,card;
    private String[] photoid=new String[1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.id_recycler_view);
        comment = new String[1000][5];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("photo").whereEqualTo("id",login.loginid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "noti";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                photoid[i]=document.getData().get("commentid").toString();
                                Log.d("comment=", photoid[i]);
                                i++;
                            }
                            getcomment();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void getcomment(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("comment")
                    .orderBy("time")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        private static final String TAG = "noti";
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int j = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    for (int i=0;i<photoid.length;i++) {
                                        if (photoid[i]==null){
                                            break;
                                        }
                                        if (document.getData().get("commentid").toString().equals(photoid[i])
                                            && !document.getData().get("id").toString().equals(login.loginid)) {
                                            comment[j][0] = document.getData().get("id").toString();
                                            comment[j][1] = document.getData().get("comment").toString();
                                            comment[j][2] = document.getData().get("read").toString();
                                            comment[j][3] = document.getData().get("commentid").toString();
                                            comment[j][4] = document.getId();
                                            Log.d("comment=", document.getData().toString());
                                            j++;
                                            break;
                                        }
                                    }
                                }
                                lay();
                                initBooks();
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
    }

    private void lay(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        notiAdapter adapter=new notiAdapter(bookList,this);
        recyclerView.setAdapter(adapter);
    }

    private void initBooks(){
        for (int i=0;i<comment.length;i++){
            if(comment[i][0]!=null) {
                notis notis = new notis(comment[i][0],comment[i][1],comment[i][2],comment[i][3],comment[i][4]);
                bookList.add(notis);
            }
        }
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
}
