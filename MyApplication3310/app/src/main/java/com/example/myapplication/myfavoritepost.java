package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class myfavoritepost extends AppCompatActivity {
    private ArrayList<myacts> bookList=new ArrayList<>();
    public static String[][] petdata;
    public static int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfavoritepost);


//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        petdata=new String[100][4];
        postwriting.update=false;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("active").whereArrayContains("collection",login.loginid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            com.example.myapplication.ui.home.HomeFragment.count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

//                                if(document.getString("postid").equals(login.loginid)) {
                                    String name=document.getData().get("title").toString();
                                    if (name.length() > 8) {
                                        name=name.substring(0, 8)+"...";
                                    } else {
                                        name=name;
                                    }

                                    String image=document.getData().get("photo").toString();
                                    String actid=document.getData().get("actid").toString();
                                    petdata[i][0]=name;
                                    petdata[i][1]=image;
                                    petdata[i][2]=actid;
                                    petdata[i][3]=document.getId();
                                    System.out.println(petdata[i][0]);
                                    i++;
                                    com.example.myapplication.ui.home.HomeFragment.count++;
//                                }

                                // Log.d("HII", i+":"+document.getId() + " => " + document.getString("petname"));


                            }
                            lay();

                        } else {
                            Log.w("000", "Error getting documents.", task.getException());
                        }

                    }
                });
    }


    private void initBooks() {
        com.example.myapplication.ui.home.HomeFragment.yes=false;
        for (int i = 0; i < petdata.length/2; i++) {
            if(petdata.length%2==0){
                if(petdata[i][0]==null){
                    break;
                }
                if(i%2==0){
                    myacts friends = new myacts(petdata[i][0], petdata[i][1],petdata[i][2],petdata[i][3],petdata[i+1][0], petdata[i+1][1],petdata[i+1][2],petdata[i+1][3]);
                    //pets fr=new pets(petdata[i+1][0], petdata[i+1][1]);
                    System.out.println(petdata[i][0]);
                    System.out.println(friends);
                    bookList.add(friends);
                    System.out.println("HEE+1");
                    com.example.myapplication.ui.home.HomeFragment.yes=true;
                    //bookList.add(fr);
                }

            }else{
                if(petdata[i][0]==null){
                    break;
                }
                if(i%2!=0){
                    myacts friends = new myacts(petdata[i][0], petdata[i][1],petdata[i][2],petdata[i][3],petdata[i+1][0], petdata[i+1][1],petdata[i+1][2],petdata[i+1][3]);
                    // pets fr=new pets(petdata[i+1][0], petdata[i+1][1]);
                    bookList.add(friends);
                    System.out.println("HEE");
                    //   bookList.add(fr);
                    com.example.myapplication.ui.home.HomeFragment.yes=true;
                }

            }

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
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycle);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        activeAdapter adapter=new activeAdapter(bookList,this);
        recyclerView.setAdapter(adapter);
    }
}
