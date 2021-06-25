package com.example.myapplication.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.First9Fragment;
import com.example.myapplication.R;
import com.example.myapplication.activeAdapter;
import com.example.myapplication.login;
import com.example.myapplication.myacts;
import com.example.myapplication.myattendact;
import com.example.myapplication.myfavoritepost;
import com.example.myapplication.mypostact;
import com.example.myapplication.postinthat;
import com.example.myapplication.postwriting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements LocationListener {
    LocationManager locationManager;
    private HomeViewModel homeViewModel;
    TextView nearby,all;
    LinearLayout l1;
    RecyclerView recyclerView;
    private ArrayList<myacts> bookList=new ArrayList<>();
    public static String[][] petdata;
    public static int count=0;
    public static boolean yes;
    private String tvCountry,tvState,tvCity,tvPIN,tvAddress;
    int read=1;
    static MenuItem note;

    public HomeFragment(){
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        grantPermission();
        checkLocationIsEnabledOrNot();
        getLocation();
        petdata=new String[100][4];
//        FirebaseFirestore data = FirebaseFirestore.getInstance();
        //資料寫死 記得改
        allactive();

    }

    public void allactive(){
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        Date nowdate = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        try {
            nowdate = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("nowdate="+nowdate);
        data.collection("active").whereGreaterThan("fdate",nowdate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                                i++;
                                count++;
                            }
                            if(count%2!=0){
                                yes = true;
                            }
                            else{
                                yes = false;
                            }
                            System.out.println("count="+count);
                            lay();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void nearbyactive(){
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        Date nowdate = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        try {
            nowdate = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("nowdate="+nowdate);
        data.collection("active").whereGreaterThan("fdate",nowdate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getString("longaddress"));
                                System.out.println(tvCity);
                                if (document.getData().get("longaddress").toString().contains(tvCity)) {
                                    String name = document.getData().get("title").toString();
                                    if (name.length() > 8) {
                                        name = name.substring(0, 8) + "...";
                                    } else {
                                        name = name;
                                    }
                                    String image = document.getData().get("photo").toString();
                                    String actid = document.getData().get("actid").toString();
                                    petdata[i][0] = name;
                                    petdata[i][1] = image;
                                    petdata[i][2] = actid;
                                    petdata[i][3]=document.getId();
                                    i++;
                                    count++;
                                    Log.d("friend", document.getId() + " => " + document.getData());
                                }
                            }
                            if(count%2!=0){
                                yes = true;
                            }
                            else{
                                yes = false;
                            }
                            System.out.println("count="+count);
                            lay();
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void getLocation() {
        try{
            locationManager =(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,500,5,(LocationListener)this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void checkLocationIsEnabledOrNot() {
        LocationManager lm=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnableed=false;
        boolean networkEnabled=false;
        try{
            gpsEnableed=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            networkEnabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!gpsEnableed && !networkEnabled){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Enabled GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enabled", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel",null)
                    .show();

        }

    }

    private void grantPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        try{
            Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());

            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            tvCountry=addresses.get(0).getCountryName();//國家
            tvState=addresses.get(0).getAdminArea();//城市
            tvCity=addresses.get(0).getLocality();//區
            tvPIN=addresses.get(0).getPostalCode();//郵遞區號
            tvAddress=addresses.get(0).getAddressLine(0);//完整地址

          /*  System.out.println("tvCountry="+tvCountry);
            System.out.println("tvState="+tvState);
            System.out.println("tvCity="+tvCity);
            System.out.println("tvPIN="+tvPIN);
            System.out.println("tvAddress="+tvAddress);*/
//            Toast.makeText(getActivity(), "定位成功!", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.active, menu);
        super.onCreateOptionsMenu(menu, inflater);
        note = menu.findItem(R.id.action_notifications);
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection("notice").whereEqualTo("uid",login.loginid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getBoolean("read")==false){
                                    // note.setVisibility(View.GONE);
                                    read=0;
                                    System.out.println("read"+read);
                                    note.setIcon(R.drawable.note);


                                }else{
                                    //notread.setVisibility(View.GONE);
                                    System.out.println("read"+read);
                                }
                            }

                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        //return true;
    }
    private void initBooks() {
        bookList.clear();
        yes = false;
        for (int i = 0; i < petdata.length / 2; i++) {
            if (petdata.length % 2 == 0) {
                if (petdata[i][0] == null) {
                    break;
                }
                if (i % 2 == 0) {
                    myacts friends = new myacts(petdata[i][0], petdata[i][1], petdata[i][2], petdata[i][3], petdata[i + 1][0], petdata[i + 1][1], petdata[i + 1][2], petdata[i+1][3]);
                    //pets fr=new pets(petdata[i+1][0], petdata[i+1][1]);
                    //    System.out.println(petdata[i][0]);
                    //    System.out.println(friends);
                    bookList.add(friends);
                    //       System.out.println("HEE+1");

                    //bookList.add(fr);
                }

            } else {
                if (petdata[i][0] == null) {
                    break;
                }
                if (i % 2 != 0) {
                    myacts friends = new myacts(petdata[i][0], petdata[i][1], petdata[i][2], petdata[i][3], petdata[i + 1][0], petdata[i + 1][1], petdata[i + 1][2], petdata[i+1][3]);
                    // pets fr=new pets(petdata[i+1][0], petdata[i+1][1]);
                    bookList.add(friends);
                    //        System.out.println("HEE");
                    //   bookList.add(fr);
                    //  yes = true;
                }

            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
//            case R.id.action_settings:
//                Intent chang = new Intent(getActivity(), postwriting.class);
//                startActivity(chang);
//                return true;
//
//            case R.id.mypost:
//                System.out.println("mypost");
//                Intent change1 = new Intent(getActivity(), mypostact.class);
//                startActivity(change1);
//                return true;
//            case R.id.meattend:
//                System.out.println("meattend");
//                Intent change = new Intent(getActivity(), myattendact.class);
//                startActivity(change);
//                return true;
//            case R.id.myfavoritepost:
//                System.out.println("myfavoritepost");
//                Intent change2 = new Intent(getActivity(), myfavoritepost.class);
//                startActivity(change2);
//                return true;

            case R.id.home:
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);



        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        l1 =(LinearLayout) view.findViewById(R.id.l1);
        recyclerView= (RecyclerView) view.findViewById(R.id.id_recycler_view);

        all=view.findViewById(R.id.all);
        all.setEnabled(false);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petdata=new String[100][4];
//                l1.removeAllViews();
                nearby.setEnabled(true);
                all.setEnabled(false);
                allactive();
//                NavHostFragment.findNavController(HomeFragment.this)
//                        .navigate(R.id.action_nav_home_to_First9Fragment);
            }
        });
        nearby=view.findViewById(R.id.nearby);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                petdata=new String[100][4];
//                l1.removeAllViews();
                all.setEnabled(true);
                nearby.setEnabled(false);
                nearbyactive();
            }
        });

    }
    public void lay(){
        initBooks();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        activeAdapter adapter=new activeAdapter(bookList,getContext());
        recyclerView.setAdapter(adapter);
    }

}


