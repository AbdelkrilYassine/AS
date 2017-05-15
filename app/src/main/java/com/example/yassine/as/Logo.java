package com.example.yassine.as;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Logo extends AppCompatActivity  {
    ImageView img;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        img=(ImageView)findViewById(R.id.logs);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        img.setAnimation(animation);
        MediaPlayer mpp = MediaPlayer.create(Logo.this,R.raw.intrologo);
        mpp.start();

        new Handler().postDelayed(new Runnable() {

            /*
             * sinon animation.setAnimationListener(this) w fel endanimation intent w duration animation fel xml
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Logo.this, Connexion.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}