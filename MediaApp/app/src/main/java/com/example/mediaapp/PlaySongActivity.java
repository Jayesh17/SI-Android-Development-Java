package com.example.mediaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlaySongActivity extends AppCompatActivity {

    private String songName;
    TextView songNameView;
    SeekBar songSeekBar;
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    Handler customHandler;
    boolean playing;

    public void setIntialState()
    {
        Intent main_intent = getIntent();
        songName = main_intent.getStringExtra(MainActivity.songKey);
        songNameView = findViewById(R.id.name);
        songNameView.setText(songName);
        mediaController = MainActivity.mediaController;
        playing = true;
        songSeekBar = findViewById(R.id.songSeekBar);
        mediaPlayer = MainActivity.mediaPlayer;
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        setIntialState();

        try {
            String path = mediaController.getPathByName(songName);
            //Uri uri = Uri.parse(MainActivity.mediaController.getPathByName(songName));
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ErrorMedia",e.toString());
        }
        int duration = mediaPlayer.getDuration();
        songSeekBar.setMax(duration);
        mediaPlayer.start();

        customHandler = new Handler();
        customHandler.postDelayed(run,100);

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) // it indicates that seekTO will only be called when user seeks.
                    mediaPlayer.seekTo(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void seekUpdation() {
        songSeekBar.setProgress(mediaPlayer.getCurrentPosition());
        songSeekBar.postDelayed(run, 100);
    }

    Runnable run = new Runnable() { @Override public void run() { seekUpdation(); } };

    public void PPBtnClick(View v)
    {
        Log.d("inClick","Clicked : "+playing);
        ImageView icon = findViewById(R.id.PausePlay);
        if(playing)
        {
            playing = false;
            mediaPlayer.pause();
            icon.setImageResource(R.drawable.play);
        }
        else
        {
            playing = true;
            mediaPlayer.start();
            icon.setImageResource(R.drawable.pause);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customHandler.removeCallbacksAndMessages(run);
    }
}