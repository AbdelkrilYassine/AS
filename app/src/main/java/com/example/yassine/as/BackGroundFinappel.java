package com.example.yassine.as;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by yassine on 23/04/2017.
 */

class BackGroundFinappel extends AsyncTask<String, String, String> {
    Context cx;
    ProgressDialog progressDialog;
    String name,code;

    public BackGroundFinappel(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


        code = params[0];
        name = params[1];


        String data = "";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/finappel.php?code=" +code+"&nom="+name);
            String urlParams = "";

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(urlParams.getBytes());
            os.flush();
            os.close();

            InputStream is = httpURLConnection.getInputStream();
            while ((tmp = is.read()) != -1) {
                data += (char) tmp;
            }

            is.close();
            httpURLConnection.disconnect();

            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        progressDialog.dismiss();

        if (s.contentEquals("works!")|| s.startsWith("works!")) {

            Toast.makeText(cx, "Rando has started ! Have fun", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(cx, s, Toast.LENGTH_LONG).show();

        }


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(cx);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}