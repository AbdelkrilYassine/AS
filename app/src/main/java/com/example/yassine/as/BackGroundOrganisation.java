package com.example.yassine.as;

/**
 * Created by yassine on 08/04/2017.
 */

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


class BackGroundOrganisation extends AsyncTask<String, String, String> {
    Context cx;
    String code;
    String nomorg;
    ProgressDialog progressDialog;

    public BackGroundOrganisation(Context cx) {
        this.cx = cx;
    }

    @Override
    protected String doInBackground(String... params) {


        code = params[0];
        String nomorg = params[1];


        String data = "";
        int tmp;

        try {
            URL url = new URL("http://api2.randon.ili-studios.tn/organisation.php?code=" + code + "&nomorg=" + nomorg);
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
            builder.setTitle(Html.fromHtml("<font color='#FF0000'>Here is your Rando code !"));
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


                            Intent s = new Intent(cx, ListOrganiser.class);
                           // s.putExtra("me", nomorg);
                            cx.startActivity(s);
                        }
                    }

            );


            builder.show();

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