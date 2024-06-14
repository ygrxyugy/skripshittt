package com.example.allam.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.allam.R;
import com.example.allam.ui.admin.beranda_admin;
import com.example.allam.ui.user.beranda_user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText eEmail, ePassword;
    private Button Daftar, Masuk;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu!");
        progressDialog.setCancelable(true);

        eEmail = findViewById(R.id.etEmail);
        ePassword = findViewById(R.id.etPassword);
        Daftar = findViewById(R.id.btn_daftar);
        Masuk = findViewById(R.id.btn_masuk);

        Daftar.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), daftar.class));
        });

        Masuk.setOnClickListener(v -> {
            if (eEmail.getText().length() > 0 && ePassword.getText().length() > 0) {
                masuk(eEmail.getText().toString(), ePassword.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Data belum lengkap! silahkan isi terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void masuk(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                cekRole(authResult.getUser().getUid());
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Login gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cekRole(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess" + documentSnapshot.getData());

                //identifikasi role
                if (documentSnapshot.getString("isAdmin") != null) {
                    startActivity(new Intent(getApplicationContext(), beranda_admin.class));
                }
                if (documentSnapshot.getString("isUser") != null) {
                    startActivity(new Intent(getApplicationContext(), beranda_user.class));
                }
            }
        });
    }

    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("isAdmin") != null) {
                        startActivity(new Intent(getApplicationContext(), beranda_admin.class));
                        finish();
                    }
                    if (documentSnapshot.getString("isUser") != null) {
                        startActivity(new Intent(getApplicationContext(), beranda_user.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
        }
    }
}