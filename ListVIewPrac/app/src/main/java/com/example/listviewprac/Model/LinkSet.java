package com.example.listviewprac.Model;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.example.listviewprac.DBdata.DBHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class LinkSet {

    private HashMap<String,String> LinkMaps = new HashMap<>();
    private ArrayList<String> linkStr = new ArrayList<>();
    public int getCount()
    {
        return linkStr.size();
    }

    public ArrayList<String> getLinkStr() {
        return linkStr;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getFromDB(DBHandler db)
    {
        LinkMaps = db.getAllRecords();
        if(LinkMaps.size()>0)
        {
            LinkMaps.forEach((key,value)->{
                linkStr.add(key);
            });
        }
        else
        {
            Log.d("logs","Population of data failed");
        }
    }
    public int addLink(DBHandler db,String title, String link)
    {
        if(LinkMaps.put(title,link) != null )
        {
            return 0;
        }
        else
        {
            if(db.addRecord(title,link))
            {
                linkStr.add(title);
                return 1;
            }
            else
            {
                return -1;
            }
        }
    }

    public int removeLink(DBHandler db,String title)
    {
        if(!linkStr.contains(title))
        {
            return 0;
        }
        else
        {
            if(db.deleteRecord(title))
            {
                linkStr.remove(title);
                LinkMaps.remove(title);
                return 1;
            }
            else
            {
                return -1;
            }
        }
    }

    public String getLink(String title)
    {
        return LinkMaps.get(title);
    }
}
