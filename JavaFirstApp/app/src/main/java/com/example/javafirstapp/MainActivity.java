package com.example.javafirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    //Object for containing view.
    private TextView t1;
    private TextView t2;
    private Button b1;
    private Button b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the views and register them with their components.
        t1 = (TextView)findViewById(R.id.First);
        t2 = (TextView)findViewById(R.id.Second);
        b1 = (Button) findViewById(R.id.ChangeFirst);
        b2 = (Button) findViewById(R.id.ChangeSecond);

//        b1.setOnClickListener(this);
//        b2.setOnClickListener(this);
    }

    //Handler for view
//    @Override // implement onclickListener interface
//    public void onClick(View v) {
//        t1.setText("You have changed the Text..");
//    }

    public void clickedFirst(View v)
    {
        String b = (String)b1.getText();
        String info = (String)t1.getText();
        if(b.equals("Change First"))
        {
            t1.setText("changed text 1");
            b1.setText("Previous");
        }
        else
        {
            t1.setText("Text Here Anything");
            b1.setText("Change First");
        }
    }
    public void clickedSecond(View v)
    {
        String b = (String)b2.getText();
        String info = (String)t2.getText();
        if(b.equals("Change Second"))
        {
            t2.setText("changed text 2");
            b2.setText("Previous");
        }
        else
        {
            t2.setText("Text Here Anything");
            b2.setText("Change Second");
        }
    }
}