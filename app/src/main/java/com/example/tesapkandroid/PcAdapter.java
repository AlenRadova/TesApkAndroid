package com.example.tesapkandroid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PcAdapter extends RecyclerView.Adapter<PcAdapter.PcViewHolder> {

    private Context context;
    private List<PcModel> pcList;

    public PcAdapter(Context context, List<PcModel> pcList) {
        this.context = context;
        this.pcList = pcList;
    }

    @NonNull
    @Override
    public PcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pc, parent, false);
        return new PcViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PcViewHolder holder, int position) {
        PcModel pc = pcList.get(position);

        holder.tvKodePc.setText(pc.getKodePc());
        holder.tvLokasiPc.setText(pc.getLokasiPc());
        holder.tvStatusBadge.setText(pc.getStatus());

        // LOGIKA PERUBAHAN WARNA DINAMIS BERDASARKAN STATUS
        if (pc.getStatus().equalsIgnoreCase("Tersedia")) {
            // Gunakan drawable lingkaran hijau milikmu
            holder.layoutIconContainer.setBackgroundResource(R.drawable.circle_green);

            // Ikon monitor di dalam lingkaran diubah menjadi warna putih solid agar jelas
            holder.imgPcIcon.setColorFilter(Color.WHITE);

            // Atur background teks badge dengan warna Hijau Pekat Solid (#388E3C)
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_badge_aktif);
            holder.tvStatusBadge.getBackground().setTint(Color.parseColor("#388E3C"));
            holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, android.R.color.white));

        } else if (pc.getStatus().equalsIgnoreCase("Maintenance")) {
            // Gunakan drawable lingkaran orange milikmu
            holder.layoutIconContainer.setBackgroundResource(R.drawable.circle_orange);

            // Ikon monitor di dalam lingkaran diubah menjadi warna putih solid agar jelas
            holder.imgPcIcon.setColorFilter(Color.WHITE);

            // Atur background teks badge dengan warna Jingga/Orange Pekat Solid (#F57C00)
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_badge_selesai);
            holder.tvStatusBadge.getBackground().setTint(Color.parseColor("#F57C00"));
            holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, android.R.color.white));

        } else {
            // Logika cadangan untuk status lainnya (misal: "Sedang Dipakai")
            holder.layoutIconContainer.setBackgroundResource(R.drawable.circle_green); // atau sesuaikan lingkaran abu/merah
            holder.imgPcIcon.setColorFilter(Color.WHITE);

            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_badge_selesai);
            holder.tvStatusBadge.getBackground().setTint(Color.parseColor("#D32F2F")); // Merah
            holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return pcList.size();
    }

    public static class PcViewHolder extends RecyclerView.ViewHolder {
        TextView tvKodePc, tvLokasiPc, tvStatusBadge;
        FrameLayout layoutIconContainer;
        ImageView imgPcIcon;

        public PcViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKodePc = itemView.findViewById(R.id.tvKodePc);
            tvLokasiPc = itemView.findViewById(R.id.tvLokasiPc);
            tvStatusBadge = itemView.findViewById(R.id.tvStatusBadge);
            layoutIconContainer = itemView.findViewById(R.id.layoutIconContainer);
            imgPcIcon = itemView.findViewById(R.id.imgPcIcon);
        }
    }
}