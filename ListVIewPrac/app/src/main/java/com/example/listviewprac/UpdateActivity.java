package com.example.listviewprac;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.listviewprac.DBdata.DBHandler;
import com.example.listviewprac.Model.LinkSet;

public class UpdateActivity extends AppCompatActivity {

    Spinner ddLinks;
    LinkSet linkSet;

    DBHandler dbHandler;
    ArrayAdapter<String> adapter;

    public void setInitialState()
    {
        ddLinks = (Spinner)findViewById(R.id.ddLinks);
        linkSet = MainActivity.getLinksets();
        dbHandler=MainActivity.getMyDBHandler();

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,linkSet.getLinkStr());
        ddLinks.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setInitialState();
    }

    public void changeName(View view)
    {
        String title = ddLinks.getSelectedItem().toString();
        EditText nameView = new EditText(view.getContext());
        AlertDialog.Builder renameDialog  = new AlertDialog.Builder(view.getContext());
        renameDialog.setTitle("Rename Title");
        renameDialog.setMessage("Enter new Name for Title: "+title);
        renameDialog.setView(nameView);

        renameDialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name =nameView.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(UpdateActivity.this,"Name field cannot be empty in dialog Box.",Toast.LENGTH_LONG).show();
                }
                else {
                    if(linkSet.updateTitle(dbHandler,title,name))
                    {
                        adapter.notifyDataSetChanged();
                        Toast.makeText(UpdateActivity.this,"Title updated Successfully.",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(UpdateActivity.this,"Something went wrong, please try again later.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        renameDialog.create().show();
    }

    public void changeLink(View view)
    {
        String title = ddLinks.getSelectedItem().toString();
        EditText linkView = new EditText(view.getContext());
        AlertDialog.Builder resetDialog  = new AlertDialog.Builder(view.getContext());
        resetDialog.setTitle("Edit Link");
        resetDialog.setMessage("Update Link for Title: "+title);
        resetDialog.setView(linkView);
        resetDialog.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link =linkView.getText().toString();
                if(link.isEmpty())
                {
                    Toast.makeText(UpdateActivity.this,"Link field cannot be empty in dialog Box.",Toast.LENGTH_LONG).show();
                }
                else {
                    if(linkSet.updateLink(dbHandler,title,link))
                    {
                        Toast.makeText(UpdateActivity.this,"Link updated Successfully.",Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(UpdateActivity.this,"Something went wrong, please try again later.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        resetDialog.create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}