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

public class AddActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add);
        setInitiateState();

        Button b = findViewById(R.id.linkBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleID = (EditText) findViewById(R.id.linkTitle);
                EditText linkID = (EditText) findViewById(R.id.link);

                String title = titleID.getText().toString();
                String link = linkID.getText().toString();
                if (title.isEmpty() && link.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill both Fields.", Toast.LENGTH_SHORT).show();
                } else if (title.isEmpty() && !link.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill Title.", Toast.LENGTH_SHORT).show();
                } else if (link.isEmpty() && !title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Fill Link.", Toast.LENGTH_SHORT).show();
                } else {
                    int result = linksets.addLink(myDBHandler, title, link);
                    if (result == 1)
                        Toast.makeText(getApplicationContext(), title + " link has been added.", Toast.LENGTH_SHORT).show();
                    else if (result == 0)
                        Toast.makeText(getApplicationContext(), "Title is already exist, Try with another Title.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "Link could not be added.", Toast.LENGTH_SHORT).show();
                }
                Intent mainAct = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainAct);
            }
        });
    }


}