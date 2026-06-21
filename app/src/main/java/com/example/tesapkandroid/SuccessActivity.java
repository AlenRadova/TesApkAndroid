package com.example.tesapkandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Tambahkan import Log
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

public class SuccessActivity extends AppCompatActivity {

    private String currentRole = "USER"; // Nilai default aman

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        // Ambil data ROLE dari activity sebelumnya
        if (getIntent().hasExtra("ROLE")) {
            currentRole = getIntent().getStringExtra("ROLE");
            Log.d("SUCCESS_ACTIVITY", "Role diterima: " + currentRole);
        } else {
            Log.e("SUCCESS_ACTIVITY", "Waduh! Extra 'ROLE' tidak ditemukan dari activity sebelumnya!");
        }

        Button btnHome = findViewById(R.id.btnHome);

        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                Intent intent = new Intent(SuccessActivity.this, DashboardActivity.class);

                // Menggunakan flag agar dashboard tidak menumpuk dan segar kembali
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                // Kirim balik data role dan target fragment
                intent.putExtra("ROLE", currentRole);
                intent.putExtra("TARGET_FRAGMENT", "HOME");

                Log.d("SUCCESS_ACTIVITY", "Mengarah ke Dashboard dengan Role: " + currentRole);
                startActivity(intent);
                finish();
            });
        }
    }
}