package com.example.tesapkandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DataPcFragment extends Fragment {

    private RecyclerView rvDataPc;
    private PcAdapter pcAdapter;
    private List<PcModel> pcList = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Menghubungkan fragment ke file layout fragment_data_pc.xml
        View view = inflater.inflate(R.layout.fragment_data_pc, container, false);

        // Inisialisasi RecyclerView dan DatabaseHelper
        rvDataPc = view.findViewById(R.id.rvDataPc);
        dbHelper = new DatabaseHelper(getContext());

        // Atur agar RecyclerView tersusun ke bawah (Vertikal)
        rvDataPc.setLayoutManager(new LinearLayoutManager(getContext()));

        // Ambil data dari SQLite tabel data_pc
        pcList = dbHelper.getAllPc();

        // Pasang data ke Adapter lalu set ke RecyclerView
        pcAdapter = new PcAdapter(getContext(), pcList);
        rvDataPc.setAdapter(pcAdapter);

        // Aksi Tombol Back (Opsional, jika ingin kembali ke Dashboard utama)
        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }
}