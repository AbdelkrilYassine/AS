package com.example.yassine.as;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
 * Created by yassine on 08/04/2017.
 */

class BackGroundVerifOrg extends AsyncTask<String, String, String> {
    Context cx;
    String code;
    String nomorg;
    ProgressDialog progressDialog;

    public BackGroundVerifOrg(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


         nomorg = params[0];


        String data = "";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/verifOrg.php?nomorg="+nomorg);
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

        if (s.equals("You dont have any current randon on time")) {
            Toast.makeText(cx, "You can create your own rando! ", Toast.LENGTH_LONG).show();

        }

        else if(s.equals("You never created rando and u never participated on rando"))
            {
                Toast.makeText(cx, "Take a step to Create your first rando right now!", Toast.LENGTH_LONG).show();


            }
            else if(s.equals("You have a rando that participated en cours!"))
        {
            Toast.makeText(cx, "You already participated on rando! make sure you finish it so u may create a rando", Toast.LENGTH_LONG).show();

        }
        else if(s.startsWith("!"))
        {
            Toast.makeText(cx, "Welcome Organizer", Toast.LENGTH_LONG).show();
            String x=s.substring(1, s.length());
            Toast.makeText(cx, x, Toast.LENGTH_LONG).show();

            Intent ll = new Intent(cx, ListOrganiser.class);
            ll.putExtra("me", nomorg);
            cx.startActivity(ll);

        }
        else if(s.equals("Today rando"))
        {
            Toast.makeText(cx, "Today is the big day,Have a good rando user!", Toast.LENGTH_LONG).show();

            /*
            Intent ha = new Intent(cx, ListOrganiser.class);
            ha.putExtra("me", nomorg);
            cx.startActivity(ha);*/

        }
        else{Toast.makeText(cx, s, Toast.LENGTH_LONG).show();}
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}