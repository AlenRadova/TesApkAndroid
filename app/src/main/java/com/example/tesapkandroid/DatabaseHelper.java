package com.example.tesapkandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama dan Versi Database
    private static final String DATABASE_NAME = "LabPC.db";
    private static final int DATABASE_VERSION = 2; // Dinaikkan ke versi 2 karena ada struktur tabel baru

    // Nama Tabel dan Kolom Users
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_NAMA = "nama";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";
    public static final String COL_NIM = "nim";

    // BARU: Nama Tabel dan Kolom PC
    public static final String TABLE_PC = "data_pc";
    public static final String COL_KODE_PC = "kode_pc";
    public static final String COL_LOKASI_PC = "lokasi_pc";
    public static final String COL_STATUS_PC = "status_pc";

    // Query untuk membuat tabel Users
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAMA + " TEXT, "
            + COL_NIM + " TEXT, "
            + COL_EMAIL + " TEXT UNIQUE, "
            + COL_PASSWORD + " TEXT, "
            + COL_ROLE + " TEXT);";

    // BARU: Query untuk membuat tabel PC
    private static final String CREATE_TABLE_PC = "CREATE TABLE " + TABLE_PC + " ("
            + COL_KODE_PC + " TEXT PRIMARY KEY, "
            + COL_LOKASI_PC + " TEXT, "
            + COL_STATUS_PC + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Eksekusi pembuatan semua tabel
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PC);

        // ISI DATA AWAL: Akun Admin bawaan
        ContentValues adminValues = new ContentValues();
        adminValues.put(COL_NAMA, "Administrator Lab");
        adminValues.put(COL_EMAIL, "admin@admin.com");
        adminValues.put(COL_PASSWORD, "admin123");
        adminValues.put(COL_ROLE, "ADMIN");
        db.insert(TABLE_USERS, null, adminValues);

        // BARU: ISI DATA AWAL PC (Sesuai gambar contoh desainmu)
        insertInitialPcData(db, "PC-001", "Lab C - Komputer 1", "Maintenance");
        insertInitialPcData(db, "PC-002", "Lab A - Komputer 2", "Tersedia");
        insertInitialPcData(db, "PC-003", "Lab B - Komputer 3", "Tersedia");
        insertInitialPcData(db, "PC-004", "Lab B - Komputer 4", "Tersedia");
        insertInitialPcData(db, "PC-005", "Lab A - Komputer 5", "Tersedia");
        insertInitialPcData(db, "PC-006", "Lab B - Komputer 6", "Tersedia");
    }

    // Helper method untuk isi data PC awal
    private void insertInitialPcData(SQLiteDatabase db, String kode, String lokasi, String status) {
        ContentValues pcValues = new ContentValues();
        pcValues.put(COL_KODE_PC, kode);
        pcValues.put(COL_LOKASI_PC, lokasi);
        pcValues.put(COL_STATUS_PC, status);
        db.insert(TABLE_PC, null, pcValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PC);
        onCreate(db);
    }

    // FUNGSI 1: Untuk Registrasi Akun Mahasiswa Baru
    public boolean registerUser(String nama, String nim, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAMA, nama);
        contentValues.put(COL_NIM, nim);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // FUNGSI 2: Untuk Validasi Login
    public Cursor checkUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
        return db.rawQuery(query, new String[]{email, password});
    }

    // BARU: FUNGSI 3: Mengambil Semua Data PC dari Database untuk RecyclerView
    public List<PcModel> getAllPc() {
        List<PcModel> pcList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PC, null);

        if (cursor.moveToFirst()) {
            do {
                String kode = cursor.getString(cursor.getColumnIndexOrThrow(COL_KODE_PC));
                String lokasi = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOKASI_PC));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS_PC));

                pcList.add(new PcModel(kode, lokasi, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pcList;
    }
}