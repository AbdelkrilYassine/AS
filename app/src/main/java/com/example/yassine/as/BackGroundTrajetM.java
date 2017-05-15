package com.example.yassine.as;

/**
 * Created by yassine on 07/04/2017.
 */

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
import java.util.ArrayList;
import java.util.List;

class BackGroundTrajetM extends AsyncTask<ArrayList<String>,ArrayList<String>, String> {
    Context cx;
    ProgressDialog progressDialog;

    public BackGroundTrajetM(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(ArrayList<String>... params) {


        ArrayList<String> result = new ArrayList<String>();
        String code = result.get(0);

        String data = "";
        int tmp;

        for (int i = 1; i < result.size(); i++) {
            try {

                URL url = new URL("http://api2.randon.ili-studios.tn/Trajetm.php?code=" + code + "&place=" + result.get(i));
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
        return data;
        }



    @Override
    protected void onPostExecute(String s) {
        progressDialog.dismiss();

        if (s.equals("works")) {
            Toast.makeText(cx, "Data Mountain MAP saved successfully!", Toast.LENGTH_LONG).show();
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
