package com.example.tesapkandroid;

public class PcModel {
    private String kodePc;
    private String lokasiPc;
    private String status; // "Tersedia" atau "Maintenance"

    // Constructor
    public PcModel(String kodePc, String lokasiPc, String status) {
        this.kodePc = kodePc;
        this.lokasiPc = lokasiPc;
        this.status = status;
    }

    // Getter dan Setter
    public String getKodePc() { return kodePc; }
    public void setKodePc(String kodePc) { this.kodePc = kodePc; }

    public String getLokasiPc() { return lokasiPc; }
    public void setLokasiPc(String lokasiPc) { this.lokasiPc = lokasiPc; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}