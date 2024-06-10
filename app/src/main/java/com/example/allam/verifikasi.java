package com.example.allam;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allam.adapter.VerifAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class verifikasi extends AppCompatActivity {

    RecyclerView recyclerVerif;
    VerifAdapter adapter;
    List<Map<String, Object>> userList;
    List<Map<String, Object>> currentUserList;
    ImageView vNext, vPrev;
    TextView vPageNumber;
    private int vCurrentPage = 0;
    private final int ITEMS_PER_PAGE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);

        vNext = findViewById(R.id.vNext);
        vPageNumber = findViewById(R.id.vPageNumber);
        vPrev = findViewById(R.id.vPrev);

        recyclerVerif = findViewById(R.id.recyclerVerif);
        userList = new ArrayList<>();
        currentUserList = new ArrayList<>();
        adapter = new VerifAdapter(currentUserList, this);

        recyclerVerif.setHasFixedSize(true);
        recyclerVerif.setLayoutManager(new LinearLayoutManager(this));
        recyclerVerif.setAdapter(adapter);

        vNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNext();
            }
        });

        vPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPrev();
            }
        });
        
        loadData();
    }

    private void loadPrev() {
        if (vCurrentPage > 0) {
            vCurrentPage--;
            updateRecyclerView();
        }
    }

    private void loadNext() {
        if ((vCurrentPage + 1) * ITEMS_PER_PAGE < userList.size()) {
            vCurrentPage++;
            updateRecyclerView();
        }
    }

    private void updateRecyclerView() {
        int start = vCurrentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, userList.size());
        currentUserList.clear();
        currentUserList.addAll(userList.subList(start, end));
        adapter.notifyDataSetChanged();
        updateNavigation();
    }

    private void updateNavigation() {
        vPrev.setEnabled(vCurrentPage > 0);
        vNext.setEnabled((vCurrentPage + 1) * ITEMS_PER_PAGE < userList.size());
        vPageNumber.setText(String.valueOf(vCurrentPage + 1));
    }

    private void loadData() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fStore.collection("Form")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            userList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> user = document.getData();
                                user.put("id", document.getId());
                                if (!user.containsKey("Status")) {
                                    user.put("Status", "Pending");
                                }
                                userList.add(user);
                            }
                            vCurrentPage = 0;
                            updateRecyclerView();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}