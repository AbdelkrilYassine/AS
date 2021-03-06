package com.example.yassine.as;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


/**
 * Created by yassine on 19/04/2017.
 */

public class Downloaderlist extends AsyncTask<Void, Void, String> {
    Context c;
    String urlAddess;
    ListView lv;

    public Downloaderlist(Context c, String urlAddess, ListView lv) {
        this.c = c;
        this.urlAddess = urlAddess;
        this.lv = lv;
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
        if (jsonData.startsWith("Error")) {
            Toast.makeText(c, "Unsuccessful " + jsonData, Toast.LENGTH_SHORT).show();
        } else {
            //PARSE
            new DataParserlist(c, jsonData, lv).execute();
        }
    }

    private String downloadData() {
        Object connection = Connector.connect(urlAddess);
        if (connection.toString().startsWith("Error")) {
            return connection.toString();
        }
        try {
            HttpURLConnection con = (HttpURLConnection) connection;
            InputStream is = new BufferedInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer jsonData = new StringBuffer();
            while ((line = br.readLine()) != null) {
                jsonData.append(line + "n");
            }
            br.close();
            is.close();
            return jsonData.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error " + e.getMessage();
        }
    }
}