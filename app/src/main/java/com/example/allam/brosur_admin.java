package com.example.allam;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class brosur_admin extends AppCompatActivity {

    private ImageView eBrosur_admin, kBrosur_admin;
    private Button btn_edit_brosur, btn_save_brosur;
    private ProgressDialog progressDialog;
    private String imageUrl;
    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brosur_admin);

        progressDialog = new ProgressDialog(brosur_admin.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu!");
        progressDialog.setCancelable(false);
        eBrosur_admin = findViewById(R.id.brosur_edit);
        btn_edit_brosur = findViewById(R.id.dbrosur);
        btn_save_brosur = findViewById(R.id.sbrosur);
        kBrosur_admin = findViewById(R.id.kembali_brosur_admin);

        kBrosur_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadSaveImage();

        btn_edit_brosur.setOnClickListener(v -> {
            edit_gambar();
        });

        btn_save_brosur.setOnClickListener(v -> {
            upload();
        });
    }

    private void loadSaveImage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        imageUrl = preferences.getString("imageUrl", null);
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(eBrosur_admin);
        }
    }

    private void edit_gambar() {
        final CharSequence[] items = {"Pilih dari galeri", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(brosur_admin.this);
        builder.setTitle(getString(R.string.app_name));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Pilih dari galeri")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), 20);
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void upload() {
        progressDialog.show();
        if (imageUri != null){
            eBrosur_admin.setDrawingCacheEnabled(true);
            eBrosur_admin.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) eBrosur_admin.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            FirebaseStorage fStorage = FirebaseStorage.getInstance();
            StorageReference reference = fStorage.getReference("Brosur/brosur");
            UploadTask uploadTask = reference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    saveImageUrl(imageUrl);
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Berhasil upload", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Pilih gambar terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUrl(String imageUrl) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("imageUrl", imageUrl);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Thread thread = new Thread(() -> {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    eBrosur_admin.post(() -> {
                        eBrosur_admin.setImageBitmap(bitmap);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}