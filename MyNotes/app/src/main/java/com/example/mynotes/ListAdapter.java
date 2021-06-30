package com.example.mynotes;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListAdapter extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<HashMap<String,String>> noteLists;

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public ListAdapter(Context context, ArrayList<HashMap<String,String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        noteLists = data;
        inflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = super.getView(position,convertView,parent);
        TextView title = (TextView) v.findViewById(R.id.nTitle);
        ImageView menuBtn = (ImageView)v.findViewById(R.id.menu_Btn);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v, Gravity.END);
                MenuInflater inflater = popup.getMenuInflater();
                String itemtitle = noteLists.get(position).get("title");
                inflater.inflate(R.menu.layout_menu_items, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position,itemtitle));
                popup.show();
            }
        });
        return v;
    }
}
