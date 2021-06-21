package com.example.campusrecruitment.DBManipulation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.campusrecruitment.Params.DBParams;
import com.example.campusrecruitment.Params.StudentRegistrationParams;

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context)
    {
        super(context, DBParams.DB_NAME,null,DBParams.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("init_tbl","table created:"+ StudentRegistrationParams.CREATE_QUERY);
        db.execSQL(StudentRegistrationParams.CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
