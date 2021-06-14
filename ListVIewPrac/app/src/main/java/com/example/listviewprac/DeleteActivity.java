package com.example.listviewprac;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listviewprac.DBdata.DBHandler;
import com.example.listviewprac.Model.LinkSet;

public class DeleteActivity extends AppCompatActivity {

    DBHandler myDBHandler;
    LinkSet linksets;

    public void setInitiateState()
    {
        myDBHandler =  MainActivity.getMyDBHandler();
        linksets = MainActivity.getLinksets();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        setInitiateState();

        Button b = findViewById(R.id.deleteBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleID = (EditText) findViewById(R.id.deleteTitle);
                String title = titleID.getText().toString();

                if(title.isEmpty())
                {
                    Toast.makeText(getBaseContext(), "Please Fill Title.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int result = linksets.removeLink(myDBHandler,title);
                    if (result == 1)
                    {
                        Toast.makeText(getBaseContext(), title + " link has been Deleted.", Toast.LENGTH_LONG).show();
                        Intent mainAct = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(mainAct);
                    }
                    else if (result == 0)
                        Toast.makeText(getBaseContext(), "Title does Not Exist.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getBaseContext(), "Link could not be Deleted.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}