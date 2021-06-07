package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // X - 0
    // O - 1
    private int playerTurn;
    private int state[] = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
    private int winPos[][] = {
            {0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}
    };
    int colRemaining;
    int winO = 0;
    int winX = 0;
    int priority=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialState();
    }

    public void restart(View view)
    {
        for(int i=0;i<9;i++)
        {
            state[i]=-1;
        }
        ((ImageView)findViewById(R.id.C00)).setImageResource(0);
        ((ImageView)findViewById(R.id.C01)).setImageResource(0);
        ((ImageView)findViewById(R.id.C02)).setImageResource(0);
        ((ImageView)findViewById(R.id.C10)).setImageResource(0);
        ((ImageView)findViewById(R.id.C11)).setImageResource(0);
        ((ImageView)findViewById(R.id.C12)).setImageResource(0);
        ((ImageView)findViewById(R.id.C20)).setImageResource(0);
        ((ImageView)findViewById(R.id.C21)).setImageResource(0);
        ((ImageView)findViewById(R.id.C22)).setImageResource(0);
        playerTurn = 0;
        ((ImageView)findViewById(R.id.C00)).setClickable(true);
        ((ImageView)findViewById(R.id.C01)).setClickable(true);
        ((ImageView)findViewById(R.id.C02)).setClickable(true);
        ((ImageView)findViewById(R.id.C11)).setClickable(true);
        ((ImageView)findViewById(R.id.C10)).setClickable(true);
        ((ImageView)findViewById(R.id.C12)).setClickable(true);
        ((ImageView)findViewById(R.id.C21)).setClickable(true);
        ((ImageView)findViewById(R.id.C22)).setClickable(true);
        ((ImageView)findViewById(R.id.C20)).setClickable(true);
        setInitialState();
    }
    public void disableAll()
    {
        ((ImageView)findViewById(R.id.C00)).setClickable(false);
        ((ImageView)findViewById(R.id.C01)).setClickable(false);
        ((ImageView)findViewById(R.id.C02)).setClickable(false);
        ((ImageView)findViewById(R.id.C11)).setClickable(false);
        ((ImageView)findViewById(R.id.C10)).setClickable(false);
        ((ImageView)findViewById(R.id.C12)).setClickable(false);
        ((ImageView)findViewById(R.id.C21)).setClickable(false);
        ((ImageView)findViewById(R.id.C22)).setClickable(false);
        ((ImageView)findViewById(R.id.C20)).setClickable(false);
    }
    public void boxTap(View view)
    {
        ImageView box = (ImageView)view;
        String colstr = box.getTag().toString();
        int boxNum = Integer.parseInt(colstr);
        if(state[boxNum] == -1)
        {
            //box.setTranslationY(-2000f);
            state[boxNum] = playerTurn;
            if(playerTurn == 0)//x's turn
            {
                System.out.println(boxNum+"tapped");
                box.setImageResource(R.drawable.x);
                playerTurn = 1;
                TextView t = (TextView)findViewById(R.id.StatusAns);
                t.setText("O's Turn");
            }
            else if(playerTurn == 1)
            {
                System.out.println(boxNum+"tapped");
                box.setImageResource(R.drawable.o);
                playerTurn = 0;
                TextView t = (TextView)findViewById(R.id.StatusAns);
                t.setText("X's Turn");
            }
            //box.animate().translationYBy(1000f).setDuration(400);
            colRemaining--;
        }
        else
        {
            Toast toast=Toast. makeText(this,"Already Selected.",Toast. LENGTH_SHORT);
            toast. show();
        }

        //Decide winner
        int win=0;
        for(int winner[] : winPos)
        {
            if((state[winner[0]] == state[winner[1]]) && (state[winner[1]] == state[winner[2]]) && state[winner[0]] != -1)
            {
                win = 1;
                String won;
                if(state[winner[0]] == 0)//X won
                {
                    won = "X has Won.";
                    winX++;
                    priority =0;
                    TextView t = (TextView)findViewById(R.id.Xscore);
                    t.setText(Integer.toString(winX));
                }
                else//O won
                {
                    won = "O has Won.";
                    winO++;
                    priority = 1;
                    TextView t = (TextView)findViewById(R.id.Oscore);
                    t.setText(Integer.toString(winO));
                }
                TextView t = (TextView)findViewById(R.id.StatusAns);
                t.setText(won);
                t.setTextColor(Color.parseColor("#136116"));
                Button b = (Button)findViewById(R.id.restartBtn);
                b.setVisibility(View.VISIBLE);
                b.setEnabled(true);
                disableAll();
            }
        }
        if(win == 0 && colRemaining == 0)
        {
            TextView t = (TextView)findViewById(R.id.StatusAns);
            t.setText("Match Draw.");
            Button b = (Button)findViewById(R.id.restartBtn);
            b.setVisibility(View.VISIBLE);
            b.setEnabled(true);
            disableAll();
        }
    }
    public void setInitialState()
    {
        colRemaining = 9;
        TextView t = (TextView)findViewById(R.id.StatusAns);
        t.setTextColor(Color.parseColor("#E53935"));
        if(priority == 0)
        {
            t.setText("X's Turn");
            playerTurn = 0;
        }
        else
        {
            t.setText("O's Turn");
            playerTurn = 1;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); //RatateLock
        Button b = (Button)findViewById(R.id.restartBtn);
        b.setVisibility(View.GONE); // invisible
        b.setEnabled(false); //Disable
    }
}