package com.example.kostan;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kostan.models.Kost;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KostAdapter extends RecyclerView.Adapter<KostAdapter.KostViewHolder> {

    private Context context;
    private List<Kost> kostList;
    private List<Kost> fullList; // Untuk pencarian

    public KostAdapter(Context context, List<Kost> kostList) {
        this.context = context;
        this.kostList = kostList;
        this.fullList = new ArrayList<>(kostList); // Salin untuk backup pencarian
    }

    @NonNull
    @Override
    public KostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(context).inflate(R.layout.item_kost, parent, false);
        return new KostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KostViewHolder holder, int position) {
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

        if (kost.getGambar() != null && !kost.getGambar().isEmpty()) {
            Picasso.get().load(kost.getGambar().get(0)).into(holder.gambar);
        } else {
            holder.gambar.setImageResource(R.drawable.placeholder);
        }

        // Klik item ke Detail
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

    public static class KostViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView nama, harga, deskripsi, noWa;

        public KostViewHolder(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambarKost);
            nama = itemView.findViewById(R.id.namaKost);
            harga = itemView.findViewById(R.id.hargaKost);
        }
    }

    // Untuk fitur pencarian
    public void filterList(String query) {
        List<Kost> filtered = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            filtered.addAll(fullList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Kost kost : fullList) {
                // Cek nama dan deskripsi
                if ((kost.getNama() != null && kost.getNama().toLowerCase().contains(lowerQuery)) ||
                        (kost.getDeskripsi() != null && kost.getDeskripsi().toLowerCase().contains(lowerQuery))) {
                    filtered.add(kost);
                }
            }
        }

        kostList.clear();
        kostList.addAll(filtered);
        notifyDataSetChanged();
    }

    // Untuk update data dari Firebase
    public void setData(List<Kost> newList) {
        kostList.clear();
        kostList.addAll(newList);
        fullList.clear();
        fullList.addAll(newList);
        notifyDataSetChanged();
    }

    // Fungsi untuk mengurutkan daftar kost berdasarkan harga (termurah ke mahal)
    public void sortByHarga() {
        List<Kost> sortedList = new ArrayList<>(kostList);

        // Mengurutkan berdasarkan harga secara ascending (dari termurah ke mahal)
        sortedList.sort((kost1, kost2) -> {
            if (kost1.getHarga() < kost2.getHarga()) {
                return -1; // kost1 lebih murah
            } else if (kost1.getHarga() > kost2.getHarga()) {
                return 1;  // kost2 lebih mahal
            } else {
                return 0;  // harga sama
            }
        });

        kostList.clear();
        kostList.addAll(sortedList);
        notifyDataSetChanged();
    }

}
