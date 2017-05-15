package com.example.yassine.as;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Imhere extends AppCompatActivity implements View.OnClickListener {
    ListView l;
    Button Cancel, ccontinue;
    Handler h = new Handler();
    int delay = 5000;
    Runnable runnable;
    String codeme,nameme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        codeme = intent.getExtras().getString("codeme");
        nameme = intent.getExtras().getString("nameme");

        setContentView(R.layout.activity_imhere);
        l = (ListView) findViewById(R.id.lhere);
        Cancel = (Button) findViewById(R.id.cancelr);
        ccontinue = (Button) findViewById(R.id.continueR);
        Cancel.setOnClickListener(this);
        ccontinue.setOnClickListener(this);
        this.setTitle("Call");

        new Downloaderlist(Imhere.this, "http://api2.randon.ili-studios.tn/allra.php?code="+codeme, l).execute();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelr:
                BackGroundAnnulerRando ann=new BackGroundAnnulerRando(Imhere.this);
                ann.execute(codeme,nameme);
                break;
            case R.id.continueR:
                BackGroundFinappel fd=new BackGroundFinappel(Imhere.this);
                fd.execute(codeme,nameme);
                break;


        }

    }

    @Override
    protected void onStart() {
//start handler as activity become visible

        h.postDelayed(new Runnable() {
            public void run() {
                new Downloaderlist(Imhere.this, "http://api2.randon.ili-studios.tn/allra.php?code="+codeme, l).execute();

                runnable = this;

                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onStart();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
}
