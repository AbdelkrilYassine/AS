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
 * Created by yassine on 10/04/2017.
 */

class BackGroundAnnulerRando extends AsyncTask<String, String, String> {
    Context cx;
    ProgressDialog progressDialog;
    String code,nom;

    public BackGroundAnnulerRando(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


        code = params[0];
        nom=params[1];


        String data="";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/AnnulerRandonner.php?code="+code);
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
        progressDialog.dismiss();

        if(s.equals("works")||s.startsWith("works")){
            Toast.makeText(cx,"Your rando is canceled Successfully", Toast.LENGTH_LONG).show();

            Intent BackpressedIntent = new Intent();
            BackpressedIntent.setClass(cx, Connexion.class);
            BackpressedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            BackGroundLogout b = new BackGroundLogout(cx);
            b.execute(nom);

            cx.startActivity(BackpressedIntent);
            ((Activity) cx).finish();

        }
        else{                Toast.makeText(cx,s, Toast.LENGTH_LONG).show();
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