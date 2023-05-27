package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DrugInfoActivity extends AppCompatActivity {

    private TextView itemNameTextView;
    private TextView itemNameTextView2;
    private TextView itemDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);

        itemNameTextView = findViewById(R.id.itemNameTextView);
        itemNameTextView2 = findViewById(R.id.itemNameTextView2);

        // 인텐트에서 선택한 약 정보 가져오기
        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");
        String itemDescription = intent.getStringExtra("itemDescription");

        // 첫 번째 선택한 약을 itemNameTextView에 설정
        if (itemNameTextView.getText().toString().isEmpty()) {
            itemNameTextView.setText(itemName);
        } else if (itemNameTextView2.getText().toString().isEmpty()) {
            itemNameTextView2.setText(itemName);
        } else {
            // 이미 두 개의 약 이름이 모두 표시되어 있을 경우, itemNameTextView2에 추가로 표시
            itemNameTextView2.append(", " + itemName);
        }

        // 두 번째 선택한 약이 있는지 확인
        if (intent.hasExtra("itemName2")) {
            String itemName2 = intent.getStringExtra("itemName2");
            if (itemNameTextView.getText().toString().isEmpty()) {
                itemNameTextView.setText(itemName2);
            } else if (itemNameTextView2.getText().toString().isEmpty()) {
                itemNameTextView2.setText(itemName2);
            }
        }

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