package com.example.mediaapp;

import android.os.AsyncTask;

import static com.example.mediaapp.MainActivity.mediaController;

public class BackGroundTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        mediaController.getAllFilesFromDevice(MainActivity.context);
        return null;
    }
}
