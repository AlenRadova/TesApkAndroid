package com.example.tesapkandroid;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class PeminjamanActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Spinner spinnerPC;
    private LinearLayout layoutWaktuMulai;
    private TextView tvWaktuMulai, tvEstimasiSelesai;
    private TextInputEditText etNim, etNama, etDurasiJam, etKeperluan;
    private Button btnSubmit;

    private int selectedHour = -1;
    private int selectedMinute = -1;

    // Menyimpan data role aktif agar tidak hilang saat kembali
    private String currentRole = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);

        // Tangkap data ROLE dari DashboardActivity
        if (getIntent().hasExtra("ROLE")) {
            currentRole = getIntent().getStringExtra("ROLE");
        }

        // Inisialisasi Komponen XML
        btnBack = findViewById(R.id.btnBackPeminjaman);
        spinnerPC = findViewById(R.id.spinnerPC);
        layoutWaktuMulai = findViewById(R.id.layoutWaktuMulai);
        tvWaktuMulai = findViewById(R.id.tvWaktuMulai);
        tvEstimasiSelesai = findViewById(R.id.tvEstimasiSelesai);
        etNim = findViewById(R.id.etNim);
        etNama = findViewById(R.id.etNama);
        etDurasiJam = findViewById(R.id.etDurasiJam);
        etKeperluan = findViewById(R.id.etKeperluan);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Tombol Back
        btnBack.setOnClickListener(v -> finish());

        // Mengisi Data Spinner PC
        String[] listPC = {"PC-001", "PC-002", "PC-003", "PC-004", "PC-005"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listPC);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPC.setAdapter(adapter);

        // Klik Pilih Waktu Mulai
        layoutWaktuMulai.setOnClickListener(v -> BukaTimePicker());

        // Hitung Estimasi otomatis
        etDurasiJam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HitungEstimasiSelesai();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Validasi & Submit Data Form
        btnSubmit.setOnClickListener(v -> ProsesSimpanPeminjaman());
    }

    private void BukaTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = (selectedHour == -1) ? c.get(Calendar.HOUR_OF_DAY) : selectedHour;
        int minute = (selectedMinute == -1) ? c.get(Calendar.MINUTE) : selectedMinute;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minuteOfHour;
                    String waktuFormatted = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    tvWaktuMulai.setText(waktuFormatted);
                    tvWaktuMulai.setTextColor(getResources().getColor(android.R.color.black));
                    HitungEstimasiSelesai();
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void HitungEstimasiSelesai() {
        String durasiStr = etDurasiJam.getText().toString().trim();

        if (selectedHour == -1 || durasiStr.isEmpty()) {
            tvEstimasiSelesai.setText("-");
            return;
        }

        try {
            int durasi = Integer.parseInt(durasiStr);
            int totalJamSelesai = selectedHour + durasi;

            if (totalJamSelesai >= 24) {
                totalJamSelesai = totalJamSelesai % 24;
            }

            String estimasiFormatted = String.format("%02d:%02d", totalJamSelesai, selectedMinute);
            tvEstimasiSelesai.setText(estimasiFormatted);
        } catch (NumberFormatException e) {
            tvEstimasiSelesai.setText("-");
        }
    }

    private void ProsesSimpanPeminjaman() {
        String nim = etNim.getText().toString().trim();
        String nama = etNama.getText().toString().trim();
        String durasi = etDurasiJam.getText().toString().trim();
        String keperluan = etKeperluan.getText().toString().trim();
        String pcTerpilih = spinnerPC.getSelectedItem().toString();

        if (nim.isEmpty() || nama.isEmpty() || durasi.isEmpty() || keperluan.isEmpty() || selectedHour == -1) {
            Toast.makeText(this, "Semua kolom form wajib diisi lengkap!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ==========================================
        // KODE BARU: PROSES SIMPAN DATA KE SQLITE
        // ==========================================
        DatabaseHelper db = new DatabaseHelper(this);
        boolean isBerhasil = db.insertPeminjaman(nim, nama, pcTerpilih);

        if (isBerhasil) {
            // Membuka Halaman SuccessActivity setelah data berhasil disimpan
            Intent intent = new Intent(PeminjamanActivity.this, SuccessActivity.class);
            intent.putExtra("ROLE", currentRole); // Oper data role kembali
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Gagal menyimpan data ke database!", Toast.LENGTH_SHORT).show();
        }
    }
}