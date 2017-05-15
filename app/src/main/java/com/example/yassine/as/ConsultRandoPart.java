package com.example.yassine.as;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConsultRandoPart extends AppCompatActivity {
    TextView datee, lieu, nb, ldep, larr, hdep, harr, duree, distance, nom, mode, difficulte,tel;
    String Tdatee, Tlieu, Tnb, Tldep, Tlarr, Thdep, Tharr, Tduree, Tdistance, Tnom, Tmode, Tdifficulte,Tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_rando_part);

        datee = (TextView) findViewById(R.id.datee);
        lieu = (TextView) findViewById(R.id.lieu);
        nb = (TextView) findViewById(R.id.nbb);
        ldep = (TextView) findViewById(R.id.ldep);
        larr = (TextView) findViewById(R.id.larr);
        hdep = (TextView) findViewById(R.id.hdep);
        harr = (TextView) findViewById(R.id.harr);
        duree = (TextView) findViewById(R.id.duree);
        distance = (TextView) findViewById(R.id.distance);
        nom = (TextView) findViewById(R.id.nomr);
        mode = (TextView) findViewById(R.id.mode);
        difficulte = (TextView) findViewById(R.id.difficulte);
        tel = (TextView) findViewById(R.id.teul);


        Intent intent = getIntent();

        Tdatee = intent.getExtras().getString("date");
        Tlieu = intent.getExtras().getString("lieu");
        Tnb = intent.getExtras().getString("nb");
        Tldep = intent.getExtras().getString("ldep");
        Tlarr = intent.getExtras().getString("larr");
        Thdep = intent.getExtras().getString("hdep");
        Tharr = intent.getExtras().getString("harr");
        Tduree = intent.getExtras().getString("duree");
        Tdistance = intent.getExtras().getString("distance");
        Tnom = intent.getExtras().getString("nom");
        Tmode = intent.getExtras().getString("mode");
        Tdifficulte =intent.getExtras().getString("difficulte");
        Tel =intent.getExtras().getString("tel");

        datee.setText(Tdatee);
        lieu.setText(Tlieu);
        nb.setText(Tnb);
        ldep.setText(Tldep);
        larr.setText(Tlarr);
        hdep.setText(Thdep);
        harr.setText(Tharr);
        duree.setText(Tduree);
        distance.setText(Tdistance);
        nom.setText(Tnom);
        mode.setText(Tmode);
        difficulte.setText(Tdifficulte);
        tel.setText(Tel);
        this.setTitle("Info Randon");

    }
}
