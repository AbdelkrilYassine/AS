package com.example.yassine.as;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by yassine on 09/05/2017.
 */

public class DataParserPhoto extends AsyncTask<Void, Void, Boolean> {
    Context c;
    String jsonData;
    GoogleMap map;
    ArrayList<Photoo> spacecrafts = new ArrayList<>();
     static Bitmap bi;

    public DataParserPhoto(Context c, String jsonData, GoogleMap map) {
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

      /*  Drawable circleDrawable = c.getResources().getDrawable(R.drawable.dds);
        BitmapDescriptor markerIconn = getMarkerIconFromDrawable(circleDrawable);*/

        if (result) {
            for (int i = 0; i < spacecrafts.size(); i++) {
                Photoo rr = spacecrafts.get(i);

                String[] latLng = rr.getPos().split(",");

                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                LatLng location = new LatLng(latitude, longitude);

                GetXMLTask task = new GetXMLTask();
                // Execute the task
                task.execute(new String[]{rr.getPath()});
                if (bi!=null)
                map.addMarker(new MarkerOptions().position(location).title(rr.getNom()).snippet("Taken by " + rr.getUsr()).icon(BitmapDescriptorFactory.fromBitmap(bi)));


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
                String usr = jo.getString("usr");
                String namep = jo.getString("namep");
                String path = jo.getString("path");
                String pos = jo.getString("pos");

                spacecrafts.add(new Photoo(namep, path, pos, usr));
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
}

    class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            //  imageView.setImageBitmap(result);
            DataParserPhoto.bi = result;
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }}



