package com.example.allam.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allam.R;
import com.example.allam.ui.auth.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class setting_akun extends AppCompatActivity {

    private ImageView kembali_profil;
    private TextView nama_user, email_user, nomor_user;
    private Button logout_user;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_akun);

        nama_user = findViewById(R.id.setting_nama_user);
        email_user = findViewById(R.id.setting_email_user);
        nomor_user = findViewById(R.id.setting_nomor_telepon);
        logout_user = findViewById(R.id.btn_logout_user);
        kembali_profil = findViewById(R.id.kembali_profil);

        CollectionReference cr = FirebaseFirestore.getInstance().collection("Users");
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = cr.document(userUid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nama = documentSnapshot.getString("Username");
                    String email = documentSnapshot.getString("Email");
                    String nomor = documentSnapshot.getString("Nomor");

                    nama_user.setText(nama);
                    email_user.setText(email);
                    nomor_user.setText(nomor);

                } else {
                    Toast.makeText(getApplicationContext(), "Pengguna tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal menemukan data pengguna!", Toast.LENGTH_SHORT).show();
            }
        });

        kembali_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        logout_user.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

    }
}