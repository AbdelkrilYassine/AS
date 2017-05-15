package com.example.yassine.as;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by yassine on 07/04/2017.
 */

class BackGroundRando extends AsyncTask<String, String, String> {
    Context cx;
    ProgressDialog progressDialog;
    String code;
    String nameme;


    public BackGroundRando(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


         code = params[0];
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
        String origin = params[14];
        String dest = params[15];
       nameme = params[16];



        String data = "";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/rando.php?code="+code+"&date="+date+"&lieu="+lieu+"&nb="+nb+"&ldep="+ldep+"&larr="+larr+"&hdep="+hdep+"&harr="+harr+"&duree="+duree+"&distance="+distance+"&nom="+nom+"&tel="+tel+"&diff="+diff+"&mode="+mode+"&origin="+origin+"&dest="+dest+"&nompart="+nameme);
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

        if (s.equals("works")) {
            Toast.makeText(cx, "ur name has been add to the organization list!", Toast.LENGTH_LONG).show();


            final AlertDialog.Builder builder = new AlertDialog.Builder(cx);
            builder.setTitle(Html.fromHtml("<font color='#FF0000'>Here is your unique Rando code !"));
            builder.setIcon(R.drawable.code);
            String Newligne = System.getProperty("line.separator");

            final TextView email = new TextView(cx);
            email.setHeight(100);
            email.setWidth(100);
            email.setGravity(Gravity.LEFT);
            email.setTextColor(cx.getResources().getColor(android.R.color.black));
            email.setTextSize(20);
            email.setImeOptions(EditorInfo.IME_ACTION_DONE);
            Date d = new Date();


            email.setText(code);
            builder.setView(email);


            //  builder.setView(fa);
            builder.setMessage("Share this code with your friends ,if you want them to participate on your rando! " + Newligne);


            builder.setPositiveButton("Alright",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {


                            Intent BackpressedIntent = new Intent();
                            BackpressedIntent.setClass(cx, Connexion.class);
                            BackpressedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            BackGroundLogout b = new BackGroundLogout(cx);
                            b.execute(nameme);

                            cx.startActivity(BackpressedIntent);
                            ((Activity) cx).finish();
                        }
                    }

            );
           builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent BackpressedIntent = new Intent();
                    BackpressedIntent.setClass(cx, Connexion.class);
                    BackpressedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    BackGroundLogout b = new BackGroundLogout(cx);
                    b.execute(nameme);

                    cx.startActivity(BackpressedIntent);
                    ((Activity) cx).finish();
                }
            });


            builder.show();


        } else {
            Toast.makeText(cx, s, Toast.LENGTH_LONG).show();
        }
    }


        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            progressDialog = new ProgressDialog(cx);
            progressDialog.setMessage("Creating...");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }