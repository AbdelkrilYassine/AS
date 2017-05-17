package com.example.yassine.as;

/**
 * Created by yassine on 06/04/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class BackGroundLogout extends AsyncTask<String, String, String> {
    Context cx;
    ProgressDialog progressDialog;
    String name,password;

    public BackGroundLogout(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


        name = params[0];
        String data = "";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/logout.php?username=" + name);
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

        if (s.contentEquals("Disconnected!")) {

            Intent intent = new Intent(cx, Connexion.class);
            cx.startActivity(intent);
         /*   SharedPreferences preferences = cx.getSharedPreferences("loginPrefs",Context.MODE_PRIVATE);
            preferences.edit().remove("key1").commit();
            preferences.edit().remove("key2").commit(); */

            Toast.makeText(cx, "Disconnected!", Toast.LENGTH_LONG).show();


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