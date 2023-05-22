package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Data> mData = new ArrayList<>();

    public void addItem(Data item) {
        mData.add(item);
        notifyDataSetChanged(); // 데이터 변경을 어댑터에 알림
    }

    // 아이템 뷰를 저장하는 ViewHolder 클래스
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
        }
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 반환
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
    }

    // 데이터 개수 반환
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 데이터 리스트 설정
    public void setDataList(ArrayList<Data> list) {
        mData = list;
        notifyDataSetChanged();
    }
}
