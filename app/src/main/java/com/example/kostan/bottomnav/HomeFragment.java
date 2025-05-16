package com.example.kostan.bottomnav;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kostan.R;
import com.example.kostan.HomeAdapter;
import com.example.kostan.models.Kost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewLeft, recyclerViewRight;
    private HomeAdapter homeAdapterLeft, homeAdapterRight;
    private DatabaseReference databaseReference;

    private ViewFlipper viewFlipper;
    private ImageView dot1, dot2, dot3;
    private int currentPage = 0;
    private final Handler handler = new Handler();
    private final int interval = 3000;

    private final Runnable flipRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewFlipper != null) {
                viewFlipper.showNext();
                currentPage = (currentPage + 1) % 3;
                updateDots(currentPage);
                handler.postDelayed(this, interval);
            }
        }
    };

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inisialisasi ViewFlipper dan Dots
        viewFlipper = view.findViewById(R.id.viewFlipper);
        dot1 = view.findViewById(R.id.dot1);
        dot2 = view.findViewById(R.id.dot2);
        dot3 = view.findViewById(R.id.dot3);

        updateDots(0);
        handler.postDelayed(flipRunnable, interval);

        // Inisialisasi RecyclerView
        recyclerViewLeft = view.findViewById(R.id.recyclerViewKostLeft);
        recyclerViewRight = view.findViewById(R.id.recyclerViewKostRight);

        recyclerViewLeft.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRight.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerViewLeft.setHasFixedSize(true);
        recyclerViewRight.setHasFixedSize(true);

        // Inisialisasi adapter dengan list kosong
        homeAdapterLeft = new HomeAdapter(getContext(), new ArrayList<>());
        homeAdapterRight = new HomeAdapter(getContext(), new ArrayList<>());

        recyclerViewLeft.setAdapter(homeAdapterLeft);
        recyclerViewRight.setAdapter(homeAdapterRight);

        // Ambil data dari Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("kosts");
        ambilDataDariFirebase();

        // Inisialisasi dan atur TextView untuk quote
        TextView tvQuote = view.findViewById(R.id.tvQuote);
        String[] quotes = {
                "Temukan tempat tinggal nyaman, mulai dari sini.",
                "Nyaman, terjangkau, dan sesuai kebutuhan — semua ada di sini.",
                "Kost impianmu hanya sejauh sentuhan jari.",
                "Mulai harimu dari tempat yang membuatmu betah.",
                "Rasa nyaman dimulai dari tempat yang kamu pilih."
        };

        Handler quoteHandler = new Handler();
        Runnable quoteRunnable = new Runnable() {
            int index = 0;

            @Override
            public void run() {
                tvQuote.setText(quotes[index]);
                index = (index + 1) % quotes.length;
                quoteHandler.postDelayed(this, 4000);
            }
        };

        quoteHandler.post(quoteRunnable);


        return view;
    }

    private void updateDots(int position) {
        dot1.setBackgroundResource(position == 0 ? R.drawable.dot_active : R.drawable.dot_inactive);
        dot2.setBackgroundResource(position == 1 ? R.drawable.dot_active : R.drawable.dot_inactive);
        dot3.setBackgroundResource(position == 2 ? R.drawable.dot_active : R.drawable.dot_inactive);
    }

    private void ambilDataDariFirebase() {
        databaseReference.orderByChild("createdAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Kost> allKost = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Kost kost = dataSnapshot.getValue(Kost.class);
                    if (kost != null) {
                        allKost.add(kost);
                    }
                }

                // Urutkan dari terbaru ke lama
                Collections.reverse(allKost);

// Set untuk adapter kanan (terbaru, urutan asli)
                homeAdapterRight.setData(new ArrayList<>(allKost)); // Salin data tanpa acak

// Set untuk adapter kiri (acak)
                List<Kost> shuffledList = new ArrayList<>(allKost);  // Salin data
                Collections.shuffle(shuffledList);  // Acak list
                homeAdapterLeft.setData(shuffledList);  // Kirim data acak

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Gagal ambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(flipRunnable);
    }


}
