package com.example.allam.ui.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.allam.R;
import com.example.allam.ui.auth.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class setting_akun_admin extends AppCompatActivity {

    private Button logout_admin;
    private TextView nama_admin, email_admin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_akun_admin);

        CollectionReference cr = FirebaseFirestore.getInstance().collection("Users");
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference df = cr.document(userUid);

        logout_admin = findViewById(R.id.btn_logout_admin);
        nama_admin = findViewById(R.id.setting_nama_admin);
        email_admin = findViewById(R.id.setting_email_admin);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nama = documentSnapshot.getString("Username");
                    String email = documentSnapshot.getString("Email");

                    nama_admin.setText(nama);
                    email_admin.setText(email);
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

        logout_admin.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }
}