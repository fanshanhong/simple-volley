package com.fan.simplevolley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fan.simplevolley.volley.GsonRequest;
import com.fan.simplevolley.volley.Request;
import com.fan.simplevolley.volley.RequestQueue;
import com.fan.simplevolley.volley.Response;
import com.fan.simplevolley.volley.SimpleVolley;
import com.fan.simplevolley.volley.StringRequest;
import com.fan.simplevolley.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @Description: 这是一个简单的 volley 网络请求框架的实现。 我称它为 simple-volley。
 * @Author: fan
 * @Date: 2020/3/5 23:34
 * @Modify:
 */
public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;

    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 创建一个RequestQueue, 对应 Volley.newRequestQueue();
        requestQueue = SimpleVolley.newRequestQueue();

        resultTextView = findViewById(R.id.result);

        findViewById(R.id.string_request_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.GET, "http://www.baidu.com", new Response.RequestListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                        resultTextView.setText(result);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        System.out.println(error.getMessage());
                        resultTextView.setText(error.getMessage());
                    }
                });
                requestQueue.add(request);
            }
        });

        findViewById(R.id.gson_request_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type type = new TypeToken<Weather>(){}.getType();
                GsonRequest<Weather> gsonRequest = new GsonRequest<Weather>(Request.Method.GET, "http://www.weather.com.cn/data/sk/101010100.html", new Response.RequestListener<Weather>() {
                    @Override
                    public void onSuccess(Weather result) {

                        Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                        resultTextView.setText(result.toString());
                    }

                    @Override
                    public void onError(VolleyError error) {
                        resultTextView.setText(error.getMessage());

                    }
                }, type);

                requestQueue.add(gsonRequest);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放
    }
}
