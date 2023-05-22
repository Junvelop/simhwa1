package com.example.myapplication;


import android.os.Bundle;
import android.util.Log;

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

    private String apiUrl = "https://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=Kz9SWzAXKdBc%2F16leusx9Mi65rCCzbm6DOtk3RTaeoOyzhVEux8V5BRxkum8tSOEbLGmUVTMfnE5eGVJGVpSPg%3D%3D&numOfRows=300&pageNo=1&type=json";

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_druglist);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        fetchDataFromAPI();
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

                        // 응답 데이터를 파싱하여 필요한 정보 추출
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

                                    // 약의 품목명과 이미지 URL 출력
                                    Log.d("API Data", "Item Name: " + itemName + ", Image URL: " + itemImage);

                                    // RecyclerAdapter에 데이터 추가
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