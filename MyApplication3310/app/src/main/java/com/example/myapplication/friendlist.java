package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link friendlist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class friendlist extends Fragment {

    private List<friends> bookList=new ArrayList<>();
    private String[][] friend,talk,card;
    private int size=0;
    int size2,fr;
    int c=0;
    int readcount=0;
    String loginid=login.loginid;
    RecyclerView recyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public friendlist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment friendlist.
     */
    // TODO: Rename and change types and number of parameters
    public static friendlist newInstance(String param1, String param2) {
        friendlist fragment = new friendlist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friendlist, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.friend_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), myfriend.class);
                startActivity(intent);
                //Toast.makeText(mContext,getString(R.string.btn_start),Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.friend_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), group.class);
                startActivity(intent);
                //Toast.makeText(mContext,getString(R.string.btn_start),Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.friend_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), newfriend.class);
                startActivity(intent);
                //Toast.makeText(mContext,getString(R.string.btn_start),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView= (RecyclerView) view.findViewById(R.id.id_recycler_view);
        friend = new String[1000][5];
        talk = new String[1000][6];
        card = new String[1000][6];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        System.out.println("login.loginid="+login.loginid);

        db.collection("friend").whereEqualTo("name",login.loginid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "main";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String name= document.getData().get("name").toString();
                                String friendname=document.getData().get("friend").toString();
                                String id=document.getId();
                                String friendid=document.getData().get("friend_id").toString();
                                String talkroom=document.getData().get("talkroom").toString();
                                friend[count][0]=name;
                                friend[count][1]=friendname;
                                friend[count][2]=friendid;
                                friend[count][3]=talkroom;
                                friend[count][4]=id;
                                Log.d("friend=", document.getData().toString());
                                count++;
                            }
                            gettalk();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    private void lay(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        friendsAdapter adapter=new friendsAdapter(bookList,getActivity());
        recyclerView.setAdapter(adapter);

    }

    private void initBooks(){
        for (int i=0;i<card.length;i++){
            if(card[i][0]!=null) {
                friends friends = new friends(card[i][0], card[i][1], card[i][2], card[i][3], R.drawable.unread,card[i][4],card[i][5]);
                bookList.add(friends);
            }
        }
    }

    private void gettalk(){
        System.out.println("gettalk");
        fr=0;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("talk")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "talk2";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(int i=0;i<friend.length;i++){
                                if(friend[i][0]==null) {
                                    break;
                                }
                                final String friendname=friend[i][1];
                                final String room=friend[i][3];
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getData().get("talkroom").toString().equals(room)) {
                                        if(fr<friend.length){
                                            String name= document.getData().get("name").toString();
                                            String talktext=document.getData().get("talk").toString();
                                            String talkid=document.getData().get("talkid").toString();
                                            String time=document.getData().get("time").toString();
                                            String talkroom=document.getData().get("talkroom").toString();
                                            talk[fr][0]=friendname;
                                            talk[fr][1]=talktext;
                                            talk[fr][2]=talkid;
                                            talk[fr][3]=talkroom;
                                            talk[fr][4]=time;

                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            fr++;
                                            break;
                                        }
                                    }
                                }
//                                if(talk[fr][1]==null ||  talk[fr][1]=="") {
//                                    talk[fr][0] = friend[i][1];
//                                    talk[fr][1] = "";
//                                    talk[fr][2] = "";
//                                    talk[fr][3] = friend[i][3];
//                                    talk[fr][4] = "";
//                                    fr++;
//                                }else{
//                                    fr++;
//                                }
                            }
                            readcount();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void readcount(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("talk").orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "readcount";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(int i=0;i<talk.length;i++){
                                if(talk[i][0]==null) {
                                    break;
                                }
                                readcount=0;
                                final String friendname=talk[i][0];
                                final String room=talk[i][3];
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getData().get("name").toString().equals(friendname) &&
                                            document.getData().get("talkroom").toString().equals(room) &&
                                            !document.getData().get("read").toString().contains(login.loginid)) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        readcount++;
                                        talk[i][5] = String.valueOf(readcount);
                                    }
                                }
                                Log.d("read=", String.valueOf(readcount));
                            }
                            re();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void re(){
        System.out.println("getre");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "card";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                for (int i = 0; i < talk.length; i++) {
                                    if(talk[i][0]==null) {
                                        break;
                                    }
                                    if (document.getData().get("uid").toString().equals(talk[i][0])) {
//                                        for (int x = 0; x < login.i; x++) {
//                                            if (talk[i][0].equals(login.uid[x].toString())) {
//                                                String name1 = login.name[x];
//                                                card[c][0] = name1;
//                                            }
//                                        }
//                                        final String nowuserid=talk[i][0];
//                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                        db.collection("user")
//                                                .get()
//                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                        if (task.isSuccessful()) {
//                                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                if(nowuserid.equals(document.getString("uid"))){
//                                                                    card[c][0]=document.getString("name");
//                                                                    break;
//                                                                }
//                                                            }
//                                                        } else {
//                                                            Log.w("TAG", "Error getting documents.", task.getException());
//                                                        }
//                                                    }
//                                                });
                                        card[c][0]=document.getString("name");
                                        card[c][1] = talk[i][1];
                                        card[c][2] = document.getData().get("photo").toString();
//                                    String[] time= talk[c][4].split(" ");
//                                    String[] d= time[0].split("-");
//                                    String[] t= time[1].split(":");
                                        card[c][4] =  talk[i][5];
                                        card[c][3] =  talk[i][4];
                                        card[c][5] = talk[i][3];
                                        Log.d("card=", c + " => " + card[c][0]+ " => " + card[c][1]+ " => " + card[c][2]+ " => " + card[c][3]+" => " + card[c][4]+" => " + card[c][5]);
                                        c++;
                                    }
                                }
                            }
                            initBooks();
                            lay();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}