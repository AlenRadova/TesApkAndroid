package com.example.tesapkandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LabPC.db";
    private static final int DATABASE_VERSION = 3;

    // Nama Tabel dan Kolom Users
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_NAMA = "nama";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";
    public static final String COL_NIM = "nim";

    // Nama Tabel dan Kolom PC
    public static final String TABLE_PC = "data_pc";
    public static final String COL_KODE_PC = "kode_pc";
    public static final String COL_LOKASI_PC = "lokasi_pc";
    public static final String COL_STATUS_PC = "status_pc";

    // Nama Tabel dan Kolom Peminjaman
    public static final String TABLE_PEMINJAMAN = "peminjaman";
    public static final String COL_ID_PINJAM = "id_pinjam";
    public static final String COL_NIM_PINJAM = "nim";
    public static final String COL_NAMA_PINJAM = "nama";
    public static final String COL_KODE_PC_PINJAM = "kode_pc";
    public static final String COL_STATUS_PINJAMAN = "status_pinjaman";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAMA + " TEXT, "
            + COL_NIM + " TEXT, "
            + COL_EMAIL + " TEXT UNIQUE, "
            + COL_PASSWORD + " TEXT, "
            + COL_ROLE + " TEXT);";

    private static final String CREATE_TABLE_PC = "CREATE TABLE " + TABLE_PC + " ("
            + COL_KODE_PC + " TEXT PRIMARY KEY, "
            + COL_LOKASI_PC + " TEXT, "
            + COL_STATUS_PC + " TEXT);";

    private static final String CREATE_TABLE_PEMINJAMAN = "CREATE TABLE " + TABLE_PEMINJAMAN + " ("
            + COL_ID_PINJAM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NIM_PINJAM + " TEXT, "
            + COL_NAMA_PINJAM + " TEXT, "
            + COL_KODE_PC_PINJAM + " TEXT, "
            + COL_STATUS_PINJAMAN + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PC);
        db.execSQL(CREATE_TABLE_PEMINJAMAN);

        ContentValues adminValues = new ContentValues();
        adminValues.put(COL_NAMA, "Administrator Lab");
        adminValues.put(COL_NIM, "12345678");
        adminValues.put(COL_EMAIL, "admin@admin.com");
        adminValues.put(COL_PASSWORD, "admin123");
        adminValues.put(COL_ROLE, "ADMIN");
        db.insert(TABLE_USERS, null, adminValues);

        insertInitialPcData(db, "PC-001", "Lab C - Komputer 1", "Maintenance");
        insertInitialPcData(db, "PC-002", "Lab A - Komputer 2", "Tersedia");
        insertInitialPcData(db, "PC-003", "Lab B - Komputer 3", "Tersedia");
        insertInitialPcData(db, "PC-004", "Lab B - Komputer 4", "Tersedia");
        insertInitialPcData(db, "PC-005", "Lab A - Komputer 5", "Tersedia");
        insertInitialPcData(db, "PC-006", "Lab B - Komputer 6", "Tersedia");
    }

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEMINJAMAN);
        onCreate(db);
    }

    // --- FUNGSI UPDATE USER (DITAMBAHKAN DI SINI) ---
    public boolean updateUser(String id, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);

        // Update berdasarkan ID yang didapat dari query sebelumnya
        long result = db.update(TABLE_USERS, contentValues, COL_ID + " = ?", new String[]{id});
        return result != -1;
    }

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

    public Cursor checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
        return db.rawQuery(query, new String[]{email, password});
    }

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

    public boolean insertPeminjaman(String nim, String nama, String kodePc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NIM_PINJAM, nim);
        values.put(COL_NAMA_PINJAM, nama);
        values.put(COL_KODE_PC_PINJAM, kodePc);
        values.put(COL_STATUS_PINJAMAN, "Aktif");

        long result = db.insert(TABLE_PEMINJAMAN, null, values);
        return result != -1;
    }

    public int getJumlahPcDipinjam() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PEMINJAMAN + " WHERE " + COL_STATUS_PINJAMAN + " = 'Aktif'", null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    public Cursor getPeminjamanAktifByNama(String nama) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PEMINJAMAN + " WHERE " + COL_NAMA_PINJAM + " = ? AND " + COL_STATUS_PINJAMAN + " = 'Aktif' ORDER BY " + COL_ID_PINJAM + " DESC LIMIT 1";
        return db.rawQuery(query, new String[]{nama});
    }
}