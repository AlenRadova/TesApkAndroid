package com.example.tesapkandroid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    // 1. Deklarasikan objek DatabaseHelper
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

        // 2. Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 3. Panggil fungsi cek login dari SQLite
                Cursor cursor = dbHelper.checkUserLogin(email, password);

                // 4. Jika cursor memiliki data (moveToFirst berstatus true), berarti akun ditemukan!
                if (cursor != null && cursor.moveToFirst()) {

                    // Ambil data Nama dan Role dari kolom database
                    // Kita gunakan getColumnIndex untuk mencari nomor urut kolomnya
                    int indexNama = cursor.getColumnIndex(DatabaseHelper.COL_NAMA);
                    int indexRole = cursor.getColumnIndex(DatabaseHelper.COL_ROLE);

                    String namaUser = cursor.getString(indexNama);
                    String roleUser = cursor.getString(indexRole);

                    // Tutup cursor agar tidak membebani memori
                    cursor.close();

                    // Tampilkan pesan sukses menyapa nama user
                    Toast.makeText(MainActivity.this, "Selamat Datang, " + namaUser, Toast.LENGTH_SHORT).show();

                    // Lempar ke DashboardActivity membawa Flag ROLE asli dari DB
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    intent.putExtra("ROLE", roleUser);
                    startActivity(intent);
                    finish();

                } else {
                    // Jika data tidak cocok atau tidak ditemukan di tabel database
                    Toast.makeText(MainActivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
                    if (cursor != null) cursor.close();
                }
            }
        });
    }
}