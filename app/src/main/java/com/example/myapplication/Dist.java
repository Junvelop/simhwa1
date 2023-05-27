package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Dist extends AppCompatActivity {

    private TextView itemNameTextView;
    private TextView itemNameTextView2;

    private String apiUrl = "https://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=Kz9SWzAXKdBc%2F16leusx9Mi65rCCzbm6DOtk3RTaeoOyzhVEux8V5BRxkum8tSOEbLGmUVTMfnE5eGVJGVpSPg%3D%3D&numOfRows=300&pageNo=1&type=json";

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_druglist);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        fetchDataFromAPI();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.mainlayout);
                itemNameTextView = findViewById(R.id.itemNameTextView);
                itemNameTextView2 = findViewById(R.id.itemNameTextView2);
            }
        });

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String itemName) {
                if (itemNameTextView.getText().toString().isEmpty()) {
                    itemNameTextView.setText(itemName);
                } else if (itemNameTextView2.getText().toString().isEmpty()) {
                    itemNameTextView2.setText(itemName);
                }
                Intent intent = new Intent(Dist.this, DrugInfoActivity.class);
                intent.putExtra("itemName", itemName);
                startActivity(intent);
            }

            @Override
            public void onItemClick(String itemName, String itemDescription) {
                // This method can be implemented if needed.
            }
        });
    }

    private void fetchDataFromAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(apiUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        String response = stringBuilder.toString();

                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject body = jsonObject.getJSONObject("body");
                        JSONArray items = body.getJSONArray("items");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject item = null;
                                    try {
                                        item = items.getJSONObject(i);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    String itemName = null;
                                    try {
                                        itemName = item.getString("ITEM_NAME");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    String itemImage = null;
                                    try {
                                        itemImage = item.getString("ITEM_IMAGE");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    Log.d("API Data", "Item Name: " + itemName + ", Image URL: " + itemImage);

                                    Data newItem = new Data(itemName, itemImage);
                                    adapter.addItem(newItem);
                                }
                            }
                        });
                    } else {
                        Log.e("API Error", "Response Code: " + responseCode);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
