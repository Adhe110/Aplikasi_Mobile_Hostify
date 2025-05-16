package com.example.kostan;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate; // Menambahkan import ini
import androidx.viewpager2.widget.ViewPager2;

import com.example.kostan.models.Kost;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailKostActivity extends AppCompatActivity {

    private TextView namaDetail, hargaDetail, deskripsiDetail, noWaDetail;
    private Button buttonWhatsapp;
    private ViewPager2 viewPager;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kost);



        // Hubungkan komponen UI
        namaDetail = findViewById(R.id.namaDetail);
        hargaDetail = findViewById(R.id.hargaDetail);
        deskripsiDetail = findViewById(R.id.deskripsiDetail);
        noWaDetail = findViewById(R.id.noWaDetail);
        buttonWhatsapp = findViewById(R.id.buttonWhatsapp);
        viewPager = findViewById(R.id.viewPager);
        buttonBack = findViewById(R.id.buttonBack);

        // Fungsi tombol kembali
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // atau onBackPressedDispatcher.onBackPressed();
            }
        });

        // Ambil data dari intent
        Intent intent = getIntent();
        Kost kost = (Kost) intent.getSerializableExtra("kostDetail");

        if (kost != null) {
            // Tampilkan nama
            namaDetail.setText(kost.getNama());

            // Format harga
            if (kost.getHarga() > 0) {
                String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(kost.getHarga());
                hargaDetail.setText(formattedPrice);
            } else {
                hargaDetail.setText("Harga Tidak Tersedia");
            }

            // Deskripsi
            deskripsiDetail.setText(kost.getDeskripsi());

            // Nomor WA
            noWaDetail.setText(kost.getNo_wa());
            if (kost.getNo_wa() == null || kost.getNo_wa().isEmpty()) {
                noWaDetail.setVisibility(View.GONE);
            }

            // Tampilkan gambar jika tersedia
            if (kost.getGambar() != null && !kost.getGambar().isEmpty()) {
                ImageAdapter imageAdapter = new ImageAdapter(kost.getGambar());
                viewPager.setAdapter(imageAdapter);
            }

            // Tombol WhatsApp
            buttonWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nomor = kost.getNo_wa();
                    String pesan = "Halo kakz, saya tertarik dengan " + kost.getNama() + ", boleh minta info lengkapnya?";
                    if (nomor != null && !nomor.isEmpty()) {
                        String url = "https://wa.me/" + nomor + "?text=" + Uri.encode(pesan);
                        Intent intentWa = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intentWa);
                    }
                }
            });
        }
    }
}
