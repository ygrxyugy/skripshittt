package com.example.allam.ui.user;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
//        checkNotificationPermission();

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

        //perizinan
        askNotificationPermission();

        fMsg.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        saveTokenToFirestore(token);
                    }
                });
        //subscribe topik
        fMsg.getInstance().subscribeToTopic("user_notification")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                        String msg = "Subscribed to user_notification";
                        if (!task.isSuccessful()) {
                            msg = "Subscription failed";
                        }
                        Log.d("FCM", msg);
                    }
                });
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {

                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
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
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
//    private void checkNotificationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // Jika izin belum diberikan, tampilkan dialog untuk meminta izin
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
//                new AlertDialog.Builder(this)
//                        .setTitle("Notification Permission")
//                        .setMessage("This app requires permission to send you notifications. Please grant the permission.")
//                        .setPositiveButton("OK", (dialog, which) -> {
//                            // Minta izin setelah dialog ditutup
//                            ActivityCompat.requestPermissions(beranda_user.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
//                        })
//                        .create()
//                        .show();
//            } else {
//                // Minta izin langsung
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
//            }
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d("beranda_user", "Notification permission granted.");
//                // Izin diberikan, lanjutkan operasi notifikasi
//            } else {
//                Log.d("beranda_user", "Notification permission denied.");
//                // Izin ditolak, informasikan pengguna
//            }
//        }
//    }


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