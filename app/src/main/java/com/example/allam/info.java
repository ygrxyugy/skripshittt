package com.example.allam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class info extends AppCompatActivity {

    private ImageView kembali_info;
    private ConstraintLayout iInstagram, iEmail;
    private TextView iText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        kembali_info = findViewById(R.id.kembali_info);
        iInstagram = findViewById(R.id.info_ig);
        iEmail = findViewById(R.id.info_email);
        iText = findViewById(R.id.info_text);

        iText.setText(R.string.info_aplikasi);

        iInstagram.setOnClickListener(v -> {
            String url = "https://linktw.in/uANOpp";

            // Membuat intent untuk membuka browser dan membuka URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // Memulai aktivitas dengan intent
            startActivity(intent);
        });

        iEmail.setOnClickListener(v -> {
            String url = "mailto:dhiyaulallam01@gmail.com";

            // Membuat intent untuk membuka browser dan membuka URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // Memulai aktivitas dengan intent
            startActivity(intent);
        });

        kembali_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
    }
}