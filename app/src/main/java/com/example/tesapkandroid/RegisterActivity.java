package com.example.tesapkandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    // Menggunakan TextInputEditText sesuai komponen di XML barumu
    private TextInputEditText etNama, etNim, etEmail, etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Pengaturan padding full screen otomatis
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        // Inisialisasi ID sesuai dengan file XML barumu
        etNama = findViewById(R.id.etNama);
        etNim = findViewById(R.id.etNim);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Eksekusi tombol Daftar Sekarang
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = etNama.getText().toString().trim();
                String nim = etNim.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Validasi input kosong
                if (nama.isEmpty() || nim.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi panjang password minimal 6 karakter sesuai hint XML
                if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Simpan data ke SQLite (Default Role sebagai 'USER')
                boolean isSuccess = dbHelper.registerUser(nama, nim, email, password, "USER");

                if (isSuccess) {
                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil! Silakan Login", Toast.LENGTH_LONG).show();
                    finish(); // Menutup halaman register dan otomatis kembali ke login
                } else {
                    Toast.makeText(RegisterActivity.this, "Registrasi Gagal! Email mungkin sudah digunakan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Aksi teks "Masuk" jika ingin kembali ke halaman login
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}