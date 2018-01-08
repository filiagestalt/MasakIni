package com.baskom.masakini.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.baskom.masakini.R;
import com.baskom.masakini.adapter.ItemKeranjangAdapter;
import com.baskom.masakini.adapter.ItemKeranjangCardViewHolder;
import com.baskom.masakini.model.ItemKeranjang;
import com.baskom.masakini.request.ItemKeranjangRequest;
import com.baskom.masakini.request.MasukRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemKeranjangActivity extends AppCompatActivity {
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    List<ItemKeranjang> itemKeranjangList = new ArrayList<>();
    RecyclerView recyclerView;
    ItemKeranjangAdapter adapter;
    ProgressBar progressBar;
    Toolbar toolbar;
    Button btnBayarKeranjang;
    Intent intentTotalEstimasi;
    public static int jumlahItemKeranjang;
    public static TextView tvTotalEstimasi;
    public static int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_keranjang);
        toolbar = findViewById(R.id.toolbar_keranjang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keranjang Masakan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBarItemKeranjang);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recycler_view_cart);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        tvTotalEstimasi = findViewById(R.id.tv_total_estimasi_item_keranjang);
        tvTotalEstimasi.setText(formatRupiah.format(total));

        btnBayarKeranjang = findViewById(R.id.btn_bayar_troli);
        btnBayarKeranjang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(ItemKeranjangActivity.this, PembelianKonfirmasiActivity.class);
              intent.putExtra("total", total);
              startActivity(intent);
            }
        });

        getItemKeranjangList();
    }

    public void getItemKeranjangList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonArray jsonResponse = new JsonParser().parse(response).getAsJsonArray();
                Type listType = new TypeToken<List<ItemKeranjang>>(){}.getType();
                itemKeranjangList = new Gson().fromJson(jsonResponse, listType);
                adapter = new ItemKeranjangAdapter(itemKeranjangList, ItemKeranjangActivity.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                jumlahItemKeranjang = adapter.getItemCount();
//
//                for (int i = 0 ; i<jumlahItemKeranjang;i++){
//                    ItemKeranjangCardViewHolder.tempTotal
//                }
                //Bagian setharga, masih on progress
                /*recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    @TargetApi(16)
                    public void onGlobalLayout() {
                        for(int i = 0; i < recyclerView.getAdapter().getItemCount(); i++){
                            View view = recyclerView.getLayoutManager().findViewByPosition(0);
                            TextView test = view.findViewById(R.id.harga_item_keranjang);
                            int hargaItem = Integer.parseInt(test.getText().toString().substring(33));
                            totalEstimasi += hargaItem;
                        }
                        tvTotalEstimasi.setText(Integer.toString(totalEstimasi));
                        if(Build.VERSION.SDK_INT == 15) {
                            recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }else{
                            recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }

                });*/
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
