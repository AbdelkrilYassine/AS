package com.example.yassine.as;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
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

/**
 * Created by yassine on 29/04/2017.
 */

class BackGroundVerifAppel extends AsyncTask<String, String, String> {
    Context cx;
    ProgressDialog progressDialog;
    String nom, code;




    public BackGroundVerifAppel(Context cx) {
        this.cx = cx;

    }


    @Override
    protected String doInBackground(String... params) {


        nom = params[0];
        code = params[1];


        String data = "";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/verifhiker.php?code=" + code+"&nom="+nom);
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

        if (s.equals("works") || s.startsWith("works")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(cx);
            builder.setTitle(Html.fromHtml("<font color='#FF0000'>Please valid your presence!"));
            builder.setIcon(R.drawable.code);
            String Newligne = System.getProperty("line.separator");

            final TextView email = new TextView(cx);
            email.setHeight(100);
            email.setWidth(100);
            email.setGravity(Gravity.CENTER);
            email.setTextColor(cx.getResources().getColor(android.R.color.black));
            email.setTextSize(20);
            email.setImeOptions(EditorInfo.IME_ACTION_DONE);


            email.setText("Hello " + nom);
            builder.setView(email);


            //  builder.setView(fa);
            builder.setMessage("Click on the button 'Here' to make sure that you are present  " + Newligne);


            builder.setPositiveButton("Here",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            BackGroundPartHere herep = new BackGroundPartHere(cx);
                            herep.execute(nom, code);

                        }
                    }

            );


            builder.show();
        } else if (s.equals("stillnot") || s.startsWith("stillnot")) {
            Toast.makeText(cx, "Please wait until the organizer start the call before you can go to the MapDyn Activity!", Toast.LENGTH_LONG).show();

        } else if(s.equals("here")||s.startsWith("here")) {
            Intent ha = new Intent(cx, Map_offline.class);
            ha.putExtra("me", nom);
            cx.startActivity(ha);}
        else if(s.equals("done")||s.startsWith("done"))
        {            Toast.makeText(cx, "Please wait until the organizer valid the participation list before you can go to the MapDyn Activity!", Toast.LENGTH_LONG).show();
        }
        }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}