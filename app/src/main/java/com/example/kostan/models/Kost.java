package com.example.kostan.models;

import java.io.Serializable;
import java.util.List;

public class Kost implements Serializable {
    private String nama;
    private long harga; // <- UBAH DI SINI
    private String deskripsi;
    private String no_wa;
    private List<String> gambar;

    public Kost() {} // Diperlukan untuk Firebase

    public Kost(String nama, long harga, String deskripsi, String no_wa, List<String> gambar) {
        this.nama = nama;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.no_wa = no_wa;
        this.gambar = gambar;
    }

    // Getter dan Setter
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public long getHarga() { return harga; } // <- long
    public void setHarga(long harga) { this.harga = harga; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getNo_wa() { return no_wa; }
    public void setNo_wa(String no_wa) { this.no_wa = no_wa; }

    public List<String> getGambar() { return gambar; }
    public void setGambar(List<String> gambar) { this.gambar = gambar; }
}
