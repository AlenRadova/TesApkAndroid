package com.example.tesapkandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama dan Versi Database
    private static final String DATABASE_NAME = "LabPC.db";
    private static final int DATABASE_VERSION = 1;

    // Nama Tabel dan Kolom
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_NAMA = "nama";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";
    public static final String COL_NIM = "nim";

    // Query untuk membuat tabel
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAMA + " TEXT, "
            + COL_NIM + " TEXT, " // Tambah baris ini
            + COL_EMAIL + " TEXT UNIQUE, "
            + COL_PASSWORD + " TEXT, "
            + COL_ROLE + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Eksekusi pembuatan tabel saat database pertama kali dibuat
        db.execSQL(CREATE_TABLE_USERS);

        // ISI DATA AWAL: Kita otomatis daftarkan 1 akun Admin bawaan pabrik agar bisa dicoba langsung
        ContentValues adminValues = new ContentValues();
        adminValues.put(COL_NAMA, "Administrator Lab");
        adminValues.put(COL_EMAIL, "admin@admin.com");
        adminValues.put(COL_PASSWORD, "admin123");
        adminValues.put(COL_ROLE, "ADMIN");
        db.insert(TABLE_USERS, null, adminValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // FUNGSI 1: Untuk Registrasi Akun Mahasiswa Baru
    public boolean registerUser(String nama, String nim, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAMA, nama);
        contentValues.put(COL_NIM, nim); // Tambah baris ini
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // FUNGSI 2: Untuk Validasi Login (Memeriksa email, password, sekaligus mengambil Role)
    public Cursor checkUserLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query: SELECT * FROM users WHERE email = ? AND password = ?
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";

        return db.rawQuery(query, new String[]{email, password});
    }
}
