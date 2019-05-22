package com.hrishi_3331.hrishi_studio.tronix2021;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AttendanceDataManager extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "attendance.db";
    private static  final String TABLE_NAME = "ATTENDANCE";
    private static final String COL_1 = "DATE";
    private static final String COL_2 = "MCP";
    private static final String COL_3 = "MAL";
    private static final String COL_4 = "ACD";
    private static final String COL_5 = "SNS";
    private static final String COL_6 = "EMF";

    public AttendanceDataManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + "DATE integer primary key, MCP integer, MAL integer, ACD integer, SNS integer, EMF integer" + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists "+ TABLE_NAME );
    }


}
