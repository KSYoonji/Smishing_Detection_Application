package com.example.smishingdetectionapplication.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smishingdetectionapplication.R;
import com.example.smishingdetectionapplication.adapter.RecordAdapter;
import com.example.smishingdetectionapplication.model.Record;
import com.example.smishingdetectionapplication.process.Processing;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    private ArrayList<Record> dataList = new ArrayList<>();;
    RecordAdapter adapter;

    private final static String TAG = "RecordActivity--->";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("     탐지기록");
        setSupportActionBar(toolbar);

        InitializeData();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        manager.setReverseLayout(true);     //역순으로 출력
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록

        adapter = new RecordAdapter(dataList, this);
        recyclerView.setAdapter(adapter);  // Adapter 등록

    }

    public void InitializeData()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("탐지 기록");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange 내부");

                //String n = snapshot.getValue().toString();      //{수익률={msg=..., sender=..., url=...},택배가={..}} 형식으로 출력

                for (DataSnapshot ds : snapshot.getChildren()) {

                    String sender = ds.child("sender").getValue().toString();
                    String percentage = ds.child("percentage").getValue().toString();
                    String msg = ds.child("msg").getValue().toString();
                    String url = ds.child("url").getValue().toString();

                    int img = Processing.percentImg(percentage);

                    dataList.add(new Record(sender,percentage,img,msg,url));

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}

