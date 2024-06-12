package com.example.allam;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allam.adapter.FormAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class data_pendaftaran extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private RecyclerView recyclerView;
    private FormAdapter formAdapter;
    private ImageView kembali_data, btn_next, btn_prev;
    private List<DocumentSnapshot> lastDoc = new ArrayList<>();
    private boolean isLoading = false;
    private int itemPage = 7;
    private TextView pageNumber;
    private int currentPage = 0;
    private List<String> namaLengkapList = new ArrayList<>();
    private List<String> userIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pendaftaran);

        fStore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        pageNumber = findViewById(R.id.pageNumber);
        kembali_data = findViewById(R.id.kembali_data);
        btn_next = findViewById(R.id.next);
        btn_prev = findViewById(R.id.prev);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading && (lastDoc.size() > currentPage || currentPage == 0)) {
                    currentPage++;
                    loadPage();
                }
            }
        });
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 0) {
                    currentPage--;
                    loadPage();
                }
            }
        });
        kembali_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        formAdapter = new FormAdapter(namaLengkapList, currentPage * itemPage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(formAdapter);
        loadPage();

    }

    private void loadPage() {
        isLoading = true;
        Query query = fStore.collection("Form")
                .orderBy("Nama Lengkap")
                .limit(itemPage);

        if (currentPage > 0) {
            query = query.startAfter(lastDoc.get(currentPage - 1));
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (currentPage == 0) {
                        namaLengkapList.clear();
                        lastDoc.clear();
                    } else if (task.getResult().size() > 0) {
                        namaLengkapList.clear();
                    }

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String namaLengkap = documentSnapshot.getString("Nama Lengkap");
                        if (namaLengkap != null) {
                            namaLengkapList.add(namaLengkap);
                        }
                    }

                    if (task.getResult().size() > 0) {
                        lastDoc.add(task.getResult().getDocuments().get(task.getResult().size() - 1));
                    }
                    formAdapter.updateStartPosition(currentPage * itemPage);
                    formAdapter.notifyDataSetChanged();
                    pageNumber.setText(String.valueOf(currentPage + 1));
                } else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
                isLoading = false;
            }
        });
    }
}