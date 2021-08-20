package com.addon.config;
import bms.quote.Ldb;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import bms.helper.tools.LOG;
import java.util.Arrays;
import android.content.Context;
import bms.helper.http.SQLiteDBHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

public class SpecialLdb extends Ldb {


    private static SpecialLdb INSTANCE  = null;
    private SQLiteDBHelper mHelper = null;
    private SQLiteDatabase mDB = null;
    private SpecialLdb(Context context) {
        mHelper = new SQLiteDBHelper(context);
        mDB = mHelper.getWritableDatabase();
    }

    public static SpecialLdb getInstance(Context context) {
        if (INSTANCE == null) {
            return new SpecialLdb(context);
        }
        return INSTANCE;
    }



    public SpecialLdb(File f) {
        super();
    }
/*
    @Override
    public byte[] get(String s) {
        byte[] key=s.getBytes();
        return get(key);
    }

    @Override
    public byte[] get(byte[] key) {
        
        
         
        
// select * from Orders where CustomName = 'Bor'
        Cursor cursor = mDB.query(SQLiteDBHelper.TABLE_NAME,
                                  new String[]{"v"},
                                  "k = ?",
                                  new String[]{key},
                                  null, null, null);

        if (cursor.getCount() > 0) {
            List<Student> studentList = new ArrayList<Student>(cursor.getCount());
            while (cursor.moveToNext()) {
                Student student = parseStudent(cursor);
                studentList.add(student);
            }
            cursor.close();
            return studentList;
        }
        return null;



    }

    @Override
    public void put(byte[] key, byte[] v) {

        mDB.beginTransaction();
        ContentValues contentValues=new ContentValues();
        contentValues.put("k", key);
        contentValues.put("v", v);

        mDB.insertOrThrow(SQLiteDBHelper.TABLE_NAME, null, contentValues);
        mDB.setTransactionSuccessful();
        mDB.endTransaction();



    }
    */
}
