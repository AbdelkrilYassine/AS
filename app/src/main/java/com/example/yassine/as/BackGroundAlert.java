package com.example.yassine.as;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Vibrator;
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

/**
 * Created by yassine on 14/04/2017.
 */

class BackGroundAlert extends AsyncTask<String, String, String> {
    Context cx;

    public BackGroundAlert(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


        String code = params[0];



        String data="";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/alert.php?code="+code);
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

       if (s.startsWith("!")) {

            Vibrator v = (Vibrator) cx.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
            MediaPlayer mpp = MediaPlayer.create(cx, R.raw.sounderror);
            mpp.start();

            final AlertDialog.Builder builder = new AlertDialog.Builder(cx);
            builder.setTitle(Html.fromHtml("<font color='#FF0000'>Beware A hiker has left the safe distance !"));
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


            email.setText(s.replace('!',' '));
            builder.setView(email);


            //  builder.setView(fa);
            builder.setMessage("You can notify all hikers to stop walking . " + Newligne);


            builder.setPositiveButton("Notify All Rando",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {


                        }
                    }

            );


            builder.show();


        }


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}