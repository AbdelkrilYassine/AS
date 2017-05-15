package com.example.yassine.as;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yassine on 11/04/2017.
 */

class BackGroundModRando extends AsyncTask<String, String, String> {
    Context cx;
    ProgressDialog progressDialog;
    String code;
    String nom;

    public BackGroundModRando(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


        String code = params[0];
        String date = params[1];
        String lieu = params[2];
        String nb = params[3];
        String ldep = params[4];
        String larr = params[5];
        String hdep = params[6];
        String harr = params[7];
        String duree = params[8];
        String distance = params[9];
        String nom = params[10];
        String tel = params[11];
        String diff = params[12];
        String mode = params[13];
        String data = "";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/ModifierInfoRando.php?code="+code+"&date="+date+"&lieu="+lieu+"&nb="+nb+"&ldep="+ldep+"&larr="+larr+"&hdep="+hdep+"&harr="+harr+"&duree="+duree+"&distance="+distance+"&nom="+nom+"&tel="+tel+"&diff="+diff+"&mode="+mode);
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

        if (s.equals("works") || s.startsWith("works")) {
            Toast.makeText(cx, "Your update operation ended Successfully", Toast.LENGTH_LONG).show();


          /*  Intent BackpressedIntent = new Intent();
            BackpressedIntent.setClass(cx, Connexion.class);
            BackpressedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            BackGroundLogout b = new BackGroundLogout(cx);
            b.execute(nom);

            cx.startActivity(BackpressedIntent);
            ((Activity) cx).finish();*/
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