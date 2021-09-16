package com.example.smishingdetectionapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smishingdetectionapplication.R;
import com.example.smishingdetectionapplication.process.Processing;

public class NotiShowActivity  extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notishow);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("     탐지기록");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        TextView phoneNum = findViewById(R.id.shown_phoneNum);
        TextView percentage = findViewById(R.id.shown_percent);
        ImageView percentImg = findViewById(R.id.shown_percentImg);
        TextView url = findViewById(R.id.shown_url);
        TextView msg = findViewById(R.id.shown_msg);
        Button blockBtn = findViewById(R.id.shown_block);
        Button reportBtn = findViewById(R.id.shown_report);


        String iPhoneNum = intent.getStringExtra("phoneNum");
        String iPercent = intent.getStringExtra("percent");
        String iUrl = intent.getStringExtra("url");
        String iMsg = intent.getStringExtra("msg");

        if (iUrl == "")
            iUrl = "-";

        if (iMsg == "")
            iMsg = "-";

        phoneNum.setText(iPhoneNum);
        percentage.setText(iPercent);
        url.setText(iUrl);
        msg.setText(iMsg);

        int color = Processing.percentImg(iPercent);
        percentImg.setImageResource(color);

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.krcert.or.kr/consult/phishing.do")));
            }
        });
    }
}
