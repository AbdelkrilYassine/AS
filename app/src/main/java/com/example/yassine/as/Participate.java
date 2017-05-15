package com.example.yassine.as;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Participate extends AppCompatActivity implements View.OnClickListener {
    EditText codee;
    Button valide, listparr;
    String usernames;
    String cud;
    String codehim;
    Handler h = new Handler();
    int delay = 5000;
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);

        codee = (EditText) findViewById(R.id.code);
        Intent intent = getIntent();

        Bundle b = intent.getExtras();

        usernames = b.getString("usernametest");
        String passwords = b.getString("passwordtest");
        this.setTitle("Welcome " + usernames);
        valide = (Button) findViewById(R.id.validd);
        listparr = (Button) findViewById(R.id.listpartr);
        valide.setOnClickListener(this);
        listparr.setEnabled(false);
        listparr.setVisibility(View.GONE);
        listparr.setOnClickListener(this);
        BackGroundRandoPart tt=new BackGroundRandoPart();
        tt.execute(usernames);
        BackGroundVerifPart o = new BackGroundVerifPart();
        o.execute(usernames);


    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validd:
                cud = codee.getText().toString();
                if (cud.isEmpty()) {

                    Drawable dr = getResources().getDrawable(R.drawable.wrongcode);
                    dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());


                    codee.setError("Enter a valid Rando code", dr);

                    codee.setCompoundDrawables(null, null, dr, null);
                } else {
                    codee.setError(null, null);


                    codee.setCompoundDrawables(null, null, null, null);

                    BackGroundParticipate r = new BackGroundParticipate(Participate.this);
                    r.execute(cud, usernames);

                }
                break;

            case R.id.listpartr:
                Toast.makeText(getApplicationContext(), "Rando already participated", Toast.LENGTH_LONG)
                        .show();
                Intent main = new Intent(Participate.this, ListParticiper.class);
                main.putExtra("me", usernames);
                startActivity(main);
                break;
        }

    }


    class BackGroundVerifPart extends AsyncTask<String, String, String> {
        String nompart;
        ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {


            nompart = params[0];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/VerifPart.php?nompart=" + nompart);
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


            if (s.equals("!You dont have any current randon on time") || s.startsWith("!You dont have any current randon on time") || s.equals("You dont have any current randon on time") || s.startsWith("You dont have any current randon on time")) {
                Toast.makeText(Participate.this, "You can participate on another rando! ", Toast.LENGTH_LONG).show();

            } else if (s.equals("You never created rando and u never participated on rando") || s.startsWith("You never created rando and u never participated on rando")) {
                Toast.makeText(Participate.this, "Take a step to participate on your first rando right now!", Toast.LENGTH_LONG).show();


            } else if (s.equals("You have a rando that created en cours!") || s.startsWith("You have a rando that created en cours!")) {
                valide.setEnabled(false);
                valide.setVisibility(View.GONE);
                codee.setEnabled(false);
                codee.setVisibility(View.GONE);
                listparr.setEnabled(false);
                listparr.setVisibility(View.GONE);

                Toast.makeText(Participate.this, "You already organized on rando! make sure you finish it so u may participate on a rando", Toast.LENGTH_LONG).show();

            } else if (s.equals("!Rando Soon") || s.startsWith("!Rando Soon")) {
                Toast.makeText(Participate.this, "Welcome Participant", Toast.LENGTH_LONG).show();
                String x = s.substring(1, s.length());
                valide.setEnabled(false);
                valide.setVisibility(View.GONE);
                codee.setEnabled(false);
                codee.setVisibility(View.GONE);
                listparr.setEnabled(true);
                listparr.setVisibility(View.VISIBLE);

                Intent ll = new Intent(Participate.this, ListParticiper.class);
                ll.putExtra("me", usernames);
                startActivity(ll);

            } else if (s.equals("!rando right now") || s.startsWith("!rando right now")) {
                Toast.makeText(Participate.this, "Today is the big day,Have a good rando " + usernames + "!", Toast.LENGTH_LONG).show();
                valide.setEnabled(false);
                valide.setVisibility(View.GONE);
                codee.setEnabled(false);
                codee.setVisibility(View.GONE);

                BackGroundRandoPart tt=new BackGroundRandoPart();
                tt.execute(usernames);

                if (codehim!=null)
                {



                    h.postDelayed(new Runnable() {
                        public void run() {
                            BackGroundVerifAppel ey = new BackGroundVerifAppel(Participate.this);
                            ey.execute(usernames,codehim);
                            runnable = this;

                            h.postDelayed(runnable, delay);
                        }
                    }, delay);

                }









                /*
                Intent ha = new Intent(Participate.this, Map_offline.class);
                ha.putExtra("me", usernames);
                startActivity(ha);*/

            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }


    class BackGroundParticipate extends AsyncTask<String, String, String> {
        Context cx;
        ProgressDialog progressDialog;
        String nompart;
        String code;

        public BackGroundParticipate(Context cx) {
            this.cx = cx;
        }

        @Override
        protected String doInBackground(String... params) {


            code = params[0];
            nompart = params[1];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/participation.php?code=" + code + "&nom=" + nompart);
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
                Toast.makeText(cx, "Your participation was add Successfully", Toast.LENGTH_LONG).show();
                valide.setEnabled(false);
                valide.setVisibility(View.GONE);
                codee.setEnabled(false);
                codee.setVisibility(View.GONE);
                listparr.setEnabled(true);
                listparr.setVisibility(View.VISIBLE);
                Intent h = new Intent(cx, ListParticiper.class);
                h.putExtra("me", nompart);

                cx.startActivity(h);

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


    class BackGroundRandoPart extends AsyncTask<String, String, String> {
        String Codes;

        @Override
        protected String doInBackground(String... params) {


            String aa = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/affichecodepart.php?nom=" + aa);
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

            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                Codes = user_data.getString("Code");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            codehim = Codes;

        }
    }
}
