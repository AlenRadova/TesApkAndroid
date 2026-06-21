package com.example.tesapkandroid;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private androidx.core.widget.NestedScrollView mainDashboardContent;
    private Spinner spinnerPilihanLab;

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

        // ==========================================
        // LOGIKA DROPDOWN SPINNER LAB
        // ==========================================
        spinnerPilihanLab = findViewById(R.id.spinnerPilihanLab);
        if (spinnerPilihanLab != null) {
            // Menyiapkan data pilihan Lab
            String[] daftarLab = {"Lab A", "Lab B", "Lab C"};

            // Menggunakan layout 'spinner_item' agar warna teks menjadi hitam kontras
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, R.layout.spinner_item, daftarLab
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPilihanLab.setAdapter(adapter);

            // Membuat dropdown berfungsi saat dipilih
            spinnerPilihanLab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String labTerpilih = daftarLab[position];

                    // Memunculkan Toast pemberitahuan aksi
                    Toast.makeText(DashboardActivity.this, "Menampilkan data " + labTerpilih, Toast.LENGTH_SHORT).show();

                    // Jalankan perintah pengkondisian di bawah ini jika kamu ingin merubah data statistik dari database nantinya
                    if (labTerpilih.equals("Lab A")) {
                        // Jalankan logika/panggil data Lab A
                    } else if (labTerpilih.equals("Lab B")) {
                        // Jalankan logika/panggil data Lab B
                    } else if (labTerpilih.equals("Lab C")) {
                        // Jalankan logika/panggil data Lab C
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Kosongkan jika tidak ada penanganan khusus
                }
            });
        }

        // ==========================================
        // 3. LOGIKA KLIK TOMBOL MENU NAVIGASI BAWAH
        // ==========================================
        if (bottomNavigation != null) {
            bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_dashboard) {
                        // KLIK HOME: Tampilkan beranda statis (statistik), sembunyikan fragment container
                        if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.VISIBLE);
                        if (findViewById(R.id.fragment_container) != null) {
                            findViewById(R.id.fragment_container).setVisibility(View.GONE);
                        }
                        return true;
                    }

                    else if (itemId == R.id.navigation_data_pc) {
                        // KLIK DATA PC (IKON MONITOR): Sembunyikan home, tampilkan List PC di fragment_container
                        if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.GONE);
                        if (findViewById(R.id.fragment_container) != null) {
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new DataPcFragment())
                                    .commit();
                        }
                        return true;
                    }

                    else if (itemId == R.id.nav_riwayat) {
                        // KLIK RIWAYAT: Sembunyikan home, jalankan RiwayatFragment di fragment_container
                        if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.GONE);
                        if (findViewById(R.id.fragment_container) != null) {
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

                            // MENAMPILKAN RIWAYAT FRAGMENT
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new RiwayatFragment())
                                    .commit();
                        }
                        return true;
                    }

                    else if (itemId == R.id.nav_profile) {
                        // KLIK PROFIL: Sembunyikan home, siapkan wadah fragment profile
                        if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.GONE);
                        if (findViewById(R.id.fragment_container) != null) {
                            findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                            // Jika nanti ada fragment profile, buka tanda komentar baris di bawah:
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