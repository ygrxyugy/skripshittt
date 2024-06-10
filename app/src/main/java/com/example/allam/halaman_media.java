package com.example.allam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class halaman_media extends AppCompatActivity {

    private ImageView kembali_media;
    private CardView link_ig, link_yt, link_tiktok, link_fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_media);

        link_ig = findViewById(R.id.bInstagram);
        link_fb = findViewById(R.id.bFacebook);
        link_tiktok = findViewById(R.id.bTiktok);
        link_yt = findViewById(R.id.bYoutube);

        link_yt.setOnClickListener(v -> {
            String url = "https://bit.ly/44FMuYv";

            // Membuat intent untuk membuka browser dan membuka URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // Memulai aktivitas dengan intent
            startActivity(intent);
        });

        link_fb.setOnClickListener(v -> {
            String url = "https://bit.ly/3UB5FxL";

            // Membuat intent untuk membuka browser dan membuka URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // Memulai aktivitas dengan intent
            startActivity(intent);
        });

        link_ig.setOnClickListener(v -> {
            String url = "https://linktw.in/nelysZ";

            // Membuat intent untuk membuka browser dan membuka URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // Memulai aktivitas dengan intent
            startActivity(intent);
        });

        link_tiktok.setOnClickListener(v -> {
            String url = "https://bit.ly/4dFBoXx";

            // Membuat intent untuk membuka browser dan membuka URL
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // Memulai aktivitas dengan intent
            startActivity(intent);
        });

        kembali_media = findViewById(R.id.kembali_media);

        kembali_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onBackPressed();

            }
        });
    }
}