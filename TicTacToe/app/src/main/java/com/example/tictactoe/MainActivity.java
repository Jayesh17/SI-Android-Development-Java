package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void boxTap(View view)
    {
        Toast toast=Toast. makeText(this,"Tapped.",Toast. LENGTH_SHORT);
        //toast. setMargin(50,50);
        toast. show();
    }
}