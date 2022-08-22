package com.example.test2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TodoList1";
    private static final String TABLE_NAME = "category";
    private static final int VERSION = 1;
    private static final String CATEGORY_ID = "category_id";
    private static final String CATEGORY_NAME = "category_name";


    private static final String TABLE_NAME2 = "subcategory";
    private static final String SUBCATEGORY_ID = "subcategory_id";
    private static final String SUBCATEGORY_NAME = "subcategory_name";
    private static final String CATEGORY_ID_FK= "id";


    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +  " (" +
                CATEGORY_ID + " INTEGER NOT NULL CONSTRAINT category_pk PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_NAME + " VARCHAR(20) NOT NULL " +
               ");";
        db.execSQL(sql);

        String sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 +  " (" +
                SUBCATEGORY_ID + " INTEGER NOT NULL CONSTRAINT subcategory_pk PRIMARY KEY AUTOINCREMENT, " +
                SUBCATEGORY_NAME + " VARCHAR(20) NOT NULL," +
                CATEGORY_ID_FK + " INTEGER " +");";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // add Category
    public boolean addCategories(String name) {
        Categories cat = getCategory(name);
        if (cat == null){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CATEGORY_NAME, name);

        return db.insert(TABLE_NAME, null, cv) != -1;
        }


        return false;
    }



    public boolean addSubCategories(

            String name,
            int category_id

    ) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SUBCATEGORY_NAME, name);
        cv.put(CATEGORY_ID_FK, category_id);
        return db.insert(TABLE_NAME2, null, cv) != -1;
    }

    public Categories getCategory(String cat_name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + CATEGORY_NAME + "=?",
                new String[]{cat_name});
        if (cursor.moveToFirst()) {
            return new Categories(
                    cursor.getInt(0),
                    cursor.getString(1)

            );
        } else {
            return null;
        }

    }
    public Cursor getSubCategory(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + CATEGORY_ID_FK + "=?"+";";
        return db.rawQuery(sql, new String[]{String.valueOf(id)});
    }

    public Cursor getAllCategory() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + ";";
        return db.rawQuery(sql, null);

    }

    public Cursor getAllSubCategory() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2 + ";";
        return db.rawQuery(sql, null);

    }

        // delete Employee
    public boolean deleteCat(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME2, CATEGORY_ID_FK+"=?", new String[]{String.valueOf(id)});
        return db.delete(TABLE_NAME, CATEGORY_ID+"=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteSubCat(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_NAME2, SUBCATEGORY_ID+"=?", new String[]{String.valueOf(id)}) > 0;
    }



        public boolean updateSubCat(int id,String req_name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SUBCATEGORY_NAME, req_name);

            return db.update(
                TABLE_NAME2, cv, SUBCATEGORY_ID + "=?", new String[]{String.valueOf(id)}
        ) > 0;
    }

}
