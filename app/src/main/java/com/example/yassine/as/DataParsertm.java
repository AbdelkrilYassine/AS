package com.example.yassine.as;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yassine on 04/05/2017.
 */


public class DataParsertm extends AsyncTask<Void, Void, Boolean> {
    Context c;
    String jsonData;
    private PolylineOptions polylineOptions;
    GoogleMap map;
    private ArrayList<LatLng> arrayPoints= new ArrayList<>();;
    ArrayList<String> spacecrafts = new ArrayList<>();

    public DataParsertm(Context c, String jsonData, GoogleMap map,ArrayList<LatLng> arrayPoints) {
        this.c = c;
        this.jsonData = jsonData;
        this.map = map;
        this.arrayPoints=arrayPoints;
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


        if (result) {
            for (int i = 0; i < spacecrafts.size(); i++) {
                String rr = spacecrafts.get(i);

                String[] latLng = rr.split(",");

                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                LatLng location = new LatLng(latitude, longitude);

                map.addMarker(new MarkerOptions().position(location).title("Place " + i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                addLines(location);


            }
            addLines(arrayPoints.get(0));
        }
    }

    private Boolean parseData() {
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo;
            spacecrafts.clear();
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String name = jo.getString("place");

                spacecrafts.add(name);
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

    private void addLines(LatLng latLng) {

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.CYAN);
        polylineOptions.width(10);
        arrayPoints.add(latLng);
        polylineOptions.addAll(arrayPoints);
        map.addPolyline(polylineOptions);
    }

}
