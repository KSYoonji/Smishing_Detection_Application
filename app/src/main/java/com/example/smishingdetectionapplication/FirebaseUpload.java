package com.example.smishingdetectionapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.smishingdetectionapplication.model.Record;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FirebaseUpload {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference reference = database.getReference();
    private final static String TAG = "Firebase--->";

    public static void upload(String sender,String msg,String url,String percentage){

        if(msg == "")
            msg = "-";

        if (url == "")
            url = "-";
        
        if (percentage.equals("50"))
            percentage = "의심 ";

        SimpleDateFormat format = new SimpleDateFormat ( "yyMMddHHmmss");
        Date time = new Date();
        String id = format.format(time);

        Record record = new Record(sender,msg,url,percentage);
        reference.child("탐지 기록").child(id).setValue(record);


    }


}
