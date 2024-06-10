package com.example.allam;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class beranda_user extends AppCompatActivity {

    private CardView btn_form, btn_brosur, btn_pengumuman, btn_media, btn_profil, btn_info;
    private ImageView beranda_profil;
    private FirebaseUser firebaseUser;
    private FirebaseMessaging fMsg;
    private FirebaseFirestore fStore;
    private TextView namauser;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_user);

        btn_form = findViewById(R.id.btn_form);
        btn_brosur = findViewById(R.id.btn_brosur);
        btn_pengumuman = findViewById(R.id.btn_pengumuman);
        btn_media = findViewById(R.id.btn_media);
        btn_profil = findViewById(R.id.btn_profil);
        btn_info = findViewById(R.id.btn_info);
        beranda_profil = findViewById(R.id.beranda_profil);
        namauser = findViewById(R.id.namauser);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        checkNotificationPermission();

//        fMsg.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if (!task.isSuccessful()) {
//                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                    return;
//                }
//                String token = task.getResult();
//                saveTokenToFirestore(token);
//            }
//        });
//
//        fMsg.getInstance().unsubscribeFromTopic("admin")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Unsubscribed from admin topic";
//                        if (!task.isSuccessful()) {
//                            msg = "Unsubscription from admin topic failed";
//                        }
//                        Log.d(TAG, msg);
//                        Toast.makeText(beranda_user.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });


        if (firebaseUser != null) {
            namauser.setText(firebaseUser.getDisplayName());
        } else {
            namauser.setText("Error!");
        }

        btn_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(beranda_user.this, halaman_form.class);
                startActivity(intent);
            }
        });

        beranda_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(beranda_user.this, setting_akun.class);
                startActivity(intent);
            }
        });

        btn_brosur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(beranda_user.this, halaman_brosur.class);
                startActivity(intent);
            }
        });

        btn_pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(beranda_user.this, halaman_pengumuman.class);
                startActivity(intent);
            }
        });

        btn_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(beranda_user.this, halaman_media.class);
                startActivity(intent);
            }
        });

        btn_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(beranda_user.this, setting_akun.class);
                startActivity(intent);
            }
        });

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(beranda_user.this, info.class);
                startActivity(intent);
            }
        });
    }
    private void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, tampilkan dialog untuk meminta izin
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
                new AlertDialog.Builder(this)
                        .setTitle("Notification Permission")
                        .setMessage("This app requires permission to send you notifications. Please grant the permission.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Minta izin setelah dialog ditutup
                            ActivityCompat.requestPermissions(beranda_user.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                        })
                        .create()
                        .show();
            } else {
                // Minta izin langsung
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("beranda_user", "Notification permission granted.");
                // Izin diberikan, lanjutkan operasi notifikasi
            } else {
                Log.d("beranda_user", "Notification permission denied.");
                // Izin ditolak, informasikan pengguna
            }
        }
    }


    private void saveTokenToFirestore(String token) {
        fStore = FirebaseFirestore.getInstance();
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);

        fStore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(tokenMap, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Token saved successfully"))
                .addOnFailureListener(e -> Log.w(TAG, "Error saving token", e));
    }

}