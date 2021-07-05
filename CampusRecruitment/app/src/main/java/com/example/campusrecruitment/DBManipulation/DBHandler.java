package com.example.campusrecruitment.DBManipulation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    //public void storeStudentRegistration();

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean saveStudentInformation(final String email,final String name,final String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isDone = false;
        try
        {
            ContentValues values =new ContentValues();
            values.put(StudentRegistrationParams.EMAIL_ID,email);
            values.put(StudentRegistrationParams.SNAME,name);
            values.put(StudentRegistrationParams.PASSWORD,pass);

            long id = db.insert(StudentRegistrationParams.REGISTER_STUDENTS_TBL,null,values);
            if(id==-1)
            {
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            Log.d("StudentRegErr",e.toString());
            return false;
        }
        finally {
            db.close();
        }
    }

    public boolean checkIfStudentExists(final String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
           final String selectQuery = "Select "+StudentRegistrationParams.SNAME+" from "+StudentRegistrationParams.REGISTER_STUDENTS_TBL+" where "+StudentRegistrationParams.EMAIL_ID+" like '"+email+"';";

            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount()==0)
            {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }
        catch (Exception e)
        {
            Log.d("CheckErr",e.toString());
            return false;
        }
        finally {
            db.close();
        }
    }

    public StudentRegistrationParams.status checkUserCredentials(final String email,final String pass) {

        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            final String selectQuery = "Select * from "+StudentRegistrationParams.REGISTER_STUDENTS_TBL+" where "+StudentRegistrationParams.EMAIL_ID+" like '"+email+"';";
            Cursor cursor = db.rawQuery(selectQuery,null);
            if(cursor.getCount()==0){
                cursor.close();
                return StudentRegistrationParams.status.NOT_EXIST;
            }
            cursor.moveToFirst();
            String spass=cursor.getString(cursor.getColumnIndex(StudentRegistrationParams.PASSWORD));
            if(!pass.equals(spass)){
                cursor.close();
                return StudentRegistrationParams.status.WRONG_PASS;
            }
            cursor.close();
            return StudentRegistrationParams.status.DONE;

        }
        catch (Exception e)
        {
            Log.d("loginErr",e.toString());
            return StudentRegistrationParams.status.ERROR;
        }
        finally {
            db.close();
        }
    }

    public boolean changePasswordOfStudent(final String email, final String pass) {

        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(StudentRegistrationParams.PASSWORD,pass);
            Log.d("inDB","wait");
            int rows = db.update(StudentRegistrationParams.REGISTER_STUDENTS_TBL,values,StudentRegistrationParams.EMAIL_ID+"=?",new String[]{email});
            if(rows==1)
            {
                Log.d("inDB","success");
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            Log.d("updateERR",e.toString());
            return false;
        }
    }
}
