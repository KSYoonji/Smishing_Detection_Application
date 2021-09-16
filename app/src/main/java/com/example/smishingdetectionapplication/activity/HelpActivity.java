package com.example.smishingdetectionapplication.activity;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.smishingdetectionapplication.R;
import com.example.smishingdetectionapplication.adapter.WayAdapter;
import com.example.smishingdetectionapplication.model.Way;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    ViewPager viewPager;
    WayAdapter adapter;
    List<Way> models;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        View view = getWindow().getDecorView();
        if (view != null){
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.parseColor("#81E4D0"));
        }

        models = new ArrayList<>();
        models.add(new Way(R.drawable.six));
        models.add(new Way(R.drawable.seven));
        models.add(new Way(R.drawable.eight));
        models.add(new Way(R.drawable.nine));
        models.add(new Way(R.drawable.ten));

        adapter = new WayAdapter(models, this);
        viewPager = findViewById(R.id.help_viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setBackgroundColor( getResources().getColor(R.color.color2));

    }
}
