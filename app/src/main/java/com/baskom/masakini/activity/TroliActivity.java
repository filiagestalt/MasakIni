package com.baskom.masakini.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.baskom.masakini.R;
import com.baskom.masakini.request.MasukRequest;
import com.baskom.masakini.model.Resep;
import com.baskom.masakini.model.Transaksi;
import com.baskom.masakini.request.TambahItemRequest;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by akmalmuhamad on 22/11/17.
 */

public class TroliActivity extends AppCompatActivity {

    Resep resep;
    Transaksi transaksi;

    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    int hargaProduk;
    int totalEstimasi;
    int jumlahPaket = 1;
    int totalTakaran;

    Response.Listener<String> listenerLanjutBelanja, listenerKeranjang;
    Response.ErrorListener errorListenerLanjutBelanja, errorListenerKeranjang;
    TambahItemRequest requestLanjutBelanja, requestKeranjang;
    RequestQueue queueLanjutBelanja, queueKeranjang;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troli);

        resep = (Resep) getIntent().getSerializableExtra("objekResep");
        hargaProduk = 0;
        totalEstimasi = 0;

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView imageTroli = findViewById(R.id.image_item_keranjang);
        //TextView tanggalTroli = findViewById(R.id.tanggal_item_keranjang);
        TextView judulTroli = findViewById(R.id.judul_resep_item_keranjang);
        TextView tv_hargaTroli = findViewById(R.id.harga_item_keranjang);

        LinearLayout textLinearLayoutTroli = findViewById(R.id.linear_text_item_keranjang);
        Button btnBeli = findViewById(R.id.btn_bayar_keranjang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Beli Bahan Masakan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView tv_totalJumlahPaket = findViewById(R.id.total_jumlah_paket);
        final ElegantNumberButton btnNumberJumlahPaket = findViewById(R.id.btn_tambahJumlahPaket_item_keranjang);
        final TextView tv_totalEstimasi = findViewById(R.id.total_estimasi);

        //nampilin produk dan resep
        for (int i = 0; i < resep.getProduk().size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.text_bahan, textLinearLayoutTroli, false);
            TextView textViewNamaBahanTroli = view.findViewById(R.id.text_nama_bahan);
            TextView textViewTakaranTroli = view.findViewById(R.id.text_takaran_bahan);

            textViewNamaBahanTroli.setText(resep.getProduk().get(i).getNama());
            textViewTakaranTroli.setText(resep.getProduk().get(i).getTakaran());

            hargaProduk += resep.getProduk().get(i).getHarga();
            textLinearLayoutTroli.addView(view);
        }

        //tanggalTroli.setText(formatTanggal(new Date()));
        Glide.with(imageTroli.getContext()).load(resep.getResepImage()).into(imageTroli);
        judulTroli.setText("Bahan masakan " + resep.getJudulResep());
        tv_hargaTroli.setText("Harga Bahan " + formatRupiah.format(hargaProduk));
        tv_totalEstimasi.setText(formatRupiah.format(hargaProduk));

        btnNumberJumlahPaket.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumlahPaket = Integer.parseInt(btnNumberJumlahPaket.getNumber());

                if (jumlahPaket == 2) {
                    totalEstimasi = hargaProduk * 2;
                } else if (jumlahPaket == 1) {
                    totalEstimasi = hargaProduk;
                } else if (jumlahPaket == 3) {
                    totalEstimasi = hargaProduk * 3;
                }

                tv_totalEstimasi.setText(formatRupiah.format(totalEstimasi));
                tv_totalJumlahPaket.setText(jumlahPaket + " Paket");
            }
        });
        totalEstimasi = hargaProduk;

        //Volley Stuff
        listenerLanjutBelanja = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(TroliActivity.this, MainDrawerActivity.class);
                intent.putExtra("objekTroli", transaksi);
                intent.putExtra("totalEstimasi", totalEstimasi);
                startActivity(intent);
            }
        };

        listenerKeranjang = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intentItemKeranjang = new Intent(TroliActivity.this, ItemKeranjangActivity.class);
                intentItemKeranjang.putExtra("objekTroli", transaksi);
                intentItemKeranjang.putExtra("totalEstimasi", totalEstimasi);
                startActivity(intentItemKeranjang);
            }
        };

        errorListenerLanjutBelanja = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TroliActivity.this, error.toString(), Toast.LENGTH_LONG);
            }
        };
        errorListenerKeranjang = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TroliActivity.this, error.toString(), Toast.LENGTH_LONG);
            }
        };

        requestLanjutBelanja = new TambahItemRequest(MasukRequest.getEmail(), resep.getJudulResep(), jumlahPaket, listenerLanjutBelanja, errorListenerLanjutBelanja);
        requestKeranjang = new TambahItemRequest(MasukRequest.getEmail(),resep.getJudulResep(), jumlahPaket, listenerKeranjang, errorListenerKeranjang);
        queueLanjutBelanja = Volley.newRequestQueue(this);
        queueKeranjang = Volley.newRequestQueue(this);

        btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TroliActivity.this);
                builder.setMessage("Bahan masakan berhasil dimasukkan ke Keranjang Belanja")
                        .setPositiveButton("Keranjang", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                queueKeranjang.add(requestKeranjang);
                                Toast.makeText(TroliActivity.this,"Bahan masakan berhasil ditambahkan ke Keranjang",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Beranda", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                queueLanjutBelanja.add(requestLanjutBelanja);
                                Toast.makeText(TroliActivity.this,"Bahan masakan berhasil ditambahkan ke Keranjang",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
