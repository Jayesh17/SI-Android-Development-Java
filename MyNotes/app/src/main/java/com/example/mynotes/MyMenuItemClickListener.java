package com.example.mynotes;

import android.view.MenuItem;
import android.widget.PopupMenu;

public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

    int pos;
    String itemTitle;
    SharedPrefHandler sp;

    public MyMenuItemClickListener(int pos,String title)
    {
        this.pos = pos;
        itemTitle = title;
        sp = MainActivity.SPHandler;
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        sp.deleteNote(itemTitle);
        return true;
    }
}
