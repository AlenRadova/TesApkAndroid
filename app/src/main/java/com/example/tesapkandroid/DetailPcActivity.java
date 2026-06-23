package com.example.tesapkandroid;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DetailPcActivity extends AppCompatActivity {

    private TextView tvDetailKodePc, tvDetailLokasiPc, tvDetailToken, tvDetailNama, tvDetailNim, tvDetailKeperluan, tvDetailStatusBadge, tvDetailSisaMenit, tvActionPowerTitle, tvActionPowerSubtitle;
    private ImageView imgPowerIcon;
    private ProgressBar progressDetailWaktu;
    private CardView btnDetailPeringatan, btnDetailPesanCustom, btnActionPowerPc;
    private LinearLayout layoutActionAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pc);

        // Inisialisasi View Komponen
        ImageView btnBackDetail = findViewById(R.id.btnBackDetail);
        tvDetailKodePc = findViewById(R.id.tvDetailKodePc);
        tvDetailLokasiPc = findViewById(R.id.tvDetailLokasiPc);
        tvDetailToken = findViewById(R.id.tvDetailToken);
        tvDetailNama = findViewById(R.id.tvDetailNama);
        tvDetailNim = findViewById(R.id.tvDetailNim);
        tvDetailKeperluan = findViewById(R.id.tvDetailKeperluan);
        tvDetailStatusBadge = findViewById(R.id.tvDetailStatusBadge);
        progressDetailWaktu = findViewById(R.id.progressDetailWaktu);
        tvDetailSisaMenit = findViewById(R.id.tvDetailSisaMenit);

        btnDetailPeringatan = findViewById(R.id.btnDetailPeringatan);
        btnDetailPesanCustom = findViewById(R.id.btnDetailPesanCustom);
        btnActionPowerPc = findViewById(R.id.btnActionPowerPc);
        imgPowerIcon = findViewById(R.id.imgPowerIcon);
        tvActionPowerTitle = findViewById(R.id.tvActionPowerTitle);
        tvActionPowerSubtitle = findViewById(R.id.tvActionPowerSubtitle);

        // Inisialisasi layout tombol admin
        layoutActionAdmin = findViewById(R.id.layoutActionAdmin);

        // --- LOGIKA ROLE (ADMIN VS USER) HANYA MEMBACA ---
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("role", "USER"); // Default USER

        if (userRole.equals("USER")) {
            layoutActionAdmin.setVisibility(View.GONE); // Sembunyikan tombol peringatan & pesan untuk Mahasiswa
        } else {
            layoutActionAdmin.setVisibility(View.VISIBLE); // Tampilkan untuk Admin
        }

        // Ambil Data lemparan Intent dari RecyclerView Adapter
        String kodePc = getIntent().getStringExtra("KODE_PC");
        String lokasiPc = getIntent().getStringExtra("LOKASI_PC");
        String statusPc = getIntent().getStringExtra("STATUS_PC");

        // Terapkan data dasar ke View
        tvDetailKodePc.setText(kodePc != null ? kodePc : "PC-000");
        tvDetailLokasiPc.setText(lokasiPc != null ? lokasiPc : "Unknown Lab");
        tvDetailStatusBadge.setText(statusPc != null ? statusPc : "Tersedia");

        // LOGIKA PENYESUAIAN ANTARMUKA DINAMIS BERDASARKAN STATUS PC
        if (statusPc != null && statusPc.equalsIgnoreCase("Tersedia")) {
            tvDetailStatusBadge.getBackground().setTint(Color.parseColor("#4CAF50"));
            progressDetailWaktu.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            tvDetailToken.setText("C C C A");
            tvDetailNama.setText("-");
            tvDetailNim.setText("-");
            tvDetailKeperluan.setText("-");
            tvDetailSisaMenit.setText("PC Siap digunakan");
            progressDetailWaktu.setProgress(0);

            imgPowerIcon.setImageTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#1A73E8")));
            tvActionPowerTitle.setText("Nonaktifkan PC");
            tvActionPowerTitle.setTextColor(Color.parseColor("#1A73E8"));
            tvActionPowerSubtitle.setText("Hentikan sesi, PC tidak bisa dipakai");

        } else if (statusPc != null && statusPc.equalsIgnoreCase("Maintenance")) {
            tvDetailStatusBadge.getBackground().setTint(Color.parseColor("#FF9800"));
            progressDetailWaktu.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800")));
            tvDetailToken.setText("C C C C");
            tvDetailNama.setText("-");
            tvDetailNim.setText("-");
            tvDetailKeperluan.setText("-");
            tvDetailSisaMenit.setText("Dalam Perbaikan perangkat");
            progressDetailWaktu.setProgress(100);

            imgPowerIcon.setImageTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800")));
            tvActionPowerTitle.setText("Aktifkan Kembali PC");
            tvActionPowerTitle.setTextColor(Color.parseColor("#FF9800"));
            tvActionPowerSubtitle.setText("Selesaikan pemeliharaan & buka unit");
        }

        // Click Listeners Aksi Tombol
        btnBackDetail.setOnClickListener(v -> finish());
        btnDetailPeringatan.setOnClickListener(v -> Toast.makeText(this, "Peringatan terkirim!", Toast.LENGTH_SHORT).show());
        btnDetailPesanCustom.setOnClickListener(v -> Toast.makeText(this, "Membuka dialog pesan", Toast.LENGTH_SHORT).show());
        btnActionPowerPc.setOnClickListener(v -> Toast.makeText(this, "Aksi berhasil!", Toast.LENGTH_SHORT).show());
    }
}