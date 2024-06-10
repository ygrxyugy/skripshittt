package com.example.allam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class daftar extends AppCompatActivity {

    private EditText daftarUsername, daftarEmail, daftarPassword, daftarNomor;
    private ConstraintLayout kembali;
    private Button btnDaftar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(daftar.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu!");
        progressDialog.setCancelable(true);

        daftarUsername = findViewById(R.id.etUsernamedaftar);
        daftarEmail = findViewById(R.id.etEmaildaftar);
        daftarPassword = findViewById(R.id.etPassworddaftar);
        daftarNomor = findViewById(R.id.etNomortelepon);
        kembali = findViewById(R.id.cl1);
        btnDaftar = findViewById(R.id.btn_daftar);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDaftar.setOnClickListener(v -> {
            if (daftarUsername.getText().length() > 0 && daftarNomor.getText().length() > 0 && daftarEmail.getText().length() > 0 && daftarPassword.getText().length() > 0) {
                Daftar(daftarUsername.getText().toString(),daftarNomor.getText().toString() ,daftarEmail.getText().toString(), daftarPassword.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Silahkan melengkapi data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Daftar(String username,String nomor, String email, String password) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    DocumentReference df = fStore.collection("Users").document(firebaseUser.getUid());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Username", username.toString());
                    userInfo.put("Email", email.toString());
                    userInfo.put("Nomor", nomor.toString());
                    //
                    userInfo.put("isUser", "1");
                    df.set(userInfo);

                    if (firebaseUser != null) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Daftar gagal!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void reload() {
        startActivity(new Intent(getApplicationContext(), beranda_user.class));
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
}