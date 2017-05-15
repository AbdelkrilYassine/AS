package com.example.yassine.as;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by yassine on 19/04/2017.
 */

public class DataParserlist extends AsyncTask<Void,Void,Boolean> {
    Context c;
    String jsonData;
    ListView lv;
    ArrayList<Here> spacecrafts=new ArrayList<Here>();
    public DataParserlist(Context c, String jsonData, ListView lv) {
        this.c = c;
        this.jsonData = jsonData;
        this.lv = lv;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseData();
    }
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if(result)
        {
            AdapterHere adapter=new AdapterHere(c,spacecrafts);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(c,spacecrafts.get(position).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private Boolean parseData()
    {
        try
        {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo;
            spacecrafts.clear();
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String name = jo.getString("NomParticipant");
                String present = jo.getString("present");

                if (Integer.parseInt(present)!=1)
                {String text="Absent";
                    int colorRed = c.getResources().getColor(R.color.r);

                    SpannableString spannable = new SpannableString(text);
                    // here we set the color
                    spannable.setSpan(new ForegroundColorSpan(colorRed), 0, text.length(), 0);
                    Here h=new Here(name,text,"hikme");
                    spacecrafts.add(h);}
                else
                {String textt="Present";


                    // here we set the color
                    Here he=new Here(name,textt,"hikme");

                    spacecrafts.add(he);}
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
