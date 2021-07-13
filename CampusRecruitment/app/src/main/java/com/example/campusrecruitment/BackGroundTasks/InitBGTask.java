package com.example.campusrecruitment.BackGroundTasks;
import android.os.AsyncTask;

import com.example.campusrecruitment.DBManipulation.DatabaseAccess;

public class InitBGTask extends AsyncTask<DatabaseAccess,Void,Void> {

    @Override
    protected Void doInBackground(DatabaseAccess... databaseAccesses) {
        databaseAccesses[0].createInitialTables();
        return null;
    }
}
