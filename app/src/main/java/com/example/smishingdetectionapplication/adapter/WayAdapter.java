package com.example.smishingdetectionapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.smishingdetectionapplication.R;
import com.example.smishingdetectionapplication.model.Way;

import java.util.List;

public class WayAdapter extends PagerAdapter {

    private List<Way> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public WayAdapter(List<Way> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate( R.layout.item_way, container, false);

        ImageView imageview = view.findViewById(R.id.way_image);
        imageview.setImageResource(models.get(position).getImage());

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}
