package com.example.smishingdetectionapplication.activity;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.smishingdetectionapplication.R;
import com.example.smishingdetectionapplication.adapter.WayAdapter;
import com.example.smishingdetectionapplication.model.Way;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PreventActivity extends AppCompatActivity {

    ViewPager viewPager;
    WayAdapter adapter;
    List<Way> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevent);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);

        View view = getWindow().getDecorView();
        if (view != null){
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.parseColor("#81B8E4"));
        }


        models = new ArrayList<>();
        models.add(new Way(R.drawable.one));
        models.add(new Way(R.drawable.two));
        models.add(new Way(R.drawable.three));
        models.add(new Way(R.drawable.four));
        models.add(new Way(R.drawable.five));

        adapter= new WayAdapter(models, this);
        viewPager = findViewById(R.id.prevent_viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setBackgroundColor( getResources().getColor(R.color.color1));

    }
}
