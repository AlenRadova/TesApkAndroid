package com.example.tesapkandroid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    private TextView tvProfileName, tvProfileNim, tvProfileRole;
    private TextInputEditText etProfileEmail, etProfilePassword;
    private Button btnLogout, btnSimpan;
    private DatabaseHelper dbHelper;
    private String currentUserId;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DatabaseHelper(getContext());

        // Inisialisasi Komponen
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileNim = view.findViewById(R.id.tvProfileNim);
        tvProfileRole = view.findViewById(R.id.tvProfileRole);
        etProfileEmail = view.findViewById(R.id.etProfileEmail); // ID baru dari XML
        etProfilePassword = view.findViewById(R.id.etProfilePassword); // ID baru dari XML
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSimpan = view.findViewById(R.id.btnSimpan); // ID tombol simpan dari XML

        loadUserProfileData();

        // Aksi Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String newEmail = etProfileEmail.getText().toString().trim();
            String newPass = etProfilePassword.getText().toString().trim();

            if (newEmail.isEmpty()) {
                Toast.makeText(getActivity(), "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                boolean isUpdated = dbHelper.updateUser(currentUserId, newEmail, newPass);
                if (isUpdated) {
                    Toast.makeText(getActivity(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Aksi Tombol Logout
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Berhasil keluar akun", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
        });

        return view;
    }

    private void loadUserProfileData() {
        if (getActivity() != null && getActivity().getIntent() != null) {
            String loggedInRole = getActivity().getIntent().getStringExtra("ROLE");
            if (loggedInRole == null) return;

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.COL_ROLE + " = ?", new String[]{loggedInRole});

            if (cursor != null && cursor.moveToFirst()) {
                currentUserId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAMA));
                String nim = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NIM));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE));

                tvProfileName.setText(nama);
                etProfileEmail.setText(email); // Isi otomatis ke EditText
                tvProfileRole.setText("Akses Akun: " + role);

                if ("ADMIN".equalsIgnoreCase(role)) {
                    tvProfileNim.setText("ID Petugas: Lab-Admin");
                } else {
                    tvProfileNim.setText("NIM: " + (nim != null ? nim : "-"));
                }

                cursor.close();
            }
        }
    }
}