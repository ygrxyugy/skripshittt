package com.example.allam.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allam.R;
import com.example.allam.lihat_data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VerifAdapter extends RecyclerView.Adapter<VerifAdapter.ViewHolder> {
    private List<Map<String, Object>> userList;
    private Context context;
    private int startPosition;

    public VerifAdapter(List<Map<String, Object>> userList, Context context, int startPosition) {
        this.userList = userList;
        this.context = context;
        this.startPosition = startPosition;

    }

    public void updateStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verifikasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> user = userList.get(position);
        holder.vNo.setText(String.valueOf(startPosition + position + 1));
        holder.vNama.setText((String) user.get("Nama Lengkap"));

        String status = (String) user.get("Status");
        if (status == null) {
            status = "Pending";
        }

        holder.vStatus.setText(status);

        switch (status) {
            case "Lolos":
                holder.vStatus.setBackgroundResource(R.color.warna2);
                break;
            case "Tidak Lolos":
                holder.vStatus.setBackgroundResource(R.color.merah);
                break;
            default:
                holder.vStatus.setBackgroundResource(R.color.abu);
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, lihat_data.class);
            intent.putExtra("namaLengkap", (String) user.get("Nama Lengkap"));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vNo, vNama, vStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            vNo = itemView.findViewById(R.id.vNo);
            vNama = itemView.findViewById(R.id.vNama);
            vStatus = itemView.findViewById(R.id.vStatus);
        }
    }
}
