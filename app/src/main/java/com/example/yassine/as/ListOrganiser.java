package com.example.yassine.as;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yassine.as.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ListOrganiser extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {
    private TextView tvDay, tvHour, tvMinute, tvSecond, tvEvent;
    private LinearLayout linearLayout1, linearLayout2;
    private Handler handler;
    private Runnable runnable;
    private GoogleMap mMapp;
    String nameme, Codes;
    Button consultme, modidata, modipath, cancelme;
    String Date, Lieu, NBparticipant, LieuDep, LieuArr, HeureDep, HeureArr, Duree, Distance, Nom, Tel, Difficulte, Mode, Origin, Dest;
    ;
    TextView affcode;
    private ArrayList<LatLng> arrayPoints= new ArrayList<>();
    private PolylineOptions polylineOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_organiser);
        Intent intent = getIntent();

        nameme = intent.getExtras().getString("me");
        //  codeme = intent.getExtras().getString("code");
        BackGroundRandoOrg yo = new BackGroundRandoOrg();
        yo.execute(nameme);


        this.setTitle("Organizer " + nameme);
        consultme = (Button) findViewById(R.id.consultme);
        modidata = (Button) findViewById(R.id.modidata);
        modipath = (Button) findViewById(R.id.modipath);
        cancelme = (Button) findViewById(R.id.cancelme);
        consultme.setOnClickListener(this);
        modidata.setOnClickListener(this);
        modipath.setOnClickListener(this);
        cancelme.setOnClickListener(this);
        affcode = (TextView) findViewById(R.id.affcode);
        // affcode.setText(codeme);

// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);
        initUI();
        countDownStart();
    }

    @SuppressLint("SimpleDateFormat")
    private void initUI() {
        tvDay = (TextView) findViewById(R.id.txtTimerDay);
        tvHour = (TextView) findViewById(R.id.txtTimerHour);
        tvMinute = (TextView) findViewById(R.id.txtTimerMinute);
        tvSecond = (TextView) findViewById(R.id.txtTimerSecond);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapp = googleMap;

        if (mMapp != null) {
            mMapp.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {

                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.infomap, null);

                    TextView locca = (TextView) v.findViewById(R.id.locality);
                    TextView latt = (TextView) v.findViewById(R.id.lat);
                    TextView lngg = (TextView) v.findViewById(R.id.lng);
                    TextView snippett = (TextView) v.findViewById(R.id.snippet);
                    LatLng ll = marker.getPosition();
                    locca.setText(marker.getTitle());
                    latt.setText("Latitude: " + ll.latitude);
                    lngg.setText("Longitude: " + ll.longitude);
                    snippett.setText(marker.getSnippet());

                    return v;
                }


            });

            mMapp.setOnMapClickListener(this);
            mMapp.setOnMapLongClickListener(this);
            mMapp.setOnMarkerDragListener(this);


        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cancelme:
                BackGroundAnnulerRando gh = new BackGroundAnnulerRando(ListOrganiser.this);
                gh.execute(Codes, nameme);
                break;
            case R.id.consultme:
                Intent s = new Intent(ListOrganiser.this, ConsultRandoPart.class);
                s.putExtra("date", Date);
                s.putExtra("lieu", Lieu);
                s.putExtra("ldep", LieuDep);
                s.putExtra("larr", LieuArr);
                s.putExtra("hdep", HeureDep);
                s.putExtra("harr", HeureArr);
                s.putExtra("duree", Duree);
                s.putExtra("distance", Distance);
                s.putExtra("mode", Mode);
                s.putExtra("difficulte", Difficulte);
                s.putExtra("nom", Nom);
                s.putExtra("tel", Tel);
                s.putExtra("nb", NBparticipant);


                startActivity(s);
                break;
            case R.id.modidata:
                Intent ss = new Intent(ListOrganiser.this, Mdrando.class);
                ss.putExtra("nb", NBparticipant);

                ss.putExtra("code", Codes);
                ss.putExtra("date", Date);
                ss.putExtra("lieu", Lieu);
                ss.putExtra("ldep", LieuDep);
                ss.putExtra("larr", LieuArr);
                ss.putExtra("hdep", HeureDep);
                ss.putExtra("harr", HeureArr);
                ss.putExtra("duree", Duree);
                ss.putExtra("distance", Distance);
                ss.putExtra("mode", Mode);
                ss.putExtra("difficulte", Difficulte);
                ss.putExtra("nom", Nom);
                ss.putExtra("tel", Tel);
                startActivity(ss);
                break;
            case R.id.modipath:
                Intent ssp = new Intent(ListOrganiser.this, ModifyTrajet.class);
                ssp.putExtra("code", Codes);
                break;

        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(ListOrganiser.this,
                "onMapClick:\n" + latLng.latitude + " : " + latLng.longitude,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    // //////////////COUNT DOWN START/////////////////////////
    public void countDownStart() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    // Here Set your Event Date
                    Date eventDate = dateFormat.parse(Date);
                    Date currentDate = new Date();
                    if (!currentDate.after(eventDate)) {
                        long diff = eventDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        tvDay.setText("" + String.format("%02d", days));
                        tvHour.setText("" + String.format("%02d", hours));
                        tvMinute.setText("" + String.format("%02d", minutes));
                        tvSecond.setText("" + String.format("%02d", seconds));
                    } else {

                        handler.removeCallbacks(runnable);
                        // handler.removeMessages(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    // //////////////COUNT DOWN END/////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menumap, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemsatelite:

                MediaPlayer mp = MediaPlayer.create(this, R.raw.soundmenu);
                mp.start();
                mMapp.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


                return true;
            case R.id.itemnormale:
                MediaPlayer mp1 = MediaPlayer.create(this, R.raw.soundmenu);
                mp1.start();
                mMapp.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                return true;

            case R.id.itemterrain:
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.soundmenu);
                mp2.start();
                mMapp.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                return true;
            case R.id.itemhybrid:
                MediaPlayer mp3 = MediaPlayer.create(this, R.raw.soundmenu);
                mp3.start();
                mMapp.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                return true;
            case R.id.itemnotype:
                MediaPlayer mp4 = MediaPlayer.create(this, R.raw.soundmenu);
                mp4.start();
                mMapp.setMapType(GoogleMap.MAP_TYPE_NONE);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class BackGroundRandoOrg extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {


            String aa = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/affichercodeorg.php?nom=" + aa);
                String urlParams = "";

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                Codes = user_data.getString("Code");
                Date = user_data.getString("Date");
                Lieu = user_data.getString("Lieu");
                NBparticipant = user_data.getString("NBparticipant");
                LieuDep = user_data.getString("LieuDep");
                LieuArr = user_data.getString("LieuArr");
                HeureDep = user_data.getString("HeureDep");
                HeureArr = user_data.getString("HeureArr");
                Duree = user_data.getString("Duree");
                Distance = user_data.getString("Distance");
                Nom = user_data.getString("Nom");
                Tel = user_data.getString("Tel");
                Difficulte = user_data.getString("Difficulte");
                Mode = user_data.getString("Mode");
                Origin = user_data.getString("Origin");
                Dest = user_data.getString("Dest");

                String[] latLng = Origin.split(",");

                double latitude = Double.parseDouble(latLng[0]);
                double longitude = Double.parseDouble(latLng[1]);
                LatLng locationy = new LatLng(latitude, longitude);

                mMapp.addMarker(new MarkerOptions().position(locationy).title("Origin Address").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                String[] latLng2 = Dest.split(",");

                double latitude2 = Double.parseDouble(latLng2[0]);
                double longitude2 = Double.parseDouble(latLng2[1]);
                LatLng locationy2 = new LatLng(latitude2, longitude2);

                mMapp.addMarker(new MarkerOptions().position(locationy2).title("Destination Address").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                addLines(locationy2);

                new Donwloadertm(ListOrganiser.this, "http://api2.randon.ili-studios.tn/affichetrajetm.php?code=" + Codes, mMapp, arrayPoints).execute();

                String url = getDirectionsUrl(locationy2, locationy);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            affcode.setText(Codes);
        }
    }


/*
    @Override
    public void onBackPressed() {
        Intent BackpressedIntent = new Intent();
        BackpressedIntent .setClass(getApplicationContext(),Test.class);
        BackpressedIntent .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        BackpressedIntent.putExtra("usernametest",nameme);
        startActivity(BackpressedIntent );
        finish();
    }*/


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////R

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception downloading", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";


            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }


            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.MAGENTA);

            }
            //  TextView distancen = (TextView) findViewById(R.id.tvDistance);
            //  TextView durationn = (TextView) findViewById(R.id.tvDuration);


            //    distancen.setText(distance);
            //    durationn.setText(duration);

            // Drawing polyline in the Google Map for the i-th route
            mMapp.addPolyline(lineOptions);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
        /*
         * for (Marker marker : markers) {
         * builder.include(marker.getPosition()); }
         */
            for (LatLng point : points) {
                builder.include(point);
            }

            LatLngBounds bounds = builder.build();
            int padding = 20; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                    padding);
            mMapp.moveCamera(cu);
            mMapp.animateCamera(cu, 2000, null);
        }
    }

    private void addLines(LatLng latLng) {

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.CYAN);
        polylineOptions.width(10);
        arrayPoints.add(latLng);
        polylineOptions.addAll(arrayPoints);
        mMapp.addPolyline(polylineOptions);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}


