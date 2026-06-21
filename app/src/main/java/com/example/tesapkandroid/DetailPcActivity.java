package com.example.tesapkandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
            // Sesuai mockup Tersedia (Warna Hijau Solid)
            tvDetailStatusBadge.getBackground().setTint(Color.parseColor("#4CAF50"));
            progressDetailWaktu.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));

            // Set dummy data kosongan karena belum ada peminjaman aktif
            tvDetailToken.setText("C C C A");
            tvDetailNama.setText("-");
            tvDetailNim.setText("-");
            tvDetailKeperluan.setText("-");
            tvDetailSisaMenit.setText("PC Siap digunakan");
            progressDetailWaktu.setProgress(0);

            // Pengaturan Tombol Bawah untuk Power On/Off
            imgPowerIcon.setImageTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#1A73E8")));
            tvActionPowerTitle.setText("Nonaktifkan PC");
            tvActionPowerTitle.setTextColor(Color.parseColor("#1A73E8"));
            tvActionPowerSubtitle.setText("Hentikan sesi, PC tidak bisa dipakai");

        } else if (statusPc != null && statusPc.equalsIgnoreCase("Maintenance")) {
            // Sesuai mockup Maintenance (Warna Oranye)
            tvDetailStatusBadge.getBackground().setTint(Color.parseColor("#FF9800"));
            progressDetailWaktu.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800")));

            tvDetailToken.setText("C C C C");
            tvDetailNama.setText("-");
            tvDetailNim.setText("-");
            tvDetailKeperluan.setText("-");
            tvDetailSisaMenit.setText("Dalam Perbaikan perangkat");
            progressDetailWaktu.setProgress(100);

            // Tombol bawah berubah jadi opsi nyalakan ulang sistem
            imgPowerIcon.setImageTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800")));
            tvActionPowerTitle.setText("Aktifkan Kembali PC");
            tvActionPowerTitle.setTextColor(Color.parseColor("#FF9800"));
            tvActionPowerSubtitle.setText("Selesaikan pemeliharaan & buka unit");
        }

        // Click Listeners Aksi Tombol
        btnBackDetail.setOnClickListener(v -> finish());

        btnDetailPeringatan.setOnClickListener(v -> Toast.makeText(this, "Peringatan terkirim ke klien!", Toast.LENGTH_SHORT).show());
        btnDetailPesanCustom.setOnClickListener(v -> Toast.makeText(this, "Membuka dialog pesan custom", Toast.LENGTH_SHORT).show());

        btnActionPowerPc.setOnClickListener(v -> {
            Toast.makeText(this, "Aksi Kelola Daya PC Berhasil dijalankan", Toast.LENGTH_SHORT).show();
        });
    }
}