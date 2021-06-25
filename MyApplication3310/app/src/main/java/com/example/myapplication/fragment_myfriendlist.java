package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
public class fragment_myfriendlist extends Fragment {
    private List<myfriends> bookList=new ArrayList<>();
    private String[][] friend,card;
    int c=0;
    RecyclerView recyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_myfriendlist() {
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
    public static fragment_myfriendlist newInstance(String param1, String param2) {
        fragment_myfriendlist fragment = new fragment_myfriendlist();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_myfriendlist, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.friend_list).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), frienduser.class);
//                startActivity(intent);
//                //Toast.makeText(mContext,getString(R.string.btn_start),Toast.LENGTH_SHORT).show();
//            }
//        });
        //設置聊天列表跳轉
        //talklistActivity模擬好友列表聊天列表
        view.findViewById(R.id.friend_talk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), friend.class);
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

        view.findViewById(R.id.friend_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), group.class);
                startActivity(intent);
                //Toast.makeText(mContext,getString(R.string.btn_start),Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView=view.findViewById(R.id.id_recycler_view);
        friend = new String[1000][3];
        card = new String[1000][4];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        System.out.println("login.loginid="+login.loginid);

        db.collection("friend").whereEqualTo("name",login.loginid.toString())
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
                                String talkroom=document.getData().get("talkroom").toString();
                                friend[count][0]=name;
                                friend[count][1]=friendname;
                                friend[count][2]=talkroom;
                                Log.d("friend=", document.getData().toString());
                                count++;
                            }
                            re();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void lay(){
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        myfriendsAdapter adapter1=new myfriendsAdapter(bookList,getActivity());
        recyclerView.setAdapter(adapter1);
    }

    private void initBooks(){
        System.out.println("initbook=");

        for (int i=0;i<card.length;i++){
            if(card[i][0]==null) {
                break;
            }
            System.out.println("card="+card[i][0]+" "+card[i][1]+" "+card[i][2]);
            myfriends friends = new myfriends(card[i][0], card[i][1], card[i][2],card[i][3]);
            bookList.add(friends);
        }
        lay();
    }



    private void re(){
        System.out.println("getre");
        c=0;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "card";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                for (int i = 0; i < friend.length; i++) {
                                    if(friend[i][0]==null) {
                                        break;
                                    }
                                    if (document.getData().get("uid").toString().equals(friend[i][1])) {
                                        card[c][0]=document.getString("name");
                                        card[c][1] = document.getData().get("photo").toString();
                                        card[c][2] =  friend[i][1];
                                        card[c][3] = friend[i][2];
                                        Log.d(TAG, c + " => " + card[c][0]+ " => " + card[c][1]+ " => " + card[c][2]+ " => " + card[c][3]);
                                        c++;
                                    }
                                }
                            }
                            initBooks();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
