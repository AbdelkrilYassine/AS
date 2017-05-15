package com.example.yassine.as;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by yassine on 14/04/2017.
 */

public class DataParser extends AsyncTask<Void, Void, Boolean> {
    Context c;
    String jsonData;
    GoogleMap map;
    ArrayList<Randonneur> spacecrafts = new ArrayList<>();

    public DataParser(Context c, String jsonData, GoogleMap map) {
        this.c = c;
        this.jsonData = jsonData;
        this.map = map;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        Drawable circleDrawable = c.getResources().getDrawable(R.drawable.autres);
        BitmapDescriptor markerIconn = getMarkerIconFromDrawable(circleDrawable);

        if (result) {
            for (int i = 0; i < spacecrafts.size(); i++) {
                Randonneur rr=spacecrafts.get(i);

                String[] latLng = rr.getEmp().split(",");

                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                LatLng location = new LatLng(latitude, longitude);

                map.addMarker(new MarkerOptions().position(location).title(rr.getNompart()).snippet(rr.getDistance()).icon(markerIconn));


            }
            }
    }

    private Boolean parseData() {
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo;
            spacecrafts.clear();
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String code = jo.getString("CodeRa");
                String name = jo.getString("usr");
                String emp = jo.getString("emplacement");
                String distance = jo.getString("distance");
                spacecrafts.add(new Randonneur(code,name,emp,distance));
/*
                String[] latLng = emp.split(",");

                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                LatLng location = new LatLng(latitude, longitude);*/

              //  map.addMarker(new MarkerOptions().position(sydney).title(name));



            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
