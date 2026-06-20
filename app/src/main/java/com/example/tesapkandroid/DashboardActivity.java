package com.example.tesapkandroid;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    // DEKLARASI DI SINI (Memperbaiki error merah mainDashboardContent)
    private androidx.core.widget.NestedScrollView mainDashboardContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 1. Tangkap data "ROLE" yang dikirim dari MainActivity
        String role = getIntent().getStringExtra("ROLE");

        // 2. Tentukan layout berdasarkan Role & Inisialisasi sesuai ID XML asli
        if (role != null && role.equalsIgnoreCase("ADMIN")) {
            setContentView(R.layout.activity_dashboard_admin);
            Toast.makeText(this, "Selamat Datang, Admin!", Toast.LENGTH_SHORT).show();

            bottomNavigation = findViewById(R.id.bottomNavigationAdmin);
        } else {
            setContentView(R.layout.activity_dashboard_user);
            Toast.makeText(this, "Selamat Datang, Mahasiswa!", Toast.LENGTH_SHORT).show();

            bottomNavigation = findViewById(R.id.bottomNavigationUser);
        }

        // Inisialisasi kontainer konten beranda statis
        mainDashboardContent = findViewById(R.id.mainDashboardContent);

        // 3. Logika klik tombol menu navigasi bawah sesuai dengan bottom_nav_menu.xml kamu
        if (bottomNavigation != null) {
            bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_dashboard) {
                        // KLIk BERANDA: Tampilkan statistik utama, sembunyikan wadah fragment
                        if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.VISIBLE);
                        if (findViewById(R.id.fragment_container) != null) {
                            findViewById(R.id.fragment_container).setVisibility(View.GONE);
                        }
                        return true;
                    }

                    else if (itemId == R.id.nav_riwayat) {
                        // KLIK RIWAYAT: Sembunyikan statistik utama, tampilkan list PC (DataPcFragment)
                        if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.GONE);
                        if (findViewById(R.id.fragment_container) != null) {
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new DataPcFragment())
                                    .commit();
                        }
                        return true;
                    }

                    else if (itemId == R.id.nav_profile) {
                        // KLIK PROFIL: Sembunyikan statistik utama, tampilkan fragment profile jika ada
                        if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.GONE);
                        if (findViewById(R.id.fragment_container) != null) {
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                        }
                        return true;
                    }

                    return false;
                }
            });
        }

        // 4. Atur padding sistem agar tampilan full screen rapi bawaan Android Studio
        int rootLayoutId = (role != null && role.equalsIgnoreCase("ADMIN")) ? R.id.layoutHeaderAdmin : R.id.layoutHeaderUser;
        if (findViewById(rootLayoutId) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(rootLayoutId), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }
    }
}