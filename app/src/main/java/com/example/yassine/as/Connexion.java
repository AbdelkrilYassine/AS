package com.example.yassine.as;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import java.io.IOException;

import static android.R.attr.type;


public class Connexion extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    EditText usr, pwd;
    Button log;
    ImageView iv;
    TextView sig, forget;
    Boolean test;
    CheckBox ck;
    public static String filename = "Rando";
    SharedPreferences SP;
    int counter = 3;
    String userName,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        getSupportActionBar().hide();
        usr = (EditText) findViewById(R.id.usr);
        pwd = (EditText) findViewById(R.id.pwd);
        log = (Button) findViewById(R.id.login);
        sig = (TextView) findViewById(R.id.sig);
        ck = (CheckBox) findViewById(R.id.ck);
        forget = (TextView) findViewById(R.id.forg);


        //iv = (ImageView) findViewById(R.id.iv);

        // iv.setOnClickListener(this);
        log.setOnClickListener(this);
        sig.setOnClickListener(this);
        forget.setOnClickListener(this);


        usr.setOnKeyListener(this);
        pwd.setOnKeyListener(this);
/*
        pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pwd.clearFocus();
                        pwd.setFocusable(true);
                        pwd.requestFocus();
                        pwd.setSelection(pwd.getText().length());
                        break;
                    case MotionEvent.ACTION_UP:
                        pwd.setTransformationMethod(new PasswordTransformationMethod());
                        break;
                    default:
                        break;
                }
                return true;

            }
        });

*/


        SP = getSharedPreferences(filename, 0);
        String getname = SP.getString("key1",null);
        String getpass = SP.getString("key2",null);
        usr.setText(getname);
        pwd.setText(getpass);




        if (ContextCompat.checkSelfPermission(Connexion.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Connexion.this, new String[]{Manifest.permission.INTERNET}, 6);
        }

       if( isNetworkOnline(this))
       {
           Context context = getApplicationContext();
           CharSequence text = "Internet is available";
           int duration = Toast.LENGTH_SHORT;

           Toast toast = Toast.makeText(context, text, duration);
           toast.show();
           log.setBackgroundColor(getResources().getColor(R.color.orangie));
       }
       else
       {
           Context context = getApplicationContext();
           CharSequence text = "Please activate the internet";
           int duration = Toast.LENGTH_SHORT;

           Toast toast = Toast.makeText(context, text, duration);
           toast.show();
       }




    }




    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;

     }

*/


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.forg:
                MediaPlayer mprp = MediaPlayer.create(Connexion.this, R.raw.esound);
                mprp.start();

                Context context = getApplicationContext();
                CharSequence text = "Enter your @ Email";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();


                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(Html.fromHtml("<font color='#FF0000'>Cant Sign in? Forget your password?</font>"));
                builder.setIcon(R.drawable.di);
                String Newligne = System.getProperty("line.separator");

                final EditText email = new EditText(context);
                email.setHeight(70);
                email.setWidth(100);
                email.setGravity(Gravity.LEFT);
                email.setTextColor(getResources().getColor(android.R.color.black));
                email.setTextSize(10);
                email.setImeOptions(EditorInfo.IME_ACTION_DONE);
                builder.setView(email);
                builder.setMessage("Enter your email address below and we'll send you password reset instructions" + Newligne);


                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                builder.setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {


                                if (email.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())

                                {
                                    Toast.makeText(getApplicationContext(), "Enter a valid Email Adress ", Toast.LENGTH_LONG).show();
                                    arg0.dismiss();

                                } else {

                                    /*
                                    String to = email.getText().toString();
                                    String subject = "forgetten password";
                                    String message = "Here is your old password";
                                    Intent email = new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                                    email.putExtra(Intent.EXTRA_TEXT, message);

                                    // need this to prompts email client only
                                    email.setType("message/rfc822");

                                    startActivity(Intent.createChooser(email, "Choose an Email client"));*/
                                }


                            }
                        }

                );


                builder.show();


                break;

            case R.id.sig:

                MediaPlayer mpp = MediaPlayer.create(this, R.raw.pipe);
                mpp.start();

                Intent s = new Intent(Connexion.this, Signup.class);

                startActivity(s);

                break;

            case R.id.login:

                MediaPlayer mp = MediaPlayer.create(this, R.raw.soundmenu);
                mp.start();

                Context context1 = getApplicationContext();
                String userName = usr.getText().toString();
                String password = pwd.getText().toString();

             /*   final ProgressDialog progressDialog = new ProgressDialog(Connexion.this, R.style.TransparentProgressDialog);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                new

                        Handler()

                        .

                                postDelayed(new Runnable() {
                                                public void run() {
                                                    progressDialog.dismiss();
                                                }
                                            }

                                        , 4000);*/

                signin();


                if (counter == 0)

                {

                    this.finish();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                   /* int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);*/

                }




                if(ck.isChecked()) {


                    SharedPreferences.Editor editit = SP.edit();
                    editit.putString("key1", userName);
                    editit.putString("key2", password);
                    editit.commit();
                }else
                {
                    usr.getText().clear();
                        pwd.getText().clear();
                }


        }
    }


    public void signin() {
        if (!validate()) {
            onSigninFailed();
            counter--;
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(400);
            MediaPlayer mpp = MediaPlayer.create(this, R.raw.sounderror);
            mpp.start();

        } else if (validate()) {
            onSigninSuccess();

        }
    }


    public void onSigninSuccess() {



     //   Toast.makeText(getApplicationContext(), "Congrats: Sign In Successfull", Toast.LENGTH_LONG).show();

        log.setEnabled(true);

    //    Intent main = new Intent(Connexion.this, TabWidget.class);
        // main.putExtra("usr", userName);
     //   Bundle b = new Bundle();
      //  b.putString("username", userName);
      //  b.putString("password", password);
     //   String type = "login";

     //   main.putExtras(b);
        //  BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        //   backgroundWorker.execute(type, userName, password);

       // startActivity(main);
        BackGround b = new BackGround(Connexion.this);
        b.execute(userName, password);

    }

    public void onSigninFailed() {
        Toast.makeText(getBaseContext(), "Sign in failed", Toast.LENGTH_LONG).show();
        log.setEnabled(true);

    }


    public boolean validate() {
        boolean valid = true;
        userName = usr.getText().toString();
        password = pwd.getText().toString();
        int Count = userName.replaceAll("[^.]", "").length();


        if (userName.isEmpty() || userName.startsWith(".") || userName.endsWith(".") || Count > 1 || Character.isDigit(userName.charAt(0))) {
            // usr.setError("Enter a valid username");
            Drawable dr = getResources().getDrawable(R.drawable.wrong);
            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());


            Drawable drr = getResources().getDrawable(R.drawable.useme3);
            drr.setBounds(0, 0, drr.getIntrinsicWidth(), drr.getIntrinsicHeight());

            usr.setError("Enter a valid username", dr);

            usr.setCompoundDrawables(drr, null, dr, null);

            valid = false;
        } else {
            usr.setError(null, null);


            usr.setCompoundDrawables(null, null, null, null);


        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            // pwd.setError("Enter a valid password");
            Drawable dr = getResources().getDrawable(R.drawable.wrong);
            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
            pwd.setError("Enter a valid password", dr);

            Drawable drr = getResources().getDrawable(R.drawable.passme3);
            drr.setBounds(0, 0, drr.getIntrinsicWidth(), drr.getIntrinsicHeight());
            pwd.setCompoundDrawables(drr, null, dr, null);

            //pwd.setCompoundDrawables(null, null, dr, null);
            valid = false;
        } else {
            pwd.setError(null, null);

            pwd.setCompoundDrawables(null, null, null, null);

        }


        return valid;

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            //if the enter key was pressed, then hide the keyboard and do whatever needs doing.
            InputMethodManager imm = (InputMethodManager) Connexion.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(usr.getApplicationWindowToken(), 0);
            imm.hideSoftInputFromWindow(pwd.getApplicationWindowToken(), 0);

            //do what you need on your enter key press here

            return true;
        }

        return false;
    }


    protected void sendEmail(String[] TO, String[] mail) {


        Log.i("Send email", "");
        String[] CC = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, "Here is your Password!");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reset Password");
        emailIntent.putExtra(Intent.EXTRA_TEXT, mail);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
            Toast.makeText(Connexion.this, "Finished sending email.", Toast.LENGTH_SHORT).show();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Connexion.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //start audio recording or whatever you planned to do
            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(Connexion.this, Manifest.permission.INTERNET)) {
                    //Show an explanation to the user *asynchronously*
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This permission is important to Internet.")
                            .setTitle("Important permission required");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(Connexion.this, new String[]{Manifest.permission.INTERNET}, 1);
                        }
                    });
                    ActivityCompat.requestPermissions(Connexion.this, new String[]{Manifest.permission.INTERNET},1);
                }else{
                    //Never ask again and handle your app without permission.
                }
            }
        }
    }



    public static boolean isNetworkOnline(Context con)
    {
        boolean status = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);

                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                } else {
                    status = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return status;
    }

}