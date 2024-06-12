package com.example.allam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class halaman_form extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE_1 = 1;
    private static final int REQUEST_CODE_IMAGE_2 = 2;
    private static final int REQUEST_CODE_IMAGE_3 = 3;
    private static final int REQUEST_CODE_IMAGE_4 = 4;
    private Calendar kalender;
    private DatePickerDialog datePickerDialog;

    private ImageView kembali;
    private EditText etNama, etTempatlahir, etTanggal, etNik, etAsalmadrasah, etPendidikan, etAlamat, etRt, etRw,
            etDesa, etKecamatan, etKodepos, etKabupaten, etProvinsi, etNamaayah, etNikayah, etPendidikanayah, etPekerjaanayah, etNamaibu,
            etNikibu, etPendidikanibu, etPekerjaanibu, etNomorortu, etAkta, etKk, etIjazah, etFoto;
    private RadioGroup rGender;
    private RadioButton rLaki, rCewe;
    private Button bAkta, bKartukeluarga, bIjazah, bFoto, bTanggal, bSimpan;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseStorage fStorage;
    private FirebaseMessaging fMsg;
    private StorageReference sReference;
    private String username;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_form);

        //progresdialog
        progressDialog = new ProgressDialog(halaman_form.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu!");
        progressDialog.setCancelable(false);

        //firebase
        fStore = FirebaseFirestore.getInstance();
        sReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fStorage = FirebaseStorage.getInstance();
        fUser = fAuth.getCurrentUser();

        //formsantri
        etNama = findViewById(R.id.etNamauser);
        etTempatlahir = findViewById(R.id.etTempat);
        etTanggal = findViewById(R.id.etTanggal);
        etNik = findViewById(R.id.etNik);
        etAsalmadrasah = findViewById(R.id.etAsalmadrasah);
        etPendidikan = findViewById(R.id.etPendidikanformal);
        etAlamat = findViewById(R.id.etAlamat);
        etRt = findViewById(R.id.etRt);
        etRw = findViewById(R.id.etRw);
        etDesa = findViewById(R.id.etDesa);
        etKecamatan = findViewById(R.id.etKecamatan);
        etKodepos = findViewById(R.id.etKodepos);
        etKabupaten = findViewById(R.id.etKabupaten);
        etProvinsi = findViewById(R.id.etProvinsi);
        bTanggal = findViewById(R.id.btn_tanggal);

        rGender = findViewById(R.id.radio1);
        rLaki = findViewById(R.id.rLaki);
        rCewe = findViewById(R.id.rCewe);

        //formorangtua
        etNamaayah = findViewById(R.id.etNamaayah);
        etNikayah = findViewById(R.id.etNikayah);
        etPendidikanayah = findViewById(R.id.etPendidikanformalayah);
        etPekerjaanayah = findViewById(R.id.etPekerjaanayah);
        etNamaibu = findViewById(R.id.etNamaibu);
        etNikibu = findViewById(R.id.etNikibu);
        etPendidikanibu = findViewById(R.id.etPendidikanformalibu);
        etPekerjaanibu = findViewById(R.id.etPekerjaanibu);
        etNomorortu = findViewById(R.id.etNomorortu);
        etAkta = findViewById(R.id.etAkta);
        etKk = findViewById(R.id.etKartukeluarga);
        etIjazah = findViewById(R.id.etIjazahmadrasah);
        etFoto = findViewById(R.id.etPasfoto);
        kembali = findViewById(R.id.kembali_form);

        //uploaddata
        bAkta = findViewById(R.id.btn_akta);
        bKartukeluarga = findViewById(R.id.btn_kk);
        bIjazah = findViewById(R.id.btn_ijazah);
        bFoto = findViewById(R.id.btn_pasfoto);

        loadData();

        bSimpan = findViewById(R.id.btn_simpan_form);
        bSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        if (fUser != null) {
            fetchUsername(fUser.getUid());
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = 0;
                if (v.getId() == R.id.btn_akta) {
                    requestCode = REQUEST_CODE_IMAGE_1;
                } else if (v.getId() == R.id.btn_kk) {
                    requestCode = REQUEST_CODE_IMAGE_2;
                } else if (v.getId() == R.id.btn_ijazah) {
                    requestCode = REQUEST_CODE_IMAGE_3;
                } else if (v.getId() == R.id.btn_pasfoto) {
                    requestCode = REQUEST_CODE_IMAGE_4;
                }
                openImageChooser(requestCode);
            }
        };

        bAkta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(REQUEST_CODE_IMAGE_1);
            }
        });
        bKartukeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(REQUEST_CODE_IMAGE_2);
            }
        });
        bIjazah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(REQUEST_CODE_IMAGE_3);
            }
        });
        bFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(REQUEST_CODE_IMAGE_4);
            }
        });
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void loadData() {
        DocumentReference docRef = fStore.collection("Form").document(fUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    disableForm();
                    fillForm(documentSnapshot);
                    Toast.makeText(getApplicationContext(),
                            "Anda sudah mengisi formulir!", Toast.LENGTH_SHORT).show();
                } else {
                    // form bisa diisi
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Gagal memuat data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableForm() {
        etNama.setEnabled(false);
        etTempatlahir.setEnabled(false);
        etTanggal.setEnabled(false);
        etNik.setEnabled(false);
        etAsalmadrasah.setEnabled(false);
        etPendidikan.setEnabled(false);
        etAlamat.setEnabled(false);
        etRt.setEnabled(false);
        etRw.setEnabled(false);
        etDesa.setEnabled(false);
        etKecamatan.setEnabled(false);
        etKodepos.setEnabled(false);
        etKabupaten.setEnabled(false);
        etProvinsi.setEnabled(false);
        bTanggal.setEnabled(false);
        rGender.setEnabled(false);
        rLaki.setEnabled(false);
        rCewe.setEnabled(false);
        etNamaayah.setEnabled(false);
        etNikayah.setEnabled(false);
        etPendidikanayah.setEnabled(false);
        etPekerjaanayah.setEnabled(false);
        etNamaibu.setEnabled(false);
        etNikibu.setEnabled(false);
        etPendidikanibu.setEnabled(false);
        etPekerjaanibu.setEnabled(false);
        etNomorortu.setEnabled(false);
        bAkta.setEnabled(false);
        bKartukeluarga.setEnabled(false);
        bIjazah.setEnabled(false);
        bFoto.setEnabled(false);
        bSimpan.setEnabled(false);
    }

    private void fillForm(DocumentSnapshot documentSnapshot) {
        String namaLengkap = documentSnapshot.getString("Nama Lengkap");
        String tempatLahir = documentSnapshot.getString("Tempat Lahir");
        String tanggalLahir = documentSnapshot.getString("Tanggal Lahir");
        String nik = documentSnapshot.getString("NIK");
        String jenisKelamin = documentSnapshot.getString("Jenis Kelamin");
        String asalMadrasah = documentSnapshot.getString("Asal Madrasah");
        String pendidikanFormal = documentSnapshot.getString("Pendidikan Formal");
        String alamat = documentSnapshot.getString("Alamat");
        String rt = documentSnapshot.getString("Rt");
        String rw = documentSnapshot.getString("Rw");
        String desa = documentSnapshot.getString("Desa");
        String kecamatan = documentSnapshot.getString("Kecamatan");
        String kodePos = documentSnapshot.getString("Kode Pos");
        String kabupaten = documentSnapshot.getString("Kabupaten");
        String provinsi = documentSnapshot.getString("Provinsi");
        String namaAyah = documentSnapshot.getString("Nama Ayah");
        String nikAyah = documentSnapshot.getString("Nik Ayah");
        String pendidikanAyah = documentSnapshot.getString("Pendidikan Ayah");
        String pekerjaanAyah = documentSnapshot.getString("Pekerjaan Ayah");
        String namaIbu = documentSnapshot.getString("Nama Ibu");
        String nikIbu = documentSnapshot.getString("Nik Ibu");
        String pendidikanIbu = documentSnapshot.getString("Pendidikan Ibu");
        String pekerjaanIbu = documentSnapshot.getString("Pekerjaan Ibu");
        String nomorOrtu = documentSnapshot.getString("Nomor Orangtua");

        //set value
        etNama.setText(namaLengkap);
        etTempatlahir.setText(tempatLahir);
        etTanggal.setText(tanggalLahir);
        etNik.setText(nik);
        if (jenisKelamin.equals("Laki laki")) {
            rGender.check(R.id.rLaki);
        } else if (jenisKelamin.equals("Perempuan")) {
            rGender.check(R.id.rCewe);
        }
        etAsalmadrasah.setText(asalMadrasah);
        etPendidikan.setText(pendidikanFormal);
        etAlamat.setText(alamat);
        etRt.setText(rt);
        etRw.setText(rw);
        etDesa.setText(desa);
        etKecamatan.setText(kecamatan);
        etKodepos.setText(kodePos);
        etKabupaten.setText(kabupaten);
        etProvinsi.setText(provinsi);
        etNamaayah.setText(namaAyah);
        etNikayah.setText(nikAyah);
        etPendidikanayah.setText(pendidikanAyah);
        etPekerjaanayah.setText(pekerjaanAyah);
        etNamaibu.setText(namaIbu);
        etNikibu.setText(nikIbu);
        etPendidikanibu.setText(pendidikanIbu);
        etPekerjaanibu.setText(pekerjaanIbu);
        etNomorortu.setText(nomorOrtu);

        if (fUser != null) {
            StorageReference sAkta = fStorage.getReference().child("folderAkta/");
            StorageReference sKartuKeluarga = fStorage.getReference().child("folderKartuKeluarga/");
            StorageReference sIjazah = fStorage.getReference().child("folderIjazah/");
            StorageReference sFoto = fStorage.getReference().child("folderPasFoto/");

            getImage(sAkta, etAkta);
            getImage(sKartuKeluarga, etKk);
            getImage(sIjazah, etIjazah);
            getImage(sFoto, etFoto);
        }
    }

    private void getImage(StorageReference sReference, EditText editText) {
        sReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        StringBuilder imageName = new StringBuilder();
                        for (StorageReference item : listResult.getItems()) {
                            imageName.append(item.getName()).append("\n");
                        }
                        editText.setText(imageName.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Error getting image names", e);
                        editText.setText("Error getting image names");
                    }
                });
    }

    private void fetchUsername(String uid) {
        fStore.collection("Users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            username = documentSnapshot.getString("Username");
                        } else {
                            Toast.makeText(getApplicationContext(), "Username tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selectImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.show();
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri, requestCode);
        }
    }

    private void uploadImageToFirebase(Uri imageUri, final int requestCode) {
        if (fUser != null && username != null) {
            String folder;
            String imageName;
            switch (requestCode) {
                case REQUEST_CODE_IMAGE_1:
                    folder = "folderAkta/";
                    imageName = "Akta_" + username + ".jpg";
                    break;
                case REQUEST_CODE_IMAGE_2:
                    folder = "folderKartuKeluarga/";
                    imageName = "KK_" + username + ".jpg";
                    break;
                case REQUEST_CODE_IMAGE_3:
                    folder = "folderIjazah/";
                    imageName = "Ijazah_" + username + ".jpg";
                    break;
                case REQUEST_CODE_IMAGE_4:
                    folder = "folderPasFoto/";
                    imageName = "Foto_" + username + ".jpg";
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected request code: " + requestCode);
            }
            final StorageReference fileReference = sReference.child(folder + imageName);
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    switch (requestCode) {
                                        case REQUEST_CODE_IMAGE_1:
                                            etAkta.setText(imageName);
                                            progressDialog.dismiss();
                                            break;
                                        case REQUEST_CODE_IMAGE_2:
                                            etKk.setText(imageName);
                                            progressDialog.dismiss();
                                            break;
                                        case REQUEST_CODE_IMAGE_3:
                                            etIjazah.setText(imageName);
                                            progressDialog.dismiss();
                                            break;
                                        case REQUEST_CODE_IMAGE_4:
                                            etFoto.setText(imageName);
                                            progressDialog.dismiss();
                                            break;
                                    }
                                    Toast.makeText(getApplicationContext(), "Gambar berhasil dipilih", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Gagal upload", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "Gagal menemukan pengguna", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadData() {
        progressDialog.show();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        //santri
        String NamaLengkap = etNama.getText().toString();
        String TempatLahir = etTempatlahir.getText().toString();
        String TanggalLahir = etTanggal.getText().toString();
        String NIK = etNik.getText().toString();
        String AsalMadrasah = etAsalmadrasah.getText().toString();
        String PendidikanFormal = etPendidikan.getText().toString();
        String Alamat = etAlamat.getText().toString();
        String Rt = etRt.getText().toString();
        String Rw = etRw.getText().toString();
        String Desa = etDesa.getText().toString();
        String Kecamatan = etKecamatan.getText().toString();
        String KodePos = etKodepos.getText().toString();
        String Kabupaten = etKabupaten.getText().toString();
        String Provinsi = etProvinsi.getText().toString();

        int selectedGenderId = rGender.getCheckedRadioButtonId();
        String Gender = "";
        if (selectedGenderId == rLaki.getId()) {
            Gender = "Laki-laki";
        } else if (selectedGenderId == rCewe.getId()) {
            Gender = "Perempuan";
        }

        //orangtua
        String NamaAyah = etNamaayah.getText().toString();
        String NikAyah = etNikayah.getText().toString();
        String PendidikanAyah = etPendidikanayah.getText().toString();
        String PekerjaanAyah = etPekerjaanayah.getText().toString();
        String NamaIbu = etNamaibu.getText().toString();
        String NikIbu = etNikibu.getText().toString();
        String PendidikanIbu = etPendidikanibu.getText().toString();
        String PekerjaanIbu = etPekerjaanibu.getText().toString();
        String NomorOrangtua = etNomorortu.getText().toString();

        String Akta = etAkta.getText().toString();
        String KK = etKk.getText().toString();
        String Ijazah = etIjazah.getText().toString();
        String Pasfoto = etFoto.getText().toString();

        //validasi
        if (NamaLengkap.isEmpty() || TempatLahir.isEmpty() || TanggalLahir.isEmpty() || NIK.isEmpty() || Gender.isEmpty() ||
                AsalMadrasah.isEmpty() || PendidikanFormal.isEmpty() || Alamat.isEmpty() || Rt.isEmpty() ||
                Rw.isEmpty() || Desa.isEmpty() || Kecamatan.isEmpty() || KodePos.isEmpty() || Kabupaten.isEmpty() ||
                Provinsi.isEmpty() || NamaAyah.isEmpty() || NikAyah.isEmpty() || PendidikanAyah.isEmpty() ||
                PekerjaanAyah.isEmpty() || NamaIbu.isEmpty() || NikIbu.isEmpty() || PendidikanIbu.isEmpty() ||
                PekerjaanIbu.isEmpty() || NomorOrangtua.isEmpty() || Akta.isEmpty() || KK.isEmpty()
                || Ijazah.isEmpty() || Pasfoto.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Tolong masukkan data!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        //map menyimpan data
        Map<String, Object> user = new HashMap<>();
        user.put("Nama Lengkap", NamaLengkap);
        user.put("Tempat Lahir", TempatLahir);
        user.put("Tanggal Lahir", TanggalLahir);
        user.put("Jenis Kelamin", Gender);
        user.put("NIK", NIK);
        user.put("Asal Madrasah", AsalMadrasah);
        user.put("Pendidikan Formal", PendidikanFormal);
        user.put("Alamat", Alamat);
        user.put("Rt", Rt);
        user.put("Rw", Rw);
        user.put("Desa", Desa);
        user.put("Kecamatan", Kecamatan);
        user.put("Kode Pos", KodePos);
        user.put("Kabupaten", Kabupaten);
        user.put("Provinsi", Provinsi);
        //orangtua
        user.put("Nama Ayah", NamaAyah);
        user.put("Nik Ayah", NikAyah);
        user.put("Pendidikan Ayah", PendidikanAyah);
        user.put("Pekerjaan Ayah", PekerjaanAyah);
        user.put("Nama Ibu", NamaIbu);
        user.put("Nik Ibu", NikIbu);
        user.put("Pendidikan Ibu", PendidikanIbu);
        user.put("Pekerjaan Ibu", PekerjaanIbu);
        user.put("Nomor Orangtua", NomorOrangtua);
        user.put("Akta", Akta);
        user.put("KK", KK);
        user.put("Ijazah", Ijazah);
        user.put("Pas Foto", Pasfoto);

        //Tambah ke Firestore
        fStore.collection("Form").document(fUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        sendNotifToServer();
                        Toast.makeText(getApplicationContext(), "Berhasil simpan data!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), beranda_user.class));
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void sendNotifToServer() {
        try {
            String serviceAccountKeyPath = "app/serviceAccount.json";
            String accessToken = GoogleAuthUtil.getAccessToken(serviceAccountKeyPath);

            OkHttpClient client = new OkHttpClient.Builder().build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            fcmService service = retrofit.create(fcmService.class);

            Notification notification = new Notification("Test Title", "Test Body");
            notifikasiRequest request = new notifikasiRequest();
            request.setTo("user_device_token");
            request.setNotification(notification);

            Call<notifikasiResponse> call = service.sendNotification("Bearer " + accessToken, request);
            notifikasiResponse response = call.execute().body();

            if (response != null) {
                System.out.println("Successfully sent message: " + response.getMessage_id());
            } else {
                System.out.println("Failed to send message.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerDialog() {
        final Calendar kalender = Calendar.getInstance();
        int year = kalender.get(Calendar.YEAR);
        int month = kalender.get(Calendar.MONTH);
        int day = kalender.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(
                halaman_form.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Note: month is 0-based, so we add 1 to it
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        etTanggal.setText(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void openImageChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), requestCode);
    }
}