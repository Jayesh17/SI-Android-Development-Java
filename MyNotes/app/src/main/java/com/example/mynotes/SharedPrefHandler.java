package com.example.mynotes;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class SharedPrefHandler {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Notes notes;

    SharedPrefHandler(SharedPreferences sp)
    {
        this.sp = sp;
        editor = sp.edit();
        /*editor.clear();
        editor.apply();*/
        notes = MainActivity.notes;
        notes.populateSavedData((HashMap<String, String>) sp.getAll());
    }
    //1->done, 0->something.-1->exist
    public int addNewNote(String title,String content)
    {
        boolean dup = notes.addNewNote(title,content);
        if(!dup)
        {
            return -1;
        }
        try {
            editor.putString(title,content);
            editor.apply();
        }
        catch (Exception e)
        {
            Log.d("addERR:",e.toString());
            return 0;
        }
        return 1;
    }
    public void deleteNote(int pos,String title)
    {
        editor.remove(title);
        editor.apply();
        if(notes.removeItem(pos,title))
        {
            Log.d("delete","Success");
        }
        else
        {
            Toast.makeText(MainActivity.context,"couldn't delete.",Toast.LENGTH_LONG).show();
        }
    }
    public void deleteNoteByTitle(String title)
    {
        editor.remove(title);
        editor.apply();
        notes.deleteNoteByTitle(title);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editNote(String prevTitle, String title, String content)
    {
        String isStored = sp.getString(title,"NA");
        if(isStored.equals("NA"))
        {
            deleteNoteByTitle(prevTitle);
            addNewNote(title,content);
        }
        else {
            editor.putString(title,content);
            notes.changeContentOfTitle(title,content);
        }
    }
}
