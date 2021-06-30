package com.example.mynotes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView notesList;
    Button addNoteBtn;
    public static TextView status;
    public static final String titleKey = "com.example.mynotes.MainActivity.TITLE_KEY";

    SharedPreferences sp;
    public static SharedPrefHandler SPHandler;
    public static Notes notes;
    public static Context context;
    public static ListAdapter adapter;
    String from[] = {"title"};
    int to[] = {R.id.nTitle};

    public void setInitialState()
    {
        context = getBaseContext();
        notesList = findViewById(R.id.TitleLists);
        addNoteBtn = findViewById(R.id.addBtn);
        status = findViewById(R.id.status);

        sp = getSharedPreferences("Notes",MODE_PRIVATE);
        notes = new Notes();
        SPHandler = new SharedPrefHandler(sp);
    }

    public void showLists()
    {
        if(notes.getCount()==0)
        {
            status.setVisibility(View.VISIBLE);
        }
        else {
            status.setVisibility(View.INVISIBLE);
            adapter = new ListAdapter(this,notes.getTitleArrayList(),R.layout.activity_view_item,from,to);
            notesList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialState();
        showLists();

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewNoteIntent = new Intent(context,AddNoteActivity.class);
                startActivity(addNewNoteIntent);
            }
        });

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView titleView = (TextView) view.findViewById(R.id.nTitle);
                String itemTitle = titleView.getText().toString();
                Intent addViewNoteIntent = new Intent(context,ViewNoteActivity.class);
                addViewNoteIntent.putExtra(titleKey,itemTitle);
                startActivity(addViewNoteIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showLists();
        Log.d("resume","resume");
    }
}