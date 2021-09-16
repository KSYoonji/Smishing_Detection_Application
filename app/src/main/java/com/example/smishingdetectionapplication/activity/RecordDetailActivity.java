package com.example.smishingdetectionapplication.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BlockedNumberContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smishingdetectionapplication.R;
import com.example.smishingdetectionapplication.process.Processing;

public class RecordDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("     탐지기록");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        TextView phoneNum = findViewById(R.id.detail_phoneNum);
        TextView percentage = findViewById(R.id.detail_percent);
        ImageView percentImg = findViewById(R.id.detail_percentImg);
        TextView url = findViewById(R.id.detail_url);
        TextView msg = findViewById(R.id.detail_msg);
        //Button blockBtn = findViewById(R.id.detail_block);
        Button reportBtn = findViewById(R.id.detail_report);


        String iPhoneNum = intent.getStringExtra("phoneNum");
        String iPercent = intent.getStringExtra("percent");
        String iUrl = intent.getStringExtra("url");
        String iMsg = intent.getStringExtra("msg");

        if (iUrl == "")
            iUrl = "-";

        if (iMsg == "")
            iMsg = "-";

        if (! iPercent.equals("주의 "))
            iPercent = iPercent + "%";


        phoneNum.setText(iPhoneNum);
        percentage.setText(iPercent);
        url.setText(iUrl);
        msg.setText(iMsg);

        Log.d("Detail--->", "iUrl = " + iUrl);
        Log.d("Detail--->", "iMsg = " + iMsg);

        int color = Processing.percentImg(iPercent);
        percentImg.setImageResource(color);

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.krcert.or.kr/consult/phishing.do")));
            }
        });

//        blockBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContentValues values = new ContentValues();
//                values.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, iPhoneNum);
//                Uri uri = getContentResolver().insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values);
        //java.lang.SecurityException: Caller must be system, default dialer or default SMS app 라는 오류가 남!
//            }
//        });




    }
}
