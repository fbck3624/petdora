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

public class accept extends AppCompatActivity {

    private ArrayList<friendaccept> bookList=new ArrayList<>();
    public static String[][] accept;

    public static int count=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        accept=new String[100][4];

        count=0;
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection("waitaccept")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //記得改登入
                                if(document.getString("uid").equals("U29VExCdmzgWdHkbs7Top9oC7C92")){
                                //    System.out.println(document.getString("notice"));

                                    accept[i][0]=document.getId();


                                    accept[i][1]=document.getString("uid");
                                    accept[i][2]=document.getString("fid");

                                    i++;
                                    count++;
                                }

                            }

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        data.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("uid").equals(accept[i][2])){
                                    accept[i][2]=document.getString("name");
                                    accept[i][3]=document.getString("photo");
                                    // System.out.println("img:"+notice[i][2]);
                                    i++;

                                }

                            }
                            lay();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void initBooks() {

        for (int i = 0; i < count; i++) {
            //  System.out.println("notice"+notice[i][0]);
            friendaccept friends = new friendaccept(accept[i][0],accept[i][3],accept[i][2]);

            //pets fr=new pets(petdata[i+1][0], petdata[i+1][1]);
            System.out.println(friends);
            bookList.add(friends);
            // System.out.println("HEE+1");

        }

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lay(){
        initBooks();
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.id_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        friendacceptAdapter adapter=new friendacceptAdapter(bookList,this);
        recyclerView.setAdapter(adapter);
    }

}