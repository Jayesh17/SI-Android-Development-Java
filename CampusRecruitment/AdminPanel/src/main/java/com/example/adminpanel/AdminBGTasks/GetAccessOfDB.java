package com.example.adminpanel.AdminBGTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.adminpanel.AdminDBManagement.DatabaseAccess;

public class GetAccessOfDB extends AsyncTask<Void,Void,Void> {

    Context context;

    public GetAccessOfDB(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.createInitialTables();
        return null;
    }
}
