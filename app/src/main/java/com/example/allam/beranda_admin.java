package com.example.allam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class beranda_admin extends AppCompatActivity {

    private CardView pendaftaran_admin, brosur_admin, verifikasi_admin, setting_admin;
    private ImageView profil_admin;
    private FirebaseUser fUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_admin);

        pendaftaran_admin = findViewById(R.id.btn_data_pendaftaran);
        brosur_admin = findViewById(R.id.btn_brosur_admin);
        verifikasi_admin = findViewById(R.id.btn_verifikasi_admin);
        setting_admin = findViewById(R.id.btn_seting_akun_admin);
        profil_admin = findViewById(R.id.set_profiladmin);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        profil_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), setting_akun_admin.class));
                finish();
            }
        });

        pendaftaran_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.allam.data_pendaftaran.class));
                finish();
            }
        });

        brosur_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.allam.brosur_admin.class));
                finish();
            }
        });

        verifikasi_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.allam.verifikasi.class));
                finish();
            }
        });

        setting_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), setting_akun_admin.class));
                finish();
            }
        });

    }

}