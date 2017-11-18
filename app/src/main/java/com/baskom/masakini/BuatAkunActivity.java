package com.baskom.masakini;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by akmalmuhamad on 18/11/17.
 */

public class BuatAkunActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buat_akun);

        final EditText etNama = (EditText) findViewById(R.id.etNama);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button btBuatAkun = (Button) findViewById(R.id.btBuatAkun);

        final TextView loginLink = (TextView) findViewById(R.id.textLogin);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(BuatAkunActivity.this, LoginActivity.class);
                BuatAkunActivity.this.startActivity(loginIntent);
            }
        });

    }

}
