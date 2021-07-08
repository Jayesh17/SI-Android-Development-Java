package com.example.adminpanel.AdminBGTasks;

import android.os.AsyncTask;

import com.example.adminpanel.AdminDBManagement.AdminDBManager;

public class InitializeDB extends AsyncTask<Void,Void,Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        AdminDBManager.mergeDB();
        return null;
    }
}
