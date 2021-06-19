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
import java.util.Map;
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
    int currentPlayingID;

    public void setIntialState()
    {
        Intent main_intent = getIntent();
        songName = main_intent.getStringExtra(MainActivity.songKey);
        currentPlayingID = main_intent.getIntExtra(MainActivity.songid,1);
        songNameView = findViewById(R.id.name);
        mediaController = MainActivity.mediaController;
        playing = true;
        songSeekBar = findViewById(R.id.songSeekBar);
        mediaPlayer = MainActivity.mediaPlayer;
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
    }

    public void playSong(String song)
    {
        try {
            String path = mediaController.getPathByName(song);
            //Uri uri = Uri.parse(MainActivity.mediaController.getPathByName(songName));
            songNameView.setText(song);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ErrorMedia",e.toString());
        }
        int duration = mediaPlayer.getDuration();
        songSeekBar.setMax(duration);
        mediaPlayer.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        setIntialState();

        playSong(songName);

        customHandler = new Handler();
        customHandler.postDelayed(run,100);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong(new View(getBaseContext()));
            }
        });
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

    public void playPrevSong(View view)
    {
        try {
            String prevSongName="";
            Log.d("id",currentPlayingID+"");
            if(currentPlayingID == 1)
            {
                currentPlayingID = MainActivity.Songs.size();
            }
            else {
                currentPlayingID--;
            }
            for(Map.Entry<String,Integer> en : MainActivity.Songs.entrySet())
            {
                if(currentPlayingID == (int)en.getValue())
                {
                    prevSongName = en.getKey();
                    break;
                }
            }
            Log.d("prevSOngName",prevSongName);
            playSong(prevSongName);
        }
        catch (Exception e)
        {
            Log.d("prevErr",e.toString());
        }
    }
    public void playNextSong(View view)
    {
        try{
            String nextSongName="";
            Log.d("id",currentPlayingID+"");
            if(currentPlayingID == MainActivity.Songs.size())
            {
                currentPlayingID = 1;
            }
            else {
                currentPlayingID++;
            }
            for(Map.Entry<String,Integer> en : MainActivity.Songs.entrySet())
            {
                if(currentPlayingID == (int)en.getValue())
                {
                    nextSongName = en.getKey();
                    break;
                }
            }
            Log.d("nextSongName",nextSongName);
            playSong(nextSongName);
        }
        catch (Exception e)
        {
            Log.d("nextErr",e.toString());
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customHandler.removeCallbacksAndMessages(run);
        finish();
    }
}