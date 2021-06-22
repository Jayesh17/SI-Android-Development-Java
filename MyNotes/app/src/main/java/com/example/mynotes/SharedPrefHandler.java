package com.example.mynotes;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedPrefHandler {
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    SharedPrefHandler()
    {
        sp = MainActivity.sp;
        editor = sp.edit();

    }
    public HashMap<String, String> getAllStoredNotes() {
        Map<String, String> notes;
        notes = (Map<String, String>)sp.getAll();
    }
}
