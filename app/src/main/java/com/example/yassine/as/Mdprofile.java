package com.example.yassine.as;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Mdprofile extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    EditText name, username, password, email, birthdate;
    Button sign, cancel;
    RadioButton sexe, MALE, FEMALE;
    RadioGroup rgg1;
    TextView stat;
    private ProgressDialog pDialog;
    Handler _handler;
    String Name, Password, Email,Namee,Birthdate,Sexe,Gender;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdprofile);
        Intent intent = getIntent();

        user = intent.getExtras().getString("usr");
        this.setTitle("Welcome " + user);

        _handler = new Handler();

        name = (EditText) findViewById(R.id.name);

        password = (EditText) findViewById(R.id.passwordss);
        email = (EditText) findViewById(R.id.emails);
        birthdate = (EditText) findViewById(R.id.birthdate);
        MALE = (RadioButton) findViewById(R.id.raadio1);
        FEMALE = (RadioButton) findViewById(R.id.raadio2);
        rgg1 = (RadioGroup) findViewById(R.id.rag1);
        sign = (Button) findViewById(R.id.btsign);
        sign.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.btcancel);
        cancel.setOnClickListener(this);

        name.addTextChangedListener(this);


        birthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Mdprofile.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
/*
                        if (selectedday < 10) {
                            datte.setText(selectedyear + "/" + selectedmonth + "/0"+ selectedday);
                        } else {
                            datte.setText(selectedyear + "/" + selectedmonth + "/"  + selectedday);
                        }
                        if (selectedmonth + 1 < 10) {
                            int x = selectedmonth + 1;
                            datte.setText(selectedyear + "/" + 0 + x + "/" + selectedday);
                        } else {
                            datte.setText(selectedyear + "/" + selectedmonth + "/" + selectedday);
                        }*/

                        int x = selectedmonth + 1;

                        if (selectedday < 10 && x < 10)
                            birthdate.setText(selectedyear + "/" + 0 + x + "/" + 0 + selectedday);
                        else if (selectedday < 10)
                            birthdate.setText(selectedyear + "/" + x + "/" + 0 + selectedday);
                        else if (x < 10)
                            birthdate.setText(selectedyear + "/" + 0 + x + "/" + selectedday);
                        else if (selectedday > 10 && x > 10)
                            birthdate.setText(selectedyear + "/" + x + "/" + selectedday);
                        else if (selectedday > 10)
                            birthdate.setText(selectedyear + "/" + x + "/" + selectedday);
                        else if (x > 10)
                            birthdate.setText(selectedyear + "/" + x + "/" + selectedday);



                    /*      Your code   to get date and time    */

                    }
                }, mYear-18, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }




    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btsign:
                MediaPlayer mp = MediaPlayer.create(this, R.raw.soundmenu);
                mp.start();

                String text2 = password.getText().toString();
                String text3 = email.getText().toString();
                String text4 = name.getText().toString();
                String text5 = birthdate.getText().toString();
              //  Gender = ((RadioButton)findViewById(rgg1.getCheckedRadioButtonId())).getText().toString();


                String[] tab1 = {"Male"};
                String[] tab2 = {"FEMALE"};
                if (MALE.isChecked()) {
                } else if (FEMALE.isChecked()) {
                }


                final ProgressDialog progressDialog = new ProgressDialog(Mdprofile.this, R.style.TransparentProgressDialog);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(Html.fromHtml("<font color='#ffffff'>Loading...</font>"));
                progressDialog.setMax(5);
                progressDialog.setProgress(1);
                progressDialog.setSecondaryProgress(2);
                progressDialog.show();


                //               Intent main = new Intent(Signup.this, Connexion.class);
                //             startActivity(main);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            // some stuff to do the task...
                            while (progressDialog.getProgress() <= progressDialog.getMax()) {

                                Thread.sleep(1000);

                                _handler.post(new Runnable() {
                                    public void run() {
                                        progressDialog.incrementProgressBy(1);
                                    }
                                });

                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                Handler handle = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        progressDialog.incrementProgressBy(1);
                    }
                };


                signup();


                break;
            case R.id.btcancel:
                clearForm((ViewGroup) findViewById(R.id.mdpf));
                break;



        }
    }


    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof RadioButton) {
                ((RadioButton) view).setChecked(false);
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }


    public void signup() {
        if (!validate()) {
            onSignupFailed();
            MediaPlayer mpp = MediaPlayer.create(this, R.raw.sounderror);
            mpp.start();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(400);
        } else if (validate()) {
            onSignupSuccess();


        }
    }


    public void onSignupSuccess() {
        String text2 = password.getText().toString();
        String text3 = email.getText().toString();
        String text4 = name.getText().toString();
        String text5 = birthdate.getText().toString();
        Sexe = sexe.getText().toString();

        sign.setEnabled(true);
        BackGroundMDPRofile b = new BackGroundMDPRofile(Mdprofile.this);
        b.execute(user, text2, text3,text4,text5,Sexe);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Update failed", Toast.LENGTH_LONG).show();

        sign.setEnabled(true);
    }

    public boolean validate() {

        String text2 = password.getText().toString();
        String text3 = email.getText().toString();
        String text4 = name.getText().toString();
        String text5 = birthdate.getText().toString();


        int selected = rgg1.getCheckedRadioButtonId();
        sexe = (RadioButton) findViewById(selected);
        boolean valid = true;



        if (text3.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(text3).matches()) {
            email.setError("enter a valid email address");
            Drawable dr = getResources().getDrawable(R.drawable.error);
            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
            email.setCompoundDrawables(null, null, dr, null);
            valid = false;


        } else {
            email.setError(null);
            email.setCompoundDrawables(null, null, null, null);


        }

        if (text2.isEmpty() || text2.length() < 4 || text2.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            Drawable dr = getResources().getDrawable(R.drawable.error);
            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
            password.setCompoundDrawables(null, null, dr, null);
            valid = false;

        } else {
            password.setError(null);
            password.setCompoundDrawables(null, null, null, null);


        }

        if (text4.isEmpty() || text4.length() < 3) {
            name.setError("at least 3 characters for the name");
            Drawable dr = getResources().getDrawable(R.drawable.error);
            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
            name.setCompoundDrawables(null, null, dr, null);
            valid = false;

        } else {
            name.setError(null);
            name.setCompoundDrawables(null, null, null, null);


        }

        if (selected < 0) {
            Toast.makeText(getBaseContext(), "Choose ur gender", Toast.LENGTH_LONG).show();

            valid = false;

        }


        if (text5.isEmpty() || !isValidDate(text5)) {
            birthdate.setError("enter a valid birth date");
            Drawable dr = getResources().getDrawable(R.drawable.error);
            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
            birthdate.setCompoundDrawables(null, null, dr, null);
            valid = false;


        } else {
            birthdate.setError(null);
            birthdate.setCompoundDrawables(null, null, null, null);


        }


        return valid;

    }

    /*

        public static String formatDate(String inputDate) {
            String formattedDate;
            try {
                DateFormat originalFormat = new SimpleDateFormat("dd/mm/yyyy"); //Input date format

                DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy"); //Output date format
                Date date = originalFormat.parse(inputDate);
                formattedDate = targetFormat.format(date);
                return formattedDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "no_date";
        }


        public static boolean ValidDate(String inputDate) {
            String formattedDate;
            try {
                DateFormat originalFormat = new SimpleDateFormat("dd/mm/yyyy"); //Input date format

                DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy"); //Output date format
                Date date = originalFormat.parse(inputDate);
                formattedDate = targetFormat.format(date);
                return true;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }
    */
    public boolean isValidDate(String date) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        // declare and initialize testDate variable, this is what will hold
        // our converted string

        Date testDate = null;
        Date datecurrent = new Date();

        // we will now try to parse the string into date form
        try {
            testDate = sdf.parse(date);
        }

        // if the format of the string provided doesn't match the format we
        // declared in SimpleDateFormat() we will get an exception

        catch (ParseException e) {

            return false;
        }

        // dateformat.parse will accept any date as long as it's in the format
        // you defined, it simply rolls dates over, for example, december 32
        // becomes jan 1 and december 0 becomes november 30
        // This statement will make sure that once the string
        // has been checked for proper formatting that the date is still the
        // date that was entered, if it's not, we assume that the date is invalid

        if (!sdf.format(testDate).equals(date)) {
            return false;
        }

        if (testDate.after(datecurrent)) {
            return false;
        }
        // if we make it to here without getting an error it is assumed that
        // the date was a valid one and that it's in the proper format
        if (testDate.compareTo(datecurrent)==0) {
            return false;
        }
        return true;

    } // end isValidDate

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

/*
        Drawable dr = getResources().getDrawable(R.drawable.clear);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        name.setCompoundDrawables(null, null, dr, null);
*/

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            //if the enter key was pressed, then hide the keyboard and do whatever needs doing.
            InputMethodManager imm = (InputMethodManager) Mdprofile.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(username.getApplicationWindowToken(), 0);
            imm.hideSoftInputFromWindow(password.getApplicationWindowToken(), 0);
            imm.hideSoftInputFromWindow(birthdate.getApplicationWindowToken(), 0);
            imm.hideSoftInputFromWindow(name.getApplicationWindowToken(), 0);
            imm.hideSoftInputFromWindow(email.getApplicationWindowToken(), 0);


            //do what you need on your enter key press here

            return true;
        }

        return false;
    }

}
