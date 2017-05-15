package com.example.yassine.as;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
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

import com.example.yassine.as.R;

public class Organiser extends AppCompatActivity implements View.OnClickListener {
    String usernames;
    Button organiser;
    Button listorganiiser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser);

        Intent intent = getIntent();

        Bundle b = intent.getExtras();

        usernames = b.getString("usernametest");
        String passwords = b.getString("passwordtest");
        this.setTitle("Welcome " + usernames);
        BackGroundVerifOrgg o = new BackGroundVerifOrgg();
        o.execute(usernames);
        organiser = (Button) findViewById(R.id.organiserr);
        listorganiiser = (Button) findViewById(R.id.listorganiser);
        listorganiiser.setEnabled(false);
        listorganiiser.setVisibility(View.GONE);
        organiser.setOnClickListener(this);
        listorganiiser.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.organiserr:
                Toast.makeText(getApplicationContext(), "Organize a rando!", Toast.LENGTH_LONG)
                        .show();
                Intent maiin = new Intent(Organiser.this, MapsActivity.class);
                maiin.putExtra("usro", usernames);
                startActivity(maiin);
                break;

            case R.id.listorganiser:
                Toast.makeText(getApplicationContext(), "Rando already created", Toast.LENGTH_LONG)
                        .show();
                Intent main = new Intent(Organiser.this, ListOrganiser.class);
                main.putExtra("me", usernames);

                startActivity(main);
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }

    class BackGroundVerifOrgg extends AsyncTask<String, String, String> {
        String nomorg;
        ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {


            nomorg = params[0];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/verifOrg.php?nomorg=" + nomorg);
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

            if (s.equals("!You dont have any current randon on time")||s.startsWith("!You dont have any current randon on time")||s.equals("You dont have any current randon on time")||s.startsWith("You dont have any current randon on time")) {
                Toast.makeText(Organiser.this, "You can create another rando! and hope you enjoyed your last rando! ", Toast.LENGTH_LONG).show();

            }
            else if (s.equals("You have no rando participated en cours!")||s.startsWith("You have no rando participated en cours!")) {
                Toast.makeText(Organiser.this, "Take a step to Create your rando right now!", Toast.LENGTH_LONG).show();


            }
           else if (s.equals("You never created rando and u never participated on rando")||s.startsWith("You never created rando and u never participated on rando")) {
                Toast.makeText(Organiser.this, "Take a step to Create your first rando right now!", Toast.LENGTH_LONG).show();


            } else if (s.equals("You have a rando that participated en cours!")||s.startsWith("You have a rando that participated en cours!")||s.equals("You have a rando that participate en cours!")||s.startsWith("You have a rando that participate en cours!")) {
                organiser.setEnabled(false);
                organiser.setVisibility(View.GONE);
                listorganiiser.setEnabled(false);
                listorganiiser.setVisibility(View.GONE);

                Toast.makeText(Organiser.this, "You already participated on rando! make sure you finish it so u may create a rando", Toast.LENGTH_LONG).show();

            } else if (s.equals("!Rando Soon") ||s.startsWith("!Rando Soon")) {
                Toast.makeText(Organiser.this, "Welcome Organizer", Toast.LENGTH_LONG).show();
                String x = s.substring(1, s.length());
                organiser.setEnabled(false);
                organiser.setVisibility(View.GONE);
                listorganiiser.setEnabled(true);
                listorganiiser.setVisibility(View.VISIBLE);

                Intent ll = new Intent(Organiser.this, ListOrganiser.class);
                ll.putExtra("me", nomorg);

                startActivity(ll);

            } else if (s.equals("!rando right now")||s.startsWith("!rando right now")) {
                Toast.makeText(Organiser.this, "Today is the big day,Have a good rando user!", Toast.LENGTH_LONG).show();
                organiser.setEnabled(false);
                organiser.setVisibility(View.GONE);


            Intent ha = new Intent(Organiser.this, MapOrganisateur.class);
            ha.putExtra("me", nomorg);
            startActivity(ha);

            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

   
}
