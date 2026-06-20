package com.example.tesapkandroid;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 1. Tangkap data "ROLE" yang dikirim dari MainActivity
        String role = getIntent().getStringExtra("ROLE");

        // 2. Tentukan layout berdasarkan Role
        if (role != null && role.equals("ADMIN")) {
            // Jika Admin, tempelkan layout activity_dashboard_admin.xml
            setContentView(R.layout.activity_dashboard_admin);
            Toast.makeText(this, "Selamat Datang, Admin!", Toast.LENGTH_SHORT).show();
        } else {
            // Jika User/Mahasiswa, tempelkan layout activity_dashboard_user.xml
            setContentView(R.layout.activity_dashboard_user);
            Toast.makeText(this, "Selamat Datang, Mahasiswa!", Toast.LENGTH_SHORT).show();
        }

        // 3. Atur padding sistem agar tampilan full screen rapi (Bawaan Android Studio)
        // Pastikan ID 'main' di bawah ini ada di root layout xml milikmu (baik admin maupun user)
        // Jika memicu error merah, baris ViewCompat ini boleh kamu hapus/komentari dulu sementara.
        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }
}