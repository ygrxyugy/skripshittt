package com.example.allam.ui.admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.allam.R;
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

        fMsg.getInstance().subscribeToTopic("admin_notification")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to admin_notifications";
                        if (!task.isSuccessful()) {
                            msg = "Subscription failed";
                        }
                        Log.d("FCM", msg);
                    }
                });
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
                startActivity(new Intent(getApplicationContext(), data_pendaftaran.class));
                finish();
            }
        });

        brosur_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.allam.ui.admin.brosur_admin.class));
                finish();
            }
        });

        verifikasi_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), verifikasi.class));
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission();
        }

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        // Meminta izin dari pengguna

    }

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Toast.makeText(getApplicationContext(), "Notifikasi sudah aktif", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(getApplicationContext(), "Notifikasi belum aktif", Toast.LENGTH_SHORT).show();

                }
            });

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(beranda_admin.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, tampilkan dialog untuk meminta izin
            if (ActivityCompat.shouldShowRequestPermissionRationale(beranda_admin.this, Manifest.permission.POST_NOTIFICATIONS)) {
                new AlertDialog.Builder(this)
                        .setTitle("Notification Permission")
                        .setMessage("This app requires permission to send you notifications. Please grant the permission.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Minta izin setelah dialog ditutup
                            ActivityCompat.requestPermissions(beranda_admin.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                        })
                        .create()
                        .show();
            } else {
                // Minta izin langsung
                ActivityCompat.requestPermissions(beranda_admin.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
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