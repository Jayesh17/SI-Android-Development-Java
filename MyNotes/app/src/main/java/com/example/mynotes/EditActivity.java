package com.example.mynotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    EditText editNoteTitleView;
    EditText editNoteContentView;
    Button itemEditNoteBtnView;

    String title;
    String content;
    Notes notes;
    SharedPrefHandler sp;

    boolean isTitleChanged;
    boolean isContentChanged;
    public void setInitialState()
    {
        editNoteContentView = findViewById(R.id.editNoteText);
        editNoteTitleView = findViewById(R.id.editTitleField);
        itemEditNoteBtnView = findViewById(R.id.editNoteBtn);

        Intent it = getIntent();
        title = it.getStringExtra(ViewNoteActivity.noteTitleKey);
        notes = MainActivity.notes;
        sp = MainActivity.SPHandler;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setInitialState();

        editNoteTitleView.setText(title);
        content = notes.getContentByTitle(title);
        editNoteContentView.setText(content);


        itemEditNoteBtnView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String changedTitle = editNoteTitleView.getText().toString();
                String changedContent = editNoteContentView.getText().toString();
                Log.d("changeTitle",editNoteTitleView.getText().toString()+" "+title);
                Log.d("changeCon",editNoteContentView.getText().toString()+" "+content);

                if(content.equals(changedContent))
                {
                    Toast.makeText(getBaseContext(),"Content is same as before changing of this Note.",Toast.LENGTH_LONG).show();
                }
                else {
                    sp.editNote(title,changedTitle,changedContent);
                    Toast.makeText(getBaseContext(),"Changed !",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}