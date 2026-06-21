package com.example.tesapkandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RiwayatFragment extends Fragment {

    public RiwayatFragment() {
        // Diperlukan constructor kosong
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Menghubungkan fragment dengan layout xml yang sudah kita buat sebelumnya
        return inflater.inflate(R.layout.activity_riwayat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Di sini nanti kamu bisa menginisialisasi RecyclerView, EditText search,
        // dan tombol filter yang ada di dalam layout riwayat.
    }
}