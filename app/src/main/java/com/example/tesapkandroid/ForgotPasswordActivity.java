package com.example.tesapkandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton; // Diubah ke ImageButton sesuai XML aslimu
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    // Komponen Langkah Kontainer
    private LinearLayout layoutStep1, layoutStep2, layoutStep3, layoutStep4;

    // Komponen Tombol & Input
    private ImageButton btnBack; // Menggunakan ImageButton sesuai XML
    private EditText etEmail, etNewPassword, etConfirmNewPassword;
    private EditText etOtp1, etOtp2, etOtp3, etOtp4;
    private Button btnResetPassword, btnVerifyOtp, btnResetPasswordFinal, btnBackToLogin;
    private TextView tvBackToLogin, tvResendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // 1. Inisialisasi Kontainer Step
        layoutStep1 = findViewById(R.id.layoutStep1Email);
        layoutStep2 = findViewById(R.id.layoutStep2Otp);
        layoutStep3 = findViewById(R.id.layoutStep3NewPassword);
        layoutStep4 = findViewById(R.id.layoutStep4Success);

        // 2. Inisialisasi Elemen Input & Button dari XML yang baru
        btnBack = findViewById(R.id.btnBack);
        etEmail = findViewById(R.id.etEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);

        // Kotak OTP
        etOtp1 = findViewById(R.id.etOtp1);
        etOtp2 = findViewById(R.id.etOtp2);
        etOtp3 = findViewById(R.id.etOtp3);
        etOtp4 = findViewById(R.id.etOtp4);

        // Tombol-tombol Aksi
        btnResetPassword = findViewById(R.id.btnResetPassword); // Ini tombol kirim email di Step 1
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);         // Ini tombol verifikasi di Step 2
        btnResetPasswordFinal = findViewById(R.id.btnResetPasswordFinal); // SUDAH DIPERBAIKI: Tombol atur ulang di Step 3
        btnBackToLogin = findViewById(R.id.btnBackToLogin);     // Tombol masuk kembali di Step 4

        tvBackToLogin = findViewById(R.id.tvBackToLogin);       // Teks "Masuk" di Step 1
        tvResendCode = findViewById(R.id.tvResendCode);

        // ==========================================
        // LOGIKA TOMBOL & NAVIGASI ALUR
        // ==========================================

        // TOMBOL KEMBALI / BACK (Pojok kiri atas)
        btnBack.setOnClickListener(v -> {
            if (layoutStep2.getVisibility() == View.VISIBLE) {
                switchStep(layoutStep2, layoutStep1);
            } else if (layoutStep3.getVisibility() == View.VISIBLE) {
                switchStep(layoutStep3, layoutStep2);
            } else {
                finish(); // Keluar halaman, balik ke halaman login utama
            }
        });

        // TEKS MASUK (Di bagian bawah Step 1)
        tvBackToLogin.setOnClickListener(v -> {
            finish(); // Tutup halaman ini untuk kembali ke login
        });

        // STEP 1 -> STEP 2 (Klik Kirim Kode Verifikasi)
        btnResetPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Masukkan email Anda terlebih dahulu!", Toast.LENGTH_SHORT).show();
            } else {
                // Pindah ke step input OTP
                switchStep(layoutStep1, layoutStep2);
            }
        });

        // STEP 2 -> STEP 3 (Klik Verifikasi OTP)
        btnVerifyOtp.setOnClickListener(v -> {
            // Sementara bypass langsung lolos ke step buat password baru
            switchStep(layoutStep2, layoutStep3);
        });

        // STEP 3 -> STEP 4 (Klik Atur Ulang Password Baru)
        btnResetPasswordFinal.setOnClickListener(v -> {
            String pass = etNewPassword.getText().toString().trim();
            String confirm = etConfirmNewPassword.getText().toString().trim();

            if (pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(confirm)) {
                Toast.makeText(this, "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show();
            } else {
                // Sukses update password, pindah ke step berhasil
                switchStep(layoutStep3, layoutStep4);
                btnBack.setVisibility(View.GONE); // Sembunyikan tombol back panah atas karena sudah sukses
            }
        });

        // STEP 4 -> SELESAI (Kembali ke halaman utama/Login)
        btnBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Helper fungsi untuk mengatur visibilitas pergantian layout step
    private void switchStep(LinearLayout layoutLama, LinearLayout layoutBaru) {
        layoutLama.setVisibility(View.GONE);
        layoutBaru.setVisibility(View.VISIBLE);
    }
}