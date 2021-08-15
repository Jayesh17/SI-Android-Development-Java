package com.example.listviewprac.DBdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.listviewprac.DBparams.Params;
import java.util.HashMap;

public class DBHandler extends SQLiteOpenHelper
{
    public DBHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateQuery = "CREATE TABLE "+Params.LINKS_TBL+"("+Params.KEY_ID+" INTEGER IDENTITY(1,1) PRIMARY KEY,"+Params.TITLE+" VARCHAR(30) NOT NULL,"+Params.LINK+" VARCHAR(100) NOT NULL)";
        Log.d("logs","Create running query : "+CreateQuery);
        //to run the query
        db.execSQL(CreateQuery);
    }
    public boolean addRecord(String title, String link)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean done = false;
        try
        {
            ContentValues values = new ContentValues();
            values.put(Params.TITLE,title);
            values.put(Params.LINK,link);
            long id = db.insert(Params.LINKS_TBL,null,values);
            if(id == -1)
            {
                Log.d("logs","addRecord Failed.");
                done = false;
            }
            else
            {
                Log.d("logs","Link "+link+" with title "+title+" has been added, ID ."+id);
                done = true;
            }
        }
        catch (Exception e)
        {
            Log.d("logs",e.toString());
        }
        finally {
            db.close();
        }
        return done;
    }

    public boolean deleteRecord(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Boolean done = false;
        try
        {
            int rows = db.delete(Params.LINKS_TBL,Params.TITLE+"='"+title+"'",null);
            //Log.d("logs",);
            if(rows > 0)
            {
                Log.d("logs",rows+" with title "+title+"Deleted.");
                done = true;
            }
            else
            {
                Log.d("logs","deletion failed");
                done = false;
            }
        }
        catch (Exception e)
        {
            Log.d("Logs",e.toString());
        }
        finally {
            db.close();
        }
        return done;
    }

    public HashMap<String,String> getAllRecords()
    {
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String,String> recs = new HashMap<>();
        try
        {
            String SelectQuery = "Select * from "+Params.LINKS_TBL;
            Cursor cursor = db.rawQuery(SelectQuery,null);
             if(cursor.moveToFirst())
             {
                 do {
                    recs.put(cursor.getString(1),cursor.getString(2));
                 }while(cursor.moveToNext());
             }
        }
        catch(Exception e)
        {
            Log.d("logs",e.toString());
        }
        finally {
            db.close();
        }
        return recs;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean editByName(String title, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Params.TITLE,name);
            int rows = db.update(Params.LINKS_TBL,values,Params.TITLE+"=?",new String[]{title});
            if(rows==1)
            {
                Log.d("inDB","success");
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            Log.d("logsERR",e.toString());
            return false;
        }
        finally {
            db.close();
        }
    }

    public boolean editLink(String title, String link) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Params.LINK,link);
            int rows = db.update(Params.LINKS_TBL,values,Params.TITLE+"=?",new String[]{title});
            if(rows==1)
            {
                Log.d("inDB","success");
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            Log.d("logsERR",e.toString());
            return false;
        }
        finally {
            db.close();
        }
    }
}
