package com.example.listviewprac;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String,String> LinkMaps = new HashMap<>();
    ArrayList<String> linkStr = new ArrayList<>();

    /*public class BG extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }
    }*/

    public void addLink(String title, String link)
    {
        LinkMaps.put(title,link);
        linkStr.add(title);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button b = findViewById(R.id.linkBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleID = (EditText) findViewById(R.id.linkTitle);
                EditText linkID = (EditText)findViewById(R.id.Link);

                String title = titleID.getText().toString();
                String link = linkID.getText().toString();
                if(title.isEmpty() && link.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Fill both Fields.",Toast.LENGTH_SHORT).show();
                }
                else if(title.isEmpty() && !link.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Fill Title.",Toast.LENGTH_SHORT).show();
                }
                else if(link.isEmpty() && !title.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Fill Link.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addLink(title,link);
                    Toast.makeText(getApplicationContext(),title+" link has been added.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*LinkMaps.put("Khan sir","https://www.youtube.com/channel/UCatL-c6pmnjzEOHSyjn-sHA");
        LinkMaps.put("Triggered Insan","https://www.youtube.com/channel/UCfLuT3JwLx8rvHjHfTymekw");
        LinkMaps.put("Live Insan","https://www.youtube.com/channel/UCFwKgzKe-EdTz83r6wzhmOw");
        LinkMaps.put("Sandeep Maheshvari TV","https://sandeepmaheshwari.tv");
        LinkMaps.put("Sandeep Maheshvar TV","https://sandeepmaheshwari.tv");
        LinkMaps.put("Sandeep Maheshva TV","https://sandeepmaheshwari.tv");
        LinkMaps.put("Sandeep Maheshv TV","https://sandeepmaheshwari.tv");*/
        TextView msg = findViewById(R.id.initMsg);
        if(linkStr.isEmpty())
        {

            msg.setText("No Links Added");
            msg.setVisibility(View.VISIBLE);
        }
        else
        {
            msg.setVisibility(View.GONE);
            ListView links = findViewById(R.id.linkLists);
            ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,linkStr);

            links.setAdapter(adapter);

            if(linkStr.size() >= 1)
            {
                LinkMaps.forEach((key,value)->{
                    linkStr.add(key);
                });

                links.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String title = ((TextView)view).getText().toString();
                        Toast.makeText(getApplicationContext(),"Redirect to "+title,Toast.LENGTH_SHORT).show();
                        String link = LinkMaps.get(title);
                        Uri url = Uri.parse(link);
                        Intent in = new Intent(Intent.ACTION_VIEW,url);
                        startActivity(in);
                    }
                });
            }
        }
    }
}