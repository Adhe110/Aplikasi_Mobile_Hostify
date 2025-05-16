package com.example.kostan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kostan.models.Kost;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.KostViewHolder> {

    private Context context;
    private List<Kost> kostList;
    private List<Kost> fullList; // Untuk pencarian

    public HomeAdapter(Context context, List<Kost> kostList) {
        this.context = context;
        this.kostList = kostList;
        this.fullList = new ArrayList<>(kostList); // Salin untuk backup pencarian
    }

    @NonNull
    @Override
    public KostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        // Menggunakan layout item_home.xml untuk setiap item di RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new KostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KostViewHolder holder, int position) {
        // Ambil data kost berdasarkan posisi dan tampilkan ke dalam item
        Kost kost = kostList.get(position);

        holder.nama.setText(kost.getNama());

        // Format harga menjadi format Rupiah
        if (kost.getHarga() > 0) { // Cek jika harga lebih dari 0
            double harga = kost.getHarga(); // Asumsikan harga bertipe long
            String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(harga);
            holder.harga.setText(formattedPrice);
        } else {
            holder.harga.setText("Harga Tidak Tersedia"); // Jika harga tidak valid
        }

        // Set gambar jika ada, jika tidak, tampilkan gambar placeholder
        if (kost.getGambar() != null && !kost.getGambar().isEmpty()) {
            Picasso.get().load(kost.getGambar().get(0)).into(holder.gambar);
        } else {
            holder.gambar.setImageResource(R.drawable.placeholder);
        }

        // Set OnClickListener untuk item
        holder.itemView.setOnClickListener(v -> {
            // Gunakan context untuk membuat Intent
            Intent intent = new Intent(context, DetailKostActivity.class);
            // Kirim data kost ke DetailKostActivity menggunakan Intent
            intent.putExtra("kostDetail", kost);
            context.startActivity(intent);  // Mulai activity dengan context
        });
    }

    @Override
    public int getItemCount() {
        return kostList.size();
    }

    // ViewHolder untuk item kost
    public static class KostViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView nama, harga;

        public KostViewHolder(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambarKost);  // Gambar kost
            nama = itemView.findViewById(R.id.namaKost);      // Nama kost
            harga = itemView.findViewById(R.id.hargaKost);    // Harga kost
        }
    }

    // Untuk fitur pencarian, filter berdasarkan nama kost
    public void filterList(String query) {
        List<Kost> filtered = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            filtered.addAll(fullList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Kost kost : fullList) {
                if (kost.getNama() != null && kost.getNama().toLowerCase().contains(lowerQuery)) {
                    filtered.add(kost);
                }
            }
        }

        kostList.clear();
        kostList.addAll(filtered);
        notifyDataSetChanged();
    }

    // Untuk update data dari Firebase atau sumber lain
    public void setData(List<Kost> newList) {
        kostList.clear();               // Bersihkan list yang ada
        kostList.addAll(newList);       // Tambahkan data baru
        fullList.clear();               // Bersihkan list cadangan
        fullList.addAll(newList);       // Tambahkan data baru ke fullList
        notifyDataSetChanged();        // Update adapter

        // Jika kamu ingin acak data untuk adapter left, lakukan di tempat lain, misalnya saat set data ke homeAdapterLeft
    }

}
