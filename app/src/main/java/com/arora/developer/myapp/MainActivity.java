package com.arora.developer.myapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    int offset = 0;
    Adapter adapter;
    ArrayList<String> urls;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        urls = new ArrayList<String>();

//to get images from db
        volleyRequest();

    }


    public void volleyRequest() {

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, "http://dev1.xicom.us/xttest/getdata.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Volley response ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.get("status").equals("success")) {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray = jsonObject.getJSONArray("images");
                        urls.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //adding urls to list
                            urls.add(String.valueOf(jsonArray.getJSONObject(i).get("xt_image")));
                        }
                        Adapter adapter = new Adapter(urls, MainActivity.this);
                        recyclerView.setAdapter(adapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Volley error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();
                //form data
                param.put("user_id", "108");
                param.put("offset", String.valueOf(offset));
                param.put("type", "popular");

                return param;
            }
        };

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }

    //to load new images
    public void loadMore(View view) {

        offset++;

        volleyRequest();
    }
}