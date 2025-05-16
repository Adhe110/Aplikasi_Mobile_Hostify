package com.example.kostan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Menunggu selama 3 detik sebelum berpindah ke MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Arahkan ke MainActivity setelah SplashActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Menambahkan animasi transisi
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                finish();  // Selesaikan SplashActivity
            }
        }, 3000);  // Waktu tunggu 3 detik
    }
}
