package com.example.smishingdetectionapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smishingdetectionapplication.R;
import com.example.smishingdetectionapplication.activity.RecordDetailActivity;
import com.example.smishingdetectionapplication.model.Record;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private ArrayList<Record> mDataList;
    Context mContext;
    private final static String TAG = "RecordAdapter---> ";

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView phoneNum;
        TextView percent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_percent);
            phoneNum = itemView.findViewById(R.id.record_phoneNum);
            percent = itemView.findViewById(R.id.record_percent);

            // 아이템 클릭 이벤트 처리.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(v.getContext(), RecordDetailActivity.class);
                        Record record = mDataList.get(pos);
                        intent.putExtra("phoneNum",record.getSender());
                        intent.putExtra("percent",record.getPercentage());
                        intent.putExtra("url", record.getUrl());
                        intent.putExtra("msg", record.getMsg());

                        mContext.startActivity(intent);
                    }
                }
            });

        }

        void onBind(Record record) {
            String p="";

            phoneNum.setText(record.getSender());
            if (!(record.getPercentage().equals("주의 ")))
                p = record.getPercentage() + "%";
            else
                p=record.getPercentage();
            percent.setText(p);
            imageView.setImageResource(record.getPercentImg());
        }

    }

    public RecordAdapter(ArrayList<Record> arrayList, Context context){
        mDataList = arrayList;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //ViewHolder가 관리하는 View position에 해당하는 데이터 바인딩
        holder.onBind(mDataList.get(position));

    }

    @Override
    public int getItemCount() {

        //Adapter가 관리하는 전체 데이터 개수 반환
        return mDataList.size();
    }


}
