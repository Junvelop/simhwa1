package com.example.eat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    private Dist mActivity;

    public RecyclerAdapter(Dist activity) {
        mActivity = activity;
    }

    private ArrayList<Data> mData = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String itemName);

        void onItemClick(String itemName, String itemDescription);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void addItem(Data item) {
        mData.add(item);
        notifyDataSetChanged(); // 데이터 변경을 어댑터에 알림
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;     // 영화이름
        TextView textView2;     // 누적관객수
        TextView textView3;     // 영화개봉일
        ImageView imageView;    // 이미지

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void onBind(Data data) {
            textView1.setText(data.getItemName());
            textView2.setText(data.getItemImage());
            textView3.setText(data.getOpenDt());

            Glide.with(itemView.getContext())
                    .load(data.getItemImage())
                    .into(imageView);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(data.getItemName());
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.activity_item_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        Data item = mData.get(position);
        holder.onBind(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = item.getItemName();



                Intent intent = new Intent(mActivity, DrugInfoActivity.class);
                intent.putExtra("itemName", itemName);

                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setDataList(ArrayList<Data> list) {
        mData = list;
        notifyDataSetChanged();
    }


}