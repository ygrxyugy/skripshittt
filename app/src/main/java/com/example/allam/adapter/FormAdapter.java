package com.example.allam.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allam.R;
import com.example.allam.data.lihat_data;

import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.ViewHolder> {
    private List<String> namaLengkapList;
    private int startPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView noTextView;
        public TextView usernameTextView;
        public Button lihatButton;


        public ViewHolder(View view) {
            super(view);
            noTextView = view.findViewById(R.id.noTextView);
            usernameTextView = view.findViewById(R.id.usernameTextView);
            lihatButton = view.findViewById(R.id.btn_lihat);
        }
    }

    public FormAdapter(List<String> namaLengkapList, int startPosition ) {
        this.namaLengkapList = namaLengkapList;
        this.startPosition = startPosition;
    }

    public void updateStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String namaLengkap = namaLengkapList.get(position);

        holder.noTextView.setText(String.valueOf( startPosition + position + 1));
        holder.usernameTextView.setText(namaLengkap);
        holder.lihatButton.setText("Lihat");
        holder.lihatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), lihat_data.class);
                intent.putExtra("namaLengkap", namaLengkap);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return namaLengkapList.size();
    }

}
