package com.example.mynotes;

import android.os.Build;

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
        return savedNotes.size();
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

    public boolean removeItem(String title)
    {
        if(savedNotes.containsKey(title))
        {
            savedNotes.remove(title);
            Iterator<HashMap<String,String>> it = titleArrayList.iterator();
            while(it.hasNext())
            {
                HashMap<String,String> item = it.next();
                if(item.containsValue(title))
                {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void changeContentOfTitle(String title, String content) {
        savedNotes.replace(title,content);
    }
}
