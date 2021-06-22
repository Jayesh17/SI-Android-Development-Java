package com.example.mynotes;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView notesList;
    Button addNoteBtn;
    TextView status;

    public static SharedPreferences sp;
    SharedPrefHandler SPHandler;
    HashMap<String,String> notes;
    List<HashMap<String,String>> arrayList;

    public void setInitialState()
    {
        notesList = findViewById(R.id.TitleLists);
        addNoteBtn = findViewById(R.id.addBtn);
        status = findViewById(R.id.status);

        SPHandler = new SharedPrefHandler();
        notes = new HashMap<>();
        arrayList = new ArrayList<>();

        sp = getSharedPreferences("Notes",MODE_PRIVATE);
    }

    public void showLists()
    {
        /*if(notes.isEmpty())
        {
            status.setVisibility(View.VISIBLE);
        }
        else
        {
            status.setVisibility(View.GONE);
        }*/
        notes = SPHandler.getAllStoredNotes();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialState();
        showLists();
    }
}