package com.example.yassine.as;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yassine on 14/04/2017.
 */
class BackGroundMapDyn extends AsyncTask<String, String, String> {
    Context cx;


    public BackGroundMapDyn(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


        String nompart = params[0];
        String code = params[1];
        String emp = params[2];
        String distance = params[3];


        String data="";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/mapdyn.php?nom="+nompart+"&code="+code+"&emp="+emp+"&distance="+distance);
            String urlParams = "";

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(urlParams.getBytes());
            os.flush();
            os.close();
            InputStream is = httpURLConnection.getInputStream();
            while((tmp=is.read())!=-1){
                data+= (char)tmp;
            }
            is.close();
            httpURLConnection.disconnect();

            return data;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception: "+e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {

        if (s.equals("works") || s.startsWith("works")) {}
        else
            {     //       Toast.makeText(cx, s, Toast.LENGTH_LONG).show();
            }

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}