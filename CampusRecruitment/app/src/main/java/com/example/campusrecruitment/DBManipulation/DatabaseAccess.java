package com.example.campusrecruitment.DBManipulation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.campusrecruitment.Params.StudentRegistrationParams;

public class DatabaseAccess {

    private static DatabaseAccess instance=null;
    private DBOpenHelper dbOpenHelper;

    private DatabaseAccess(Context context)
    {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context)
    {
        if(instance==null)
        {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }
    public void createInitialTables()
    {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL(StudentRegistrationParams.CREATE_QUERY);
        db.close();
    }

    public boolean saveStudentInformation(final String email,final String name,final String pass) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
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
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

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

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
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

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
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
