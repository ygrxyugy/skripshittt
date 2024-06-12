package com.example.allam;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;


public class beranda_admin extends AppCompatActivity {

    private CardView pendaftaran_admin, brosur_admin, verifikasi_admin, setting_admin;
    private ImageView profil_admin;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;
    private FirebaseMessaging fMsg;

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

//        fMsg.getInstance().subscribeToTopic("admin_notification")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Subscribed to admin_notifications";
//                        if (!task.isSuccessful()) {
//                            msg = "Subscription failed";
//                        }
//                        Log.d("FCM", msg);
//                        Toast.makeText(beranda_admin.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });

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