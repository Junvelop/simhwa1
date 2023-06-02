package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DrugInfoActivity extends AppCompatActivity {

    private TextView itemNameTextView;
    private TextView itemDescriptionTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);







        itemNameTextView = findViewById(R.id.itemNameTextView);


        // 인텐트에서 약 정보를 가져옴
        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");
        String itemDescription = intent.getStringExtra("itemDescription");

        // 약 정보를 뷰에 설정
        itemNameTextView.setText(itemName);


        Button addButton = findViewById(R.id.button2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추가 버튼 클릭 시, Dist 액티비티로 이동
                Intent intent = new Intent(DrugInfoActivity.this, Dist.class);
                startActivity(intent);
            }
        });

        Button SButton = findViewById(R.id.button0);
        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SButton 클릭 시, MainActivity로 이동
                Intent intent = new Intent(DrugInfoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}