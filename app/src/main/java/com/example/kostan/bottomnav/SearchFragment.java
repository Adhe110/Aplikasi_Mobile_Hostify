package com.example.kostan.bottomnav;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kostan.R;
import com.example.kostan.KostAdapter;
import com.example.kostan.models.Kost;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private KostAdapter kostAdapter;
    private List<Kost> kostList;
    private DatabaseReference databaseReference;
    private MaterialButton filterButton;  // Tombol Filter

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Inisialisasi view
        recyclerView = view.findViewById(R.id.recyclerViewKost);
        searchEditText = view.findViewById(R.id.search_edit_text); // EditText pencarian
        filterButton = view.findViewById(R.id.filterButton); // Tombol filter

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        kostList = new ArrayList<>();
        kostAdapter = new KostAdapter(getContext(), kostList);
        recyclerView.setAdapter(kostAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("kosts");

        ambilDataDariFirebase();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                kostAdapter.filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Mengatur klik tombol filter untuk urutkan harga
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortKostByPrice(); // Panggil metode untuk mengurutkan kost berdasarkan harga
            }
        });

        return view;
    }

    private void ambilDataDariFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Kost> tempList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Kost kost = dataSnapshot.getValue(Kost.class);
                    if (kost != null) {
                        tempList.add(kost);
                    }
                }
                kostAdapter.setData(tempList); // Mengupdate data pada adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Gagal ambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Database error: " + error.getMessage());
            }
        });
    }

    // Metode untuk mengurutkan daftar kost berdasarkan harga
    private void sortKostByPrice() {
        Collections.sort(kostList, new Comparator<Kost>() {
            @Override
            public int compare(Kost kost1, Kost kost2) {
                // Periksa apakah harga tersedia dan urutkan berdasarkan harga termurah
                return Double.compare(kost1.getHarga(), kost2.getHarga());
            }
        });
        kostAdapter.notifyDataSetChanged(); // Memberitahu adapter bahwa data telah berubah
        Toast.makeText(getContext(), "Kost diurutkan berdasarkan harga terendah", Toast.LENGTH_SHORT).show();
    }
}
