package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewNoteActivity extends AppCompatActivity {

    public static final String noteTitleKey = "com.example.mynotes.ViewNoteActivity.TITLE_KEY";

    TextView itemTitleView;
    TextView itemContentView;
    Button itemEditBtnView;

    Notes notes;
    String itemTitle;
    String content;
    public void setInitialState()
    {
        notes = MainActivity.notes;
        Intent in = getIntent();
        itemTitle = in.getStringExtra(MainActivity.titleKey);
        content = notes.getContentByTitle(itemTitle);

        itemTitleView = findViewById(R.id.noteViewTitle);
        itemContentView = findViewById(R.id.noteContent);
        itemEditBtnView = findViewById(R.id.editBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        setInitialState();

        itemTitleView.setText(itemTitle);
        itemContentView.setText(content);

        itemEditBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EditActivity.class);
                intent.putExtra(noteTitleKey,itemTitle);
                startActivity(intent);
                finish();
            }
        });
    }
}