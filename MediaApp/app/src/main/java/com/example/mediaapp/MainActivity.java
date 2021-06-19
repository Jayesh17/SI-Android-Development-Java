package com.example.mediaapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public int STORAGE_PERMISSION_CODE = 1;
    public static Context context;
    public static boolean isLoaded;
    public static MediaController mediaController;
    public static MediaPlayer mediaPlayer;
    TextView status;
    Handler customHandler;
    ListView songList;
    public static String songKey;
    public static String songid;

    public static HashMap<String,Integer> Songs;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setIntialState();
            } else {
                status = findViewById(R.id.status);
                status.setVisibility(View.VISIBLE);
                status.setText("Permission Denied");
                Toast.makeText(this, "If you want to allow Permission, just Reopen the Application", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this).setTitle("Storage permission needed")
                    .setMessage("Storage permission is needed to read Music files from your device.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //hasPermission = false;
                    dialog.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }


    public void checkStoragePermissionAndSetInitialState() {
        //check if permission is alreay granted to us or not.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            setIntialState();
        } else {
            getStoragePermission();
        }
    }

    public void setIntialState() {
        context = getApplicationContext();
        isLoaded = false;
        Songs = new HashMap<>();
        songList = findViewById(R.id.list);
        mediaController = new MediaController();
        mediaPlayer = new MediaPlayer();
        BackGroundTask BGTask = new BackGroundTask();
        BGTask.execute();
        status = findViewById(R.id.status);
        status.setVisibility(View.VISIBLE);
        songKey = getPackageName()+"songKey";
        songid = getPackageName()+"songID";
        customHandler = new Handler();
        customHandler.postDelayed(checkIfDone, 0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkStoragePermissionAndSetInitialState();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getListView() {
        List<String> songs = mediaController.getNames();
        ArrayAdapter<String> songsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songs);
        songList.setAdapter(songsAdapter);

            songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String songName = ((TextView)view).getText().toString();
                    Intent playSong = new Intent(getBaseContext(),PlaySongActivity.class);
                    playSong.putExtra(songKey,songName);
                    int songID = Songs.get(songName);
                    playSong.putExtra(songid,songID);
                       startActivity(playSong);
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }

    private Runnable checkIfDone = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            if (isLoaded == false) {
                customHandler.postDelayed(this, 10);
            } else {
                status.setVisibility(View.GONE);
                getListView();
            }
        }
    };

}