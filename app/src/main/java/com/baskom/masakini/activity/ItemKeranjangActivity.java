package com.baskom.masakini.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.baskom.masakini.R;
import com.baskom.masakini.adapter.ItemKeranjangAdapter;
import com.baskom.masakini.model.ItemKeranjang;
import com.baskom.masakini.request.ItemKeranjangRequest;
import com.baskom.masakini.request.MasukRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemKeranjangActivity extends AppCompatActivity {
    List<ItemKeranjang> itemKeranjangList = new ArrayList<>();
    RecyclerView recyclerView;
    ItemKeranjangAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_keranjang);
        progressBar = findViewById(R.id.progressBarItemKeranjang);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar_keranjang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keranjang Masakan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_view_cart);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getItemKeranjangList();

        Button button = findViewById(R.id.btn_bayar_keranjang);
    }

    public void getItemKeranjangList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray jsonResponse = new JsonParser().parse(response).getAsJsonArray();
                Type listType = new TypeToken<List<ItemKeranjang>>() {
                }.getType();
                itemKeranjangList = new Gson().fromJson(jsonResponse, listType);
                adapter = new ItemKeranjangAdapter(itemKeranjangList, ItemKeranjangActivity.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
        };

        ItemKeranjangRequest request = new ItemKeranjangRequest(MasukRequest.getEmail(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}