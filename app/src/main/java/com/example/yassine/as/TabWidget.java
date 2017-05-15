package com.example.yassine.as;

import android.app.Activity;
import android.app.TabActivity;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yassine.as.R;

import static android.R.attr.path;


public class TabWidget extends TabActivity {

    private Menu menu;
    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    TextView popupText;
    ActionBar mActionBar;
    int x = 0;
    AudioManager mgr = null;
    SharedPreferences pre;
    SeekBar sek1, sek2;
    WifiManager wifimanager;
    Switch s;
    AudioManager audiomanager;
    Context context;
    String usernames;
    int volume_level;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_widget);

        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);



        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        usernames = b.getString("username");
        String passwords = b.getString("password");

        Bundle btest = new Bundle();
        btest.putString("usernametest", usernames);
        btest.putString("passwordtest", passwords);

        this.setTitle("Welcome " + usernames);
        context = getApplicationContext();

        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volume_level = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);


     /*   Intent main = new Intent(TabWidget.this, Test.class);
        Bundle btest = new Bundle();
        btest.putString("usernametest", usernames);
        btest.putString("passwordtest", passwords);

        main.putExtras(btest);


        startActivity(main);

*/


        Resources res = getResources();
        // create the TabHost that will contain the Tabs
        //       final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        tabHost.setup();


        final TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third tab");
        //      TabHost.TabSpec tab4 = tabHost.newTabSpec("Fourth tab");
//        TabHost.TabSpec tab5 = tabHost.newTabSpec("Fifth Tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected


        tab1.setIndicator("Profile", res.getDrawable(R.drawable.profile));
        Intent im = new Intent(this, Test.class);
        im.putExtras(btest).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ;
        tab1.setContent(im);
        //  tab1.setContent(new Intent (this, Test.class));

        tab2.setIndicator("Organiser", res.getDrawable(R.drawable.organiser));
        Intent im2 = new Intent(this, Organiser.class);
        im2.putExtras(btest).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ;
        tab2.setContent(im2);

        // tab2.setContent(new Intent(this, Organiser.class));

        tab3.setIndicator("Participer", res.getDrawable(R.drawable.participer));
        Intent im3 = new Intent(this, Participate.class);
        im3.putExtras(btest).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ;
        tab3.setContent(im3);

        //tab3.setContent(new Intent(this, Participate.class));

        //    tab4.setIndicator("L.Organisée", res.getDrawable(R.drawable.listorganiser));
        //  tab4.setContent(new Intent(this, Tab4.class));

        //tab5.setIndicator("L.Participée", res.getDrawable(R.drawable.listparticiper));
        //tab5.setContent(new Intent(this, Tab5.class));
        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        //tabHost.addTab(tab4);
        //tabHost.addTab(tab5);


        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
        }


        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#FF4081"));


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                updateTabs();
                MediaPlayer mpp = MediaPlayer.create(TabWidget.this, R.raw.soundsign);
                mpp.start();


            }
        });


    }


    protected void updateTabs() {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

            if (tabHost.getTabWidget().getChildAt(i).isSelected()) {
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                tv.setTextColor(Color.parseColor("#FF4081"));
            } else {
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                tv.setTextColor(Color.parseColor("#ffffff"));

            }
        }

    }


    //******************************************************************************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu, menu);


        MenuItem item = menu.findItem(R.id.menu_logout);
        SpannableStringBuilder builder = new SpannableStringBuilder("        Logout     ");
        builder.setSpan(new ImageSpan(this, R.drawable.lougout), 17, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);

        MenuItem item1 = menu.findItem(R.id.menu_settings);
        SpannableStringBuilder builder1 = new SpannableStringBuilder("        Settings   ");
        builder1.setSpan(new ImageSpan(this, R.drawable.wsett), 17, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item1.setTitle(builder1);


        MenuItem item2 = menu.findItem(R.id.menu_help);
        SpannableStringBuilder builder2 = new SpannableStringBuilder("        Help           ");
        builder2.setSpan(new ImageSpan(this, R.drawable.help), 20, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item2.setTitle(builder2);


        MenuItem item3 = menu.findItem(R.id.menu_about);
        SpannableStringBuilder builder3 = new SpannableStringBuilder("        About          ");
        builder3.setSpan(new ImageSpan(this, R.drawable.winf), 19, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item3.setTitle(builder3);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:

                MediaPlayer mp = MediaPlayer.create(this, R.raw.soundmenu);
                mp.start();

                showPopup(this, "About :", "Cette application Randon-ili a pour objectifs de découvrir et d'augmenter le nombre des randonnées, d'améliorer leur qualité et de faciliter d'avoir des randonneurs.Développé par Yassine Ben Abdelkrim");


                return true;
            case R.id.menu_help:
                MediaPlayer mp1 = MediaPlayer.create(this, R.raw.soundmenu);
                mp1.start();
                showPopup(this, "Help", "Contact : randon_ili.com");

                return true;

            case R.id.menu_logout:
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.soundmenu);
                mp2.start();
                BackGroundLogout b = new BackGroundLogout(TabWidget.this);
                b.execute(usernames);


                return true;

            case R.id.menu_settings:
                MediaPlayer mp3 = MediaPlayer.create(this, R.raw.soundmenu);
                mp3.start();
                showPopup2(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // The method that displays the popup.
    private void showPopup(final Activity context, String ch1, String ch2) {
        int popupWidth = 1000;
        int popupHeight = 900;


        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);


        TextView txt1 = (TextView) layout.findViewById(R.id.textView1);
        TextView txt2 = (TextView) layout.findViewById(R.id.textView2);

        txt1.setText(ch1);
        txt2.setText(ch2);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 220;
        int OFFSET_Y = 900;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setAnimationStyle(android.R.style.Animation_Dialog);

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, OFFSET_X, OFFSET_Y);

        Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

    }

    private void showPopup2(final Activity context) {
        int popupWidth = 1000;
        int popupHeight = 900;


        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup2);

        View layout = layoutInflater.inflate(R.layout.pop, viewGroup);

        final TextView progg = (TextView) layout.findViewById(R.id.prog);
        sek1 = (SeekBar) layout.findViewById(R.id.seek1);

        final SharedPreferences sharedPref1 = getSharedPreferences("com.example.SEK", MODE_PRIVATE);
        int mProgress = sharedPref1.getInt("mMySeekBarProgress", 0);
        sek1.setProgress(mProgress);

        sek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //---change the font size of the EditText---
                mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                mgr.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);


                SharedPreferences.Editor editor = getSharedPreferences("com.example.SEK", MODE_PRIVATE).edit();
                editor.putInt("mMySeekBarProgress", progress).commit();
            }
        });


        Switch swi = (Switch) layout.findViewById(R.id.switch1);

        SharedPreferences sharedPref2 = getSharedPreferences("com.example.ASVi", MODE_PRIVATE);
        swi.setChecked(sharedPref2.getBoolean("Switch3", true));

        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {
                    audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.ASVi", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch3", true);
                    editor.commit();

                } else {
                    audiomanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                    SharedPreferences.Editor editor = getSharedPreferences("com.example.ASVi", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch3", false);
                    editor.commit();

                }
            }
        });


        s = (Switch) layout.findViewById(R.id.switch2);
        SharedPreferences sharedPref3 = getSharedPreferences("com.example.AS", MODE_PRIVATE);
        s.setChecked(sharedPref3.getBoolean("Switch2", true));

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {
                    Toast.makeText(TabWidget.this, "WiFi ON", Toast.LENGTH_SHORT).show();
                    EnableWiFi();
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.AS", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch2", true);
                    editor.commit();

                } else {
                    Toast.makeText(TabWidget.this, "WiFi OFF", Toast.LENGTH_SHORT).show();
                    DisableWiFi();
                    SharedPreferences.Editor editor = getSharedPreferences("com.example.AS", MODE_PRIVATE).edit();
                    editor.putBoolean("Switch2", false);
                    editor.commit();

                }
            }
        });

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 220;
        int OFFSET_Y = 900;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setAnimationStyle(android.R.style.Animation_Dialog);

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, OFFSET_X, OFFSET_Y);


    /*    if(swi.isChecked())
        {

        }*/


    }


    public void EnableWiFi() {

        wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(true);

    }

    public void DisableWiFi() {

        wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifimanager.setWifiEnabled(false);

    }

  /*  public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE) {
            BackGroundLogout tr=new BackGroundLogout(TabWidget.this);
            tr.execute(usernames);


        }}*/




}

