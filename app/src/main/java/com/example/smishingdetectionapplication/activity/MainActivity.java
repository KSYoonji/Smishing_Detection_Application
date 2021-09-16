package com.example.smishingdetectionapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;
import im.dacer.androidcharts.LineView;

import com.example.smishingdetectionapplication.R;
import com.example.smishingdetectionapplication.background.RestartService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity---> ";

    CardView prevent_cardview;
    CardView help_cardview;
    CardView record_cardview;
    LineView lineView;

    Intent serviceIntent;

    ArrayList<String> month = new ArrayList<>();
    ArrayList<Integer> dataList = new ArrayList<>();
    ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("탐지 기록");
    int[] m = new int[12];


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        prevent_cardview = findViewById(R.id.cardview_prevent);
        help_cardview = findViewById(R.id.cardview_help);
        record_cardview = findViewById(R.id.cardview_record);
        lineView = findViewById(R.id.line_view);

        // 그래프 month 세팅
        for (int i=1; i<13;i++){
            month.add(Integer.toString(i));
            m[i-1] = 0;
            dataList.add(0);
        }

        readData(new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<Integer> list) {
                Log.d(TAG, "list(dataList) 값: " + list);

                dataLists.clear();
                dataLists.add(list);
                lineView.setDrawDotLine(true);
                lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
                lineView.setBottomTextList(month);
                lineView.setColorArray(new int[]{  Color.parseColor("#e74c3c")  });
                lineView.setDataList(dataLists);
            }
        });


        //수신 문자 가져오는 권한 얻기
        String[] permissions ={
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.RECEIVE_MMS
        };

        checkPermissions(permissions);

        //절전 모드 해제 권한 얻기
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }


        CardView.OnClickListener onClickListener = new CardView.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cardview_prevent:
                        Intent intent = new Intent(getApplicationContext(), PreventActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.cardview_help:
                        intent = new Intent(getApplicationContext(), HelpActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.cardview_record:
                        intent = new Intent(getApplicationContext(), RecordActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        prevent_cardview.setOnClickListener(onClickListener);
        help_cardview.setOnClickListener(onClickListener);
        record_cardview.setOnClickListener(onClickListener);


        // 백그라운드 실행 + Doze mode 예방(어플이 백그라운드에서도 실행될 수 있도록 포그라운드로 계속 불러오기)
        if (RestartService.serviceIntent == null) {
            serviceIntent = new Intent(this, RestartService.class);
            startService(serviceIntent);
            Log.d(TAG, "start service");
        } else {        //값이 있으면, 이미 서비스가 실행 중이기 때문에 패스
            serviceIntent = RestartService.serviceIntent;
            Log.d(TAG, "alreday");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }


    //파이어베이스에서 데이터를 불러오는 행위는 비동기적으로 작동하기 때문에 따로 처리를 해줘야 함
    private interface FirebaseCallback {
        void onCallback(ArrayList<Integer> list);
    }

    private void readData(FirebaseCallback firebaseCallback){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String s = Objects.requireNonNull(ds.getKey()).substring(2,4);
                    int value = Integer.parseInt(s);

                    m[value-1]++;
                }

                for(int i=0; i<12; i++){
                    dataList.set(i,m[i]);
                    //Log.d(TAG, "m[" + i + "]값: " + (m[i]));
                    Log.d(TAG, "dataList 값: " + dataList);
                    m[i]=0;
                }

                firebaseCallback.onCallback(dataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled");
            }
        });

    }

    public void checkPermissions(String[] permissions){

        for (String currentPermission : permissions) {
            //권한이 부여되어 있는지 확인
            int permissonCheck = ContextCompat.checkSelfPermission(this, currentPermission);

            if (permissonCheck == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, currentPermission + "권한이 허용되었습니다", Toast.LENGTH_SHORT).show();
            } else {
                //권한설정 dialog에서 거부를 누르면
                // ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
                // 단, 사용자가 "Don't ask again"을 체크한 경우
                // 거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, currentPermission)) {
                    //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                    Toast.makeText(getApplicationContext(), "스미싱 탐지를 위해서는 수신 메시지에 접근하는 해당 권한이 필수적으로 필요합니다", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, permissions, 101);
                } else {
                    Toast.makeText(getApplicationContext(), "권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }



}