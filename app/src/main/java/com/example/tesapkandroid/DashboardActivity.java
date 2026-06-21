package com.example.tesapkandroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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
    private View mainDashboardContent;
    private Spinner spinnerPilihanLab;

    // Inisialisasi tombol Aksi Cepat
    private View btnPinjamPC;
    private View btnLihatDataPC;

    // Komponen Tambahan untuk Update Data Dinamis (Gabungan Opsi A & B)
    private DatabaseHelper dbHelper;
    private TextView tvPcDipinjamCount;
    private View cardStatusPeminjamanAdmin;
    private TextView tvNamaPcAdmin, tvStatusPcAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        dbHelper = new DatabaseHelper(this);

        // 1. Tangkap data "ROLE" yang dikirim dari MainActivity
        String role = getIntent().getStringExtra("ROLE");

        // 2. Tentukan layout berdasarkan Role & Inisialisasi sesuai ID XML asli
        if (role != null && role.equalsIgnoreCase("ADMIN")) {
            setContentView(R.layout.activity_dashboard_admin);
            Toast.makeText(this, "Selamat Datang, Admin!", Toast.LENGTH_SHORT).show();

            bottomNavigation = findViewById(R.id.bottomNavigationAdmin);

            // Inisialisasi Komponen Gabungan Opsi A & B khusus Admin
            tvPcDipinjamCount = findViewById(R.id.tvPcDipinjamCount);
            cardStatusPeminjamanAdmin = findViewById(R.id.cardStatusPeminjamanAdmin);
            tvNamaPcAdmin = findViewById(R.id.tvNamaPcAdmin);
            tvStatusPcAdmin = findViewById(R.id.tvStatusPcAdmin);

            // Perbarui visual data counter global dan kartu peminjaman personal
            refreshDashboardAdminData();

        } else {
            setContentView(R.layout.activity_dashboard_user);
            Toast.makeText(this, "Selamat Datang, Mahasiswa!", Toast.LENGTH_SHORT).show();

            bottomNavigation = findViewById(R.id.bottomNavigationUser);
        }

        mainDashboardContent = findViewById(R.id.mainDashboardContent);

        // ==========================================
        // LOGIKA TOMBOL AKSI CEPAT (PINJAM PC & LIST PC)
        // ==========================================
        btnPinjamPC = findViewById(R.id.btnPinjamPC);
        btnLihatDataPC = findViewById(R.id.btnLihatDataPC);

        if (btnPinjamPC != null) {
            btnPinjamPC.setOnClickListener(v -> {
                Intent intent = new Intent(DashboardActivity.this, PeminjamanActivity.class);
                intent.putExtra("ROLE", role);
                startActivity(intent);
            });
        }

        if (btnLihatDataPC != null) {
            btnLihatDataPC.setOnClickListener(v -> {
                if (bottomNavigation != null) {
                    bottomNavigation.setSelectedItemId(R.id.navigation_data_pc);
                }
            });
        }

        // ==========================================
        // LOGIKA DROPDOWN SPINNER LAB
        // ==========================================
        spinnerPilihanLab = findViewById(R.id.spinnerPilihanLab);
        if (spinnerPilihanLab != null) {
            String[] daftarLab = {"Lab A", "Lab B", "Lab C"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, R.layout.spinner_item, daftarLab
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPilihanLab.setAdapter(adapter);

            spinnerPilihanLab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String labTerpilih = daftarLab[position];
                    Toast.makeText(DashboardActivity.this, "Menampilkan data " + labTerpilih, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }

        // ==========================================
        // 3. LOGIKA KLIK TOMBOL MENU NAVIGASI BAWAH
        // ==========================================
        if (bottomNavigation != null) {
            bottomNavigation.setOnNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_dashboard) {
                    if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.VISIBLE);
                    if (findViewById(R.id.fragment_container) != null) {
                        findViewById(R.id.fragment_container).setVisibility(View.GONE);
                    }
                    // Refresh data statistik saat user kembali ke tab Home secara manual
                    if (role != null && role.equalsIgnoreCase("ADMIN")) {
                        refreshDashboardAdminData();
                    }
                    return true;
                }

                else if (itemId == R.id.navigation_data_pc) {
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
                    if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.GONE);
                    if (findViewById(R.id.fragment_container) != null) {
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new RiwayatFragment())
                                .commit();
                    }
                    return true;
                }

                else if (itemId == R.id.nav_profile) {
                    if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.GONE);
                    if (findViewById(R.id.fragment_container) != null) {
                        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ProfileFragment())
                                .commit();
                    }
                    return true;
                }

                return false;
            });
        }

        // ==========================================
        // PENANGANAN PENYEGARAN: KEMBALI DARI SUCCESS ACTIVITY
        // ==========================================
        if (getIntent().hasExtra("TARGET_FRAGMENT")) {
            String target = getIntent().getStringExtra("TARGET_FRAGMENT");
            if (target != null && target.equalsIgnoreCase("HOME")) {
                if (bottomNavigation != null) {
                    bottomNavigation.setSelectedItemId(R.id.nav_dashboard);
                }
                if (mainDashboardContent != null) mainDashboardContent.setVisibility(View.VISIBLE);
                if (findViewById(R.id.fragment_container) != null) {
                    findViewById(R.id.fragment_container).setVisibility(View.GONE);
                }
                // Refresh data statistik pasca peminjaman disubmit
                if (role != null && role.equalsIgnoreCase("ADMIN")) {
                    refreshDashboardAdminData();
                }
            }
        }

        // 4. Atur padding sistem agar tampilan full screen rapi
        int rootLayoutId = (role != null && role.equalsIgnoreCase("ADMIN")) ? R.id.layoutHeaderAdmin : R.id.layoutHeaderUser;
        if (findViewById(rootLayoutId) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(rootLayoutId), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
                return insets;
            });
        }
    }

    // =========================================================================
    // KODE YANG DIPERBAIKI: Mengambil data aktif terakhir secara global (Dinamis)
    // =========================================================================
    private void refreshDashboardAdminData() {
        // 1. Ambil data jumlah peminjaman aktif global untuk counter statistik card
        int jumlahDipinjam = dbHelper.getJumlahPcDipinjam();
        if (tvPcDipinjamCount != null) {
            tvPcDipinjamCount.setText(jumlahDipinjam + " Unit");
        }

        // 2. Ambil data transaksi peminjaman 'Aktif' paling terakhir dari database secara global
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_PEMINJAMAN +
                        " WHERE " + DatabaseHelper.COL_STATUS_PINJAMAN + " = 'Aktif'" +
                        " ORDER BY " + DatabaseHelper.COL_ID_PINJAM + " DESC LIMIT 1", null);

        if (cursor != null && cursor.moveToFirst()) {
            String kodePc = cursor.getString(cursor.getColumnIndexOrThrow("kode_pc"));

            if (cardStatusPeminjamanAdmin != null) {
                cardStatusPeminjamanAdmin.setVisibility(View.VISIBLE); // Munculkan Card
                tvNamaPcAdmin.setText(kodePc + " (Lab Utama)");
                tvStatusPcAdmin.setText("SEDANG DIGUNAKAN");
            }
        } else {
            // Sembunyikan card status jika tidak ada satu pun PC yang sedang aktif dipinjam
            if (cardStatusPeminjamanAdmin != null) {
                cardStatusPeminjamanAdmin.setVisibility(View.GONE);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}