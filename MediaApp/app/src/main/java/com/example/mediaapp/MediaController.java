package com.example.mediaapp;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class MediaController {

    private Vector<MP3File> audioFiles = new Vector<>();

    public void getAllFilesFromDevice(final Context context) {

        try
        {
            //URl to all the files
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            // Projection to all the attributes
            String[] proj = {MediaStore.Audio.AudioColumns.DATA};

            Cursor c = context.getContentResolver().query(uri,proj,null,null,null);

            if( c != null)
            {
                int count=0;
                while(c.moveToNext())
                {
                    String path = c.getString(0);
                    String name = path.substring(path.lastIndexOf('/')+1);
                    String ext = path.substring(path.lastIndexOf('.')+1);

                    if(ext != "ogg")
                    {
                        Log.d("EXT",ext);
                        MainActivity.Songs.put(name,++count);
                        MP3File file = new MP3File(name,path);
                        audioFiles.add(file);
                    }
                }
                c.close();
            }
            MainActivity.isLoaded = true;
        }
        catch (Exception e)
        {
            Log.d("Exc",e.toString());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> getNames()
    {
        List<String> songNames = new ArrayList<>();

        Iterator<MP3File> it = audioFiles.iterator();
        while(it.hasNext())
        {
            MP3File file = it.next();
            songNames.add(file.getName());
        }
        return songNames;
    }

    public final String getPathByName(String name)
    {
        String path="";
       Iterator<MP3File> it = audioFiles.iterator();
       while(it.hasNext())
       {
           MP3File file = it.next();
           if(name.equals(file.getName()))
           {
               path = file.getPath();
               break;
           }
       }
       return path;
    }
}
