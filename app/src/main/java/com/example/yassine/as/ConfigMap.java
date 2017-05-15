package com.example.yassine.as;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigMap extends AppCompatActivity implements View.OnClickListener {
    Button confirmm, cancell;
    EditText lieuarr, hdep, datte, distancepp, harr, lieudep, nbplace, nome, tel;
    private Pattern pattern;
    private Matcher matcher;
    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    private Spinner spinner1, spinner2, spinner3, spinner4;
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
    private static char[] hextable = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    String chaine;
    List<Address> adrr, adm;
    String vsp1, vsp2, vsp3, vsp4;
    String text1, text2, text3, text4, text5, text6, text7, text9, text10;
    List<String> road, moun;
    String test = null, test2 = null, msg = null;
    String[] tabme;
    String nameme;
    int i = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_map);
        road = new ArrayList<String>();
        moun = new ArrayList<String>();
        chaine = getIntent().getStringExtra("chaine");
        // adrr = getIntent().getParcelableArrayListExtra("troute");
        // adm = getIntent().getParcelableArrayListExtra("tm");
        Intent intent = getIntent();

        nameme = intent.getExtras().getString("usrme");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            road = bundle.getStringArrayList("pathroad");
            moun = bundle.getStringArrayList("pathmoun");



        }



        /*
        Bundle ba = intent.getExtras();

        test2 = ba.getString("test2");

        Bundle baa = intent.getExtras();

        msg = baa.getString("msg"); */

        confirmm = (Button) findViewById(R.id.confirmm2);
        cancell = (Button) findViewById(R.id.cancell2);
        confirmm.setOnClickListener(this);
        cancell.setOnClickListener(this);

        nbplace = (EditText) findViewById(R.id.nbplace);
        datte = (EditText) findViewById(R.id.datte);
        lieudep = (EditText) findViewById(R.id.lieudep);
        distancepp = (EditText) findViewById(R.id.distparr);
        hdep = (EditText) findViewById(R.id.hdep);
        harr = (EditText) findViewById(R.id.hrearr);
        nome = (EditText) findViewById(R.id.nom);
        lieuarr = (EditText) findViewById(R.id.lieuarr);
        tel = (EditText) findViewById(R.id.tel);


        spinner1 = (Spinner) findViewById(R.id.spinner1);
        String[] values = new String[]{"Ariana", "Béja", "Ben-Arous", "Bizerte", "Gabès", "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kébili", "Le-Kef", "Mahdia", "La-Manouba", "Médenine", "Monastir", "Nabeul", "Sfax", "Sidi-Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"};


        spinner2 = (Spinner) findViewById(R.id.spinner2);
        String[] values2 = new String[]{"Facile", "Moyenne", "Difficile", "Trés-difficile", "Extrêmement-difficile"};

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        String[] values3 = new String[]{"A-pied", "A-VTT", "Cyclo-route", "A-ski", "En-raquettes-à-neige", "A-cheval", "En-barque,canoé,kayak,quad"};

        spinner4 = (Spinner) findViewById(R.id.spinner4);
        String[] values4 = new String[]{"Moins.de.2h", "Entre(2h)et(3h)", "Entre(3h)et(4h)", "Entre(4h)et(5h)", "Entre(5h)et(7h)", "Plus.de.7h"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, values);
        spinner1.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, values2);

        spinner2.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, values3);

        spinner3.setAdapter(adapter3);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, R.layout.spinner_item, values4);

        spinner4.setAdapter(adapter4);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);

                vsp1 = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                vsp1 = "Ariana";
            }

        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);

                vsp2 = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                vsp2 = "Facile";

            }

        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);

                vsp3 = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                vsp3 = "A-pied";
            }

        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);

                vsp4 = parentView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                vsp4 = "Moins.de.2h";
            }

        });


        datte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ConfigMap.this, new DatePickerDialog.OnDateSetListener() {
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
                            datte.setText(selectedyear + "/" + 0 + x + "/" + 0 + selectedday);
                        else if (selectedday < 10)
                            datte.setText(selectedyear + "/" + x + "/" + 0 + selectedday);
                        else if (x < 10)
                            datte.setText(selectedyear + "/" + 0 + x + "/" + selectedday);
                        else if (selectedday > 10 && x > 10)
                            datte.setText(selectedyear + "/" + x + "/" + selectedday);
                        else if (selectedday > 10)
                            datte.setText(selectedyear + "/" + x + "/" + selectedday);
                        else if (x > 10)
                            datte.setText(selectedyear + "/" + x + "/" + selectedday);



                    /*      Your code   to get date and time    */

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


        hdep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ConfigMap.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 10 && selectedMinute < 10)
                            hdep.setText("0" + selectedHour + ":" + 0 + selectedMinute);
                        else if (selectedHour < 10)
                            hdep.setText("0" + selectedHour + ":" + selectedMinute);
                        else if (selectedMinute < 10)
                            hdep.setText(selectedHour + ":" + 0 + selectedMinute);
                        else if (selectedHour > 10 && selectedMinute > 10)
                            hdep.setText(selectedHour + ":" + selectedMinute);
                        else if (selectedHour > 10)
                            hdep.setText(selectedHour + ":" + selectedMinute);
                        else if (selectedMinute > 10)
                            hdep.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        harr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ConfigMap.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour < 10 && selectedMinute < 10)
                            harr.setText("0" + selectedHour + ":" + 0 + selectedMinute);
                        else if (selectedHour < 10)
                            harr.setText("0" + selectedHour + ":" + selectedMinute);
                        else if (selectedMinute < 10)
                            harr.setText(selectedHour + ":" + 0 + selectedMinute);
                        else if (selectedHour > 10 && selectedMinute > 10)
                            harr.setText(selectedHour + ":" + selectedMinute);
                        else if (selectedHour > 10)
                            harr.setText(selectedHour + ":" + selectedMinute);
                        else if (selectedMinute > 10)
                            harr.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


    }

    public void Rando() {
        if (!validate()) {
            onRandFailed();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(400);
        } else if (validate()) {

          /*  BackGroundTrajetM b = new BackGroundTrajetM(ConfigMap.this);
            for (int i=0; i<adm.size(); i++) {
                b.execute(chaine, adm.get(0));
            }*/

            onRandSuccess();


        }
    }


    public void onRandSuccess() {

        text1 = lieuarr.getText().toString();
        text2 = hdep.getText().toString();
        text3 = datte.getText().toString();
        text4 = distancepp.getText().toString();
        text5 = harr.getText().toString();
        text6 = lieudep.getText().toString();
        text7 = nbplace.getText().toString();
        text9 = nome.getText().toString();
        text10 = tel.getText().toString();

        confirmm.setEnabled(true);
        Context context = getApplicationContext();

        BackGroundRando b = new BackGroundRando(ConfigMap.this);

        b.execute(chaine, text3, vsp1, text7, text6, text1, text2, text5, vsp4, text4, text9, text10, vsp2, vsp3, removeChar(road.get(0)), removeChar(road.get(1)), nameme);

        for (int i = 0; i < moun.size(); i++) {
            BackGroundPointm pm = new BackGroundPointm(ConfigMap.this);
            pm.execute(chaine, moun.get(i));
        }
    }





/*

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#FF0000'>Here is your unique Rando code !"));
        builder.setIcon(R.drawable.code);
        String Newligne = System.getProperty("line.separator");

        final TextView email = new TextView(context);
email.setHeight(100);       email.setWidth(100);
        email.setGravity(Gravity.LEFT);
        email.setTextColor(getResources().getColor(android.R.color.black));
        email.setTextSize(20);
        email.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Date d = new Date();



        email.setText(chaine);
        builder.setView(email);


        //  builder.setView(fa);
        builder.setMessage("Share this code with your friends ,if you want them to participate on your rando! " + Newligne);


        builder.setPositiveButton("Alright",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        Intent s = new Intent(ConfigMap.this, TabWidget.class);

                        startActivity(s);
                    }
                }

        );


        builder.show(); */


    public void onRandFailed() {
        Toast.makeText(getApplicationContext(), "Creation failed", Toast.LENGTH_LONG).show();

        confirmm.setEnabled(true);
    }


    public boolean validate() {

        boolean valid = true;

        text1 = lieuarr.getText().toString();
        text2 = hdep.getText().toString();
        text3 = datte.getText().toString();
        text4 = distancepp.getText().toString();
        text5 = harr.getText().toString();
        text6 = lieudep.getText().toString();
        text7 = nbplace.getText().toString();
        text9 = nome.getText().toString();
        text10 = tel.getText().toString();

        int Count = text1.replaceAll("[^.]", "").length();
        int Count2 = text1.replaceAll("[^-]", "").length();
        int Count3 = text1.replaceAll("[^/]", "").length();
        int Count4 = text1.replaceAll("[^_]", "").length();

        int aCount = text6.replaceAll("[^.]", "").length();
        int aCount2 = text6.replaceAll("[^-]", "").length();
        int aCount3 = text6.replaceAll("[^/]", "").length();
        int aCount4 = text6.replaceAll("[^_]", "").length();

        int bCount = text9.replaceAll("[^.]", "").length();
        int bCount2 = text9.replaceAll("[^-]", "").length();
        int bCount3 = text9.replaceAll("[^/]", "").length();
        int bCount4 = text9.replaceAll("[^_]", "").length();

        Drawable dr = getResources().getDrawable(R.drawable.spamm);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

        try {
            int i = Integer.parseInt(text7);
            nbplace.setError(null, null);
            nbplace.setCompoundDrawables(null, null, null, null);
            valid = true;
            if (i < 4) {
                nbplace.setError("Entrer le nombre de participants", dr);
                nbplace.setCompoundDrawables(null, null, dr, null);
                valid = false;
            }

        } catch (NumberFormatException e) {
            nbplace.setError("Entrer le nombre de participants", dr);
            nbplace.setCompoundDrawables(null, null, dr, null);
            valid = false;
        }


        try {
            float j = Float.parseFloat(text4);
            distancepp.setError(null, null);
            distancepp.setCompoundDrawables(null, null, null, null);
            valid = true;
            if (j <= 0.0) {

                valid = false;
            }
        } catch (NumberFormatException e) {
            distancepp.setError("Entrer la distance à parcourir en km", dr);
            distancepp.setCompoundDrawables(null, null, dr, null);
            valid = false;
        }

        if (text1.isEmpty() || text1.startsWith(".") || text1.endsWith(".") || text1.startsWith("-") || text1.endsWith("-") || text1.startsWith("/") || text1.endsWith("/") || text1.startsWith("_") || text1.endsWith("_") || Count > 1 || Count2 > 1 || Count3 > 1 || Count4 > 1 || Character.isDigit(text1.charAt(0))) {

            lieuarr.setError("Entrer un lieu d'arrivée", dr);

            lieuarr.setCompoundDrawables(null, null, dr, null);

            valid = false;
        } else {
            lieuarr.setError(null, null);
            lieuarr.setCompoundDrawables(null, null, null, null);
        }

        if (text2.isEmpty() || text2.length() < 5 || text2.length() > 5 || !validateTime(text2)) {
            hdep.setError("Entrer l'heure de départ", dr);
            hdep.setCompoundDrawables(null, null, dr, null);
            valid = false;
        } else {
            hdep.setError(null, null);
            hdep.setCompoundDrawables(null, null, null, null);
        }
        if (text3.isEmpty() || !isValidDate(text3) || text3.length() < 10 || text3.length() > 10) {
            datte.setError("Entrer date du randonnée", dr);
            datte.setCompoundDrawables(null, null, dr, null);
            valid = false;
        } else {
            datte.setError(null, null);
            datte.setCompoundDrawables(null, null, null, null);
        }

        if (text5.isEmpty() || text5.length() < 5 || text5.length() > 5 || !validateTime(text5) || !checktimings(text2, text5)) {
            harr.setError("Entrer l'heure d'arrivée", dr);
            harr.setCompoundDrawables(null, null, dr, null);
            valid = false;
        } else {
            harr.setError(null, null);
            harr.setCompoundDrawables(null, null, null, null);
        }
        if (text6.isEmpty() || text6.startsWith(".") || text6.endsWith(".") || text6.startsWith("-") || text6.endsWith("-") || text6.startsWith("/") || text6.endsWith("/") || text6.startsWith("_") || text6.endsWith("_") || aCount > 1 || aCount2 > 1 || aCount3 > 1 || aCount4 > 1 || Character.isDigit(text6.charAt(0))) {
            lieudep.setError("Entrer lieu de départ", dr);
            lieudep.setCompoundDrawables(null, null, dr, null);
            valid = false;
        } else {
            lieudep.setError(null, null);
            lieudep.setCompoundDrawables(null, null, null, null);
        }

        if (text9.isEmpty() || text9.startsWith(".") || text9.endsWith(".") || text9.startsWith("-") || text9.endsWith("-") || text9.startsWith("/") || text9.endsWith("/") || text9.startsWith("_") || text9.endsWith("_") || bCount > 1 || bCount2 > 1 || bCount3 > 1 || bCount4 > 1 || Character.isDigit(text9.charAt(0))) {
            nome.setError("Entrer nom du randonnée", dr);
            nome.setCompoundDrawables(null, null, dr, null);
            valid = false;
        } else {
            nome.setError(null, null);
            nome.setCompoundDrawables(null, null, null, null);
        }

        if (text10.isEmpty()) {
            tel.setError("Entrer votre numéro de teléphone", dr);
            tel.setCompoundDrawables(null, null, dr, null);
            valid = false;
        } else {
            tel.setError(null, null);
            tel.setCompoundDrawables(null, null, null, null);
        }

        return valid;
    }


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

        if (testDate.before(datecurrent)) {
            return false;
        }
        // if we make it to here without getting an error it is assumed that
        // the date was a valid one and that it's in the proper format

        return true;

    } // end isValidDate


    public boolean validateTime(final String time) {

        matcher = pattern.matcher(time);
        return matcher.matches();

    }

    public ConfigMap() {
        pattern = Pattern.compile(TIME24HOURS_PATTERN);
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.confirmm2:

                Rando();
                break;
            case R.id.cancell2:
                final ProgressDialog progressDialog1 = new ProgressDialog(ConfigMap.this, R.style.TransparentProgressDialog);
                progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog1.setIndeterminate(false);
                progressDialog1.setCancelable(true);
                progressDialog1.setMessage("Cancel...");
                progressDialog1.show();
                clearForm((ViewGroup) findViewById(R.id.activity_config_map));


                break;
        }

    }


    //********************************************************************************************************************


    private boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if (date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static String removeChar(String str) {
        return str.substring(10, str.length() - 1);
    }
}
