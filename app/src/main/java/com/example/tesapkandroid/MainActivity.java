package com.example.tesapkandroid;

import android.content.Intent;
import android.content.SharedPreferences; // Tambahkan import ini
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    // 1. Deklarasikan variabel untuk teks Register dan Forgot Password
    private TextView tvRegister, tvForgotPassword;

    // Deklarasikan objek DatabaseHelper
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // 2. Hubungkan variabel Java dengan ID yang ada di activity_main.xml
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // =======================================================
        // AKSI TOMBOL LOGIN
        // =======================================================
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = dbHelper.checkLogin(email, password);

                if (cursor != null && cursor.moveToFirst()) {
                    int indexNama = cursor.getColumnIndex(DatabaseHelper.COL_NAMA);
                    int indexRole = cursor.getColumnIndex(DatabaseHelper.COL_ROLE);

                    String namaUser = cursor.getString(indexNama);
                    String roleUser = cursor.getString(indexRole); // Ini membaca ADMIN atau USER dari SQLite

                    cursor.close();

                    // ---> KODE TAMBAHAN UNTUK MENYIMPAN ROLE KE SHAREDPREFERENCES <---
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("role", roleUser); // Simpan role ke memori hp
                    editor.apply();
                    // -----------------------------------------------------------------

                    Toast.makeText(MainActivity.this, "Selamat Datang, " + namaUser, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    intent.putExtra("ROLE", roleUser);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(MainActivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
                    if (cursor != null) cursor.close();
                }
            }
        });

        // =======================================================
        // AKSI TEKS REGISTER / DAFTAR
        // =======================================================
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // =======================================================
        // AKSI TEKS LUPA PASSWORD (BARU)
        // =======================================================
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}