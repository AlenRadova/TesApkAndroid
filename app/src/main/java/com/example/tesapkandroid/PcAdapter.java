package com.example.tesapkandroid;

import android.content.Context;
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

            // Atur background teks badge (Hijau)
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_badge_aktif);
            holder.tvStatusBadge.setTextColor(ContextCompat.getColor(context, android.R.color.white));

        } else if (pc.getStatus().equalsIgnoreCase("Maintenance")) {
            // Gunakan drawable lingkaran orange milikmu
            holder.layoutIconContainer.setBackgroundResource(R.drawable.circle_orange);

            // Atur background teks badge (Jingga/Orange)
            holder.tvStatusBadge.setBackgroundResource(R.drawable.bg_badge_selesai); // Sementara pakai ini atau buat baru bg_badge_maintenance
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