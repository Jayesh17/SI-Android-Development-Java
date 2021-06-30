package com.example.mynotes;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Notes {

    public static HashMap<String,String> savedNotes;
    ArrayList<HashMap<String,String>> titleArrayList;

    Notes()
    {
        savedNotes = new HashMap<>();
        titleArrayList = new ArrayList<>();
    }

    public boolean addNewNote(String title,String content)
    {
        if(savedNotes.containsKey(title))
        {
            return false;
        }
        else
        {
            savedNotes.put(title,content);
            Log.d("add",title+" "+content);
            addToList(title);
        }
        return true;
    }
    public void populateSavedData(HashMap<String,String> data)
    {
        Iterator<Map.Entry<String,String>> it = data.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<String,String> en = it.next();
            savedNotes.put(en.getKey(),en.getValue());
            addToList(en.getKey());
        }
    }
    public int getCount()
    {
        return titleArrayList.size();
    }
    public void addToList(String title)
    {
        HashMap<String,String> entry = new HashMap<>();
        entry.put("title",title);
        titleArrayList.add(entry);
    }

    public ArrayList<HashMap<String, String>> getTitleArrayList() {
        return titleArrayList;
    }

    public String getContentByTitle(String title)
    {
        return savedNotes.get(title);
    }

    public boolean removeItem(int pos,String title)
    {
        if(savedNotes.containsKey(title))
        {
            savedNotes.remove(title);
            titleArrayList.remove(pos);
            MainActivity.adapter.notifyDataSetChanged();
        }
        else
        {
            return false;
        }
        return true;
    }
    public void deleteNoteByTitle(String title)
    {
        int ind = -1,count;
        savedNotes.remove(title);
        Iterator<HashMap<String,String>> it = titleArrayList.iterator();
        while (it.hasNext())
        {
            ind++;
            HashMap<String,String> en = it.next();
            if(en.containsValue(title))
            {
                break;
            }
        }
        titleArrayList.remove(ind);
        MainActivity.adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeContentOfTitle(String title, String content) {
        savedNotes.replace(title,content);
    }
}
