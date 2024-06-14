package com.example.allam.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allam.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class halaman_pengumuman extends AppCompatActivity {

    ImageView kembali_pengumuman;
    TextView tvPengumuman, tvNama, tvTanggal, tvTempat;
    ConstraintLayout bg_pengumuman;
    FirebaseFirestore fStore;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_pengumuman);

        tvPengumuman = findViewById(R.id.pengumuman);
        tvNama = findViewById(R.id.tvNama);
        tvTanggal = findViewById(R.id.vTanggal);
        tvTempat = findViewById(R.id.vTempat);
        bg_pengumuman = findViewById(R.id.bg_pengumuman);
        kembali_pengumuman = findViewById(R.id.kembali_pengumuman);

        //firebase
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        getData();

        kembali_pengumuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {onBackPressed();

            }
        });
    }

    private void getData() {
        DocumentReference docRef = fStore.collection("Form").document(fUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String vNama = documentSnapshot.getString("Nama Lengkap");
                    String vTanggal = documentSnapshot.getString("Tanggal Lahir");
                    String vTempat = documentSnapshot.getString("Tempat Lahir");
                    String vPengumuman = documentSnapshot.getString("Status");

                    tvNama.setText(vNama);
                    tvTanggal.setText(vTanggal);
                    tvTempat.setText(vTempat);

                    getPengumuman(vPengumuman);


                } else {
                    Toast.makeText(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal memuat data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPengumuman(String vPengumuman) {
        if (vPengumuman == null) {
            tvPengumuman.setText("SILAHKAN MENUNGGU BERKAS ANDA DISELEKSI");
            bg_pengumuman.setBackgroundResource(R.color.abu);
        } else if (vPengumuman.equals("Lolos")) {
            tvPengumuman.setText("SELAMAT BERKAS ANDA DINYATAKAN LOLOS SELEKSI ADMINISTRASI");
            bg_pengumuman.setBackgroundResource(R.color.warna2);
        } else if (vPengumuman.equals("Tidak Lolos")) {
            tvPengumuman.setText("MOHON MAAF BERKAS ANDA BELUM LOLOS SELEKSI ADMINISTRASI");
            bg_pengumuman.setBackgroundResource(R.color.merah);
        }
    }
}