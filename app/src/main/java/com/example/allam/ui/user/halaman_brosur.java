package com.example.allam.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.app.DownloadManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.allam.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class halaman_brosur extends AppCompatActivity {

    private ImageView kembali_brosur, tampil_brosur;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Uri imageUrl;
    private String imageUriString;
    private FirebaseStorage fStorage;
    private StorageReference sReference;
    private Button dbrosur;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_brosur);

        //firebase
        fStorage = FirebaseStorage.getInstance();
        sReference = fStorage.getReference().child("Brosur/brosur");

        dbrosur = findViewById(R.id.dbrosur);
        kembali_brosur = findViewById(R.id.kembali_brosur);
        tampil_brosur = findViewById(R.id.brosur_user);

        imageUriString = PreferenceManager.getDefaultSharedPreferences(this).getString("imageUrl", null);
        if (imageUriString != null) {
            imageUrl = Uri.parse(imageUriString);
            Picasso.get().load(imageUrl).into(tampil_brosur);
        }

        dbrosur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    downloadImage();
                } else {
                    requestPermissions();
                }
            }
        });

        kembali_brosur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(halaman_brosur.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadImage();
            } else {
                Toast.makeText(getApplicationContext(), "Perizinan tidak tersedia!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void downloadImage() {
        sReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                startDownload(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof StorageException) {
                    Toast.makeText(getApplicationContext(), "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startDownload(String url) {
        DownloadManager download = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Brosur_alfalah.jpg");

        download.enqueue(request);
        Toast.makeText(getApplicationContext(), "Sedang mendownload...", Toast.LENGTH_SHORT).show();
    }
}