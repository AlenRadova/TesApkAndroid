package com.example.tesapkandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class UserDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);

        // Inisialisasi CardView Menu Cepat
        CardView btnLihatDataPC = findViewById(R.id.btnLihatDataPC);
        CardView btnPinjamPC = findViewById(R.id.btnPinjamPC);

        btnLihatDataPC.setOnClickListener(v -> {
            // Arahkan ke activity daftar PC
            Toast.makeText(this, "Membuka Daftar PC...", Toast.LENGTH_SHORT).show();
        });

        btnPinjamPC.setOnClickListener(v -> {
            // Arahkan ke activity peminjaman
            Toast.makeText(this, "Membuka Menu Pinjam...", Toast.LENGTH_SHORT).show();
        });
    }
}