package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    EditText noteTitleView;
    EditText noteContentView;
    Button addNoteBtnView;

    private String noteTitle;
    private String noteContent;
    SharedPrefHandler SPHandler;

    public void setInitialState()
    {
        noteTitleView = findViewById(R.id.titleField);
        noteContentView = findViewById(R.id.noteText);
        addNoteBtnView = (Button) findViewById(R.id.newNoteBtn);
        SPHandler = MainActivity.SPHandler;
    }
    public boolean checkFields()
    {
        if(noteTitle.isEmpty())
        {
            noteTitleView.setError("Title is Required");
            return false;
        }
        if(noteContent.isEmpty())
        {
            Toast.makeText(getBaseContext(),"Please enter Content of your note.",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public void addNoteAndNotify()
    {
        boolean isChecked;
        noteTitle = noteTitleView.getText().toString();
        noteContent = noteContentView.getText().toString();
        isChecked = checkFields();
        if(isChecked)
        {
            int status;
            status= SPHandler.addNewNote(noteTitle,noteContent);
            if(status == -1)
            {
                Toast.makeText(getBaseContext(),"Title Already exist, try with another one.", Toast.LENGTH_LONG).show();
            }
            else if(status == 0)
            {
                Toast.makeText(getBaseContext(),"Something Went Wrong.", Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(getBaseContext(),"Note Added Successfully.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setInitialState();
        addNoteBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNoteAndNotify();
            }
        });
    }

}