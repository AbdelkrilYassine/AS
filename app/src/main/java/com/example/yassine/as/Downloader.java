package com.example.yassine.as;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
/**
 * Created by yassine on 14/04/2017.
 */

public class Downloader extends AsyncTask<Void,Void,String>{
    Context c;
    String urlAddess;
    GoogleMap map;
    public Downloader(Context c, String urlAddess, GoogleMap map) {
        this.c = c;
        this.urlAddess = urlAddess;
        this.map = map;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(Void... params) {
        return this.downloadData();
    }
    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);
        if(jsonData.startsWith("Error"))
        {
            Toast.makeText(c,"Unsuccessful "+jsonData,Toast.LENGTH_SHORT).show();
        }else
        {
            //PARSE
            new DataParser(c,jsonData,map).execute();
        }
    }
    private String downloadData()
    {
        Object connection=Connector.connect(urlAddess);
        if(connection.toString().startsWith("Error"))
        {
            return connection.toString();
        }
        try {
            HttpURLConnection con= (HttpURLConnection) connection;
            InputStream is=new BufferedInputStream(con.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer jsonData=new StringBuffer();
            while ((line=br.readLine()) != null)
            {
                jsonData.append(line+"n");
            }
            br.close();
            is.close();
            return jsonData.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error "+e.getMessage();
        }
    }
}
