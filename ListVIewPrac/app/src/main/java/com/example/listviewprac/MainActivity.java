package com.example.listviewprac;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.listviewprac.DBdata.DBHandler;
import com.example.listviewprac.Model.LinkSet;
public class MainActivity extends AppCompatActivity {

    private static DBHandler myDBHandler;
    private static LinkSet linksets;

    public void manageBtn(View view)
    {
        Intent ints = new Intent(this,OptionsActivity.class);
        startActivity(ints);
    }

    public static DBHandler getMyDBHandler() {
        return myDBHandler;
    }

    public static LinkSet getLinksets() {
        return linksets;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setIntialState()
    {
        myDBHandler = new DBHandler(MainActivity.this);
        linksets = new LinkSet();
        linksets.getFromDB(myDBHandler);
        showAllLinks();
    }

    public void showAllLinks()
    {
        TextView msg = findViewById(R.id.initMsg);
        if(linksets.getCount() == 0)
        {
            msg.setText("No Links Added");
            msg.setVisibility(View.VISIBLE);
        }
        else
        {
            msg.setVisibility(View.GONE);
            ListView links = findViewById(R.id.linkLists);
            ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,linksets.getLinkStr());
            links.setAdapter(adapter);

            links.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String title = ((TextView)view).getText().toString();
                    String link = linksets.getLink(title);//LinkMaps.get(title);
                    try {
                        Uri url = Uri.parse(link);
                        Intent in = new Intent(Intent.ACTION_VIEW,url);
                        startActivity(in);
                        Toast.makeText(getApplicationContext(),"Redirect to "+title,Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"Registered Link ' "+title+"' is Invalid or Can't be reached.",Toast.LENGTH_LONG).show();
                        Log.d("logs",e.toString());
                    }
                }
            });

            links.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("logs","inside LongClick");
                    try {
                        String t =((TextView)view).getText().toString();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("link", t);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getBaseContext(),t+" Copied to Clipboard",Toast.LENGTH_LONG).show();
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //to finish all the running activities and shut down the app.
        ActivityCompat.finishAffinity(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setIntialState();
    }
}