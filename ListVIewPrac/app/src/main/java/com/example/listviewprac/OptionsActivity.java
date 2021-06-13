package com.example.listviewprac;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Button add = findViewById(R.id.addLinkBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_intent = new Intent(getApplicationContext(),AddActivity.class);
                startActivity(add_intent);
            }
        });

        Button update = findViewById(R.id.updateLinkBtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update_intent = new Intent(getApplicationContext(),UpdateActivity.class);
                startActivity(update_intent);
            }
        });

        Button delete = findViewById(R.id.deleteLinkBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent delete_intent = new Intent(getApplicationContext(),DeleteActivity.class);
                startActivity(delete_intent);
            }
        });
    }
}