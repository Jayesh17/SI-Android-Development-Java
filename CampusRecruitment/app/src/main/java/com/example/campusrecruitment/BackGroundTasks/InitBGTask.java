package com.example.campusrecruitment.BackGroundTasks;
import android.os.AsyncTask;
import com.example.campusrecruitment.DBManipulation.DBHandler;
import com.example.campusrecruitment.MainActivity;
import com.example.campusrecruitment.MainController;

public class InitBGTask extends AsyncTask<Void,Void,Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        MainController.dbHandler = new DBHandler(MainActivity.context);
        return null;
    }
}
