package com.example.watchnews;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class BGTask extends AsyncTask<Void,Void,Void> {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Void doInBackground(Void... voids) {

        MainController mainController = MainActivity.mainController;
        mainController.populateData();
        return null;
    }
}
