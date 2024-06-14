package com.example.allam.data;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.allam.R;
import com.example.allam.ui.admin.verifikasi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class lihat_data extends AppCompatActivity {
    private EditText dpNama, dpTempatlahir, dpTanggal, dpNik, dpGender, dpAsalmadrasah, dpPendidikan, dpAlamat, dpRt, dpRw,
            dpDesa, dpKecamatan, dpKodepos, dpKabupaten, dpProvinsi, dpNamaayah, dpNikayah, dpPendidikanayah, dpPekerjaanayah, dpNamaibu,
            dpNikibu, dpPendidikanibu, dpPekerjaanibu, dpNomorortu;
    private ImageView dpAkta, dpKk, dpIjazah, dpFoto;
    private FirebaseFirestore fStore;
    private StorageReference sReference;
    private Button btn_lolos, btn_gagal;
    private String documentId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);

        //firebase
        fStore = FirebaseFirestore.getInstance();
        sReference = FirebaseStorage.getInstance().getReference();

        //Button
        btn_lolos = findViewById(R.id.btn_lolos);
        btn_gagal = findViewById(R.id.btn_gagal);
        btn_lolos.setOnClickListener(v -> updateStatus("Lolos"));
        btn_gagal.setOnClickListener(v -> updateStatus("Tidak Lolos"));

        //data santri
        dpNama = findViewById(R.id.dpNamauser);
        dpTempatlahir = findViewById(R.id.dpTempat);
        dpTanggal = findViewById(R.id.dpTanggal);
        dpNik = findViewById(R.id.dpNik);
        dpGender = findViewById(R.id.dpGender);
        dpAsalmadrasah = findViewById(R.id.dpAsalmadrasah);
        dpPendidikan = findViewById(R.id.dpPendidikanformal);
        dpAlamat = findViewById(R.id.dpAlamat);
        dpRt = findViewById(R.id.dpRt);
        dpRw = findViewById(R.id.dpRw);
        dpDesa = findViewById(R.id.dpDesa);
        dpKecamatan = findViewById(R.id.dpKecamatan);
        dpKodepos = findViewById(R.id.dpKodepos);
        dpKabupaten = findViewById(R.id.dpKabupaten);
        dpProvinsi = findViewById(R.id.dpProvinsi);
        dpNamaayah = findViewById(R.id.dpNamaayah);
        dpNikayah = findViewById(R.id.dpNikayah);
        dpPendidikanayah = findViewById(R.id.dpPendidikanformalayah);
        dpPekerjaanayah = findViewById(R.id.dpPekerjaanayah);
        dpNamaibu = findViewById(R.id.dpNamaibu);
        dpNikibu = findViewById(R.id.dpNikibu);
        dpPendidikanibu = findViewById(R.id.dpPendidikanformalibu);
        dpPekerjaanibu = findViewById(R.id.dpPekerjaanibu);
        dpNomorortu = findViewById(R.id.dpNomorortu);

        dpAkta = findViewById(R.id.dpAkta);
        dpKk = findViewById(R.id.dpKartukeluarga);
        dpIjazah = findViewById(R.id.dpIjazahmadrasah);
        dpFoto = findViewById(R.id.dpPasfoto);

        String namaLengkap = getIntent().getStringExtra("namaLengkap");

        getUserData(namaLengkap);
        getUserImage(namaLengkap);

    }

    private void updateStatus(String status) {
        if (documentId != null) {
            DocumentReference docRef = fStore.collection("Form").document(documentId);
            docRef.update("Status", status)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "Status diperbarui " + status, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(lihat_data.this, verifikasi.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(lihat_data.this, "Gagal memperbarui status" + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void getUserImage(String namaLengkap) {
        //load image
        loadImage("folderAkta/Akta_" + namaLengkap + ".jpg", dpAkta);
        loadImage("folderKartuKeluarga/KK_" + namaLengkap + ".jpg", dpKk);
        loadImage("folderIjazah/Ijazah_" + namaLengkap + ".jpg", dpIjazah);
        loadImage("folderPasFoto/Foto_" + namaLengkap + ".jpg", dpFoto);
    }

    private void loadImage(String path, ImageView imageView) {
        StorageReference imageRef = sReference.child(path);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(lihat_data.this, "Gagal memuat gambar!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserData(String namaLengkap) {
        fStore.collection("Form")
                .whereEqualTo("Nama Lengkap", namaLengkap)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        documentId = document.getId();
                        //set value
                        dpNama.setText(document.getString("Nama Lengkap"));
                        dpTempatlahir.setText(document.getString("Tempat Lahir"));
                        dpTanggal.setText(document.getString("Tanggal Lahir"));
                        dpNik.setText(document.getString("NIK"));
                        dpGender.setText(document.getString("Jenis Kelamin"));
                        dpAsalmadrasah.setText(document.getString("Asal Madrasah"));
                        dpPendidikan.setText(document.getString("Pendidikan Formal"));
                        dpAlamat.setText(document.getString("Alamat"));
                        dpRt.setText(document.getString("Rt"));
                        dpRw.setText(document.getString("Rw"));
                        dpDesa.setText(document.getString("Desa"));
                        dpKecamatan.setText(document.getString("Kecamatan"));
                        dpKodepos.setText(document.getString("Kode Pos"));
                        dpKabupaten.setText(document.getString("Kabupaten"));
                        dpProvinsi.setText(document.getString("Provinsi"));
                        dpNamaayah.setText(document.getString("Nama Ayah"));
                        dpNikayah.setText(document.getString("Nik Ayah"));
                        dpPendidikanayah.setText(document.getString("Pendidikan Ayah"));
                        dpPekerjaanayah.setText(document.getString("Pekerjaan Ayah"));
                        dpNamaibu.setText(document.getString("Nama Ibu"));
                        dpNikibu.setText(document.getString("Nik Ibu"));
                        dpPendidikanibu.setText(document.getString("Pendidikan Ibu"));
                        dpPekerjaanibu.setText(document.getString("Pekerjaan Ibu"));
                        dpNomorortu.setText(document.getString("Nomor Orangtua"));
                    } else {
                        Toast.makeText(lihat_data.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }

                });
    }
}

