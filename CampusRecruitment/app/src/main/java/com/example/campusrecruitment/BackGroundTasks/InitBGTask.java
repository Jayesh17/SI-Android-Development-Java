package com.example.campusrecruitment.BackGroundTasks;
import android.os.AsyncTask;
import com.example.campusrecruitment.DBManipulation.DBHandler;
import com.example.campusrecruitment.MainActivity;

public class InitBGTask extends AsyncTask<Void,Void,Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        MainActivity.dbHandler = new DBHandler(MainActivity.context);
        return null;
    }
}
