package com.example.yassine.as;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class Map_offline extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationSource.OnLocationChangedListener {


    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    Marker mCurrLocation;
    static final Integer LOCATION = 0x1;
    PendingResult<LocationSettingsResult> result;


    boolean GPS = false;
    String GPSprovider = "null";
    double mapCenterLat = 0;
    double mapCenterLon = 0;
    double OLDmapCenterLat = 0;
    double OLDmapCenterLon = 0;
    LinearLayout slide;
    Button out, photo, info;
    private static final int CAMERA_REQUEST = 1888;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA};
    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 6;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    String nameme, codeme, namep;
    String empo;
    double latitude, longitude;
    String distanceorg, durationorg;
    ArrayList<LatLng> markerPoints;
    private ArrayList<LatLng> arrayPoints;
    private PolylineOptions polylineOptions;
    LatLng ert;
    int i = 0;
    Bitmap imageBitmap;
    private int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_offline);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();

        nameme = intent.getExtras().getString("me");
        BackGroundRandoPart jh = new BackGroundRandoPart();
        jh.execute(nameme);
        BackGroundemp ee = new BackGroundemp();
        ee.execute(codeme);

        slide = (LinearLayout) findViewById(R.id.slide);
        slide.setVisibility(View.GONE);

        out = (Button) findViewById(R.id.out);
        photo = (Button) findViewById(R.id.photo);
        info = (Button) findViewById(R.id.info);

        out.setOnClickListener(this);
        photo.setOnClickListener(this);
        info.setOnClickListener(this);
        markerPoints = new ArrayList<>();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        if (mMap != null) {

            askForGPS();
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMapClickListener(this);
            mMap.setOnMapLongClickListener(this);
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {

                    return null;
                }

                @Override
                public View getInfoContents(final Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.infomap, null);

                    ImageView remo = (ImageView) v.findViewById(R.id.remove);
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
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    if (marker.getTitle().startsWith("Photo")) {
                        GetXMLTask task = new GetXMLTask();
                        // Execute the task
                        task.execute(new String[]{"http://api2.randon.ili-studios.tn/uploads/" + marker.getTitle().toString() + ".png"});

                    }
                }
            });


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

//remove previous current location marker and add new one at current position
                        if (mCurrLocation != null) {
                            mCurrLocation.remove();
                        }
                        Drawable circleDrawable = getResources().getDrawable(R.drawable.m1);
                        BitmapDescriptor markerIconn = getMarkerIconFromDrawable(circleDrawable);

                        Drawable circleDrawablee = getResources().getDrawable(R.drawable.guidde);
                        BitmapDescriptor guidde = getMarkerIconFromDrawable(circleDrawablee);


                        latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        // markerOptions.icon(markerIcon);
                        markerOptions.icon(markerIconn);
                        mCurrLocation = mMap.addMarker(markerOptions);
                        //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));


                        BackGroundemp ee = new BackGroundemp();
                        ee.execute(codeme);

                        new Downloader(Map_offline.this, "http://api2.randon.ili-studios.tn/allmarkers.php?code=" + codeme + "&nom=" + nameme, mMap).execute();


                        new DownloaderPhoto(Map_offline.this, "http://api2.randon.ili-studios.tn/allphoto.php?code=" + codeme + "&usr=" + nameme, mMap).execute();


                        BackGroundMapDyn d = new BackGroundMapDyn(Map_offline.this);
                        d.execute(nameme, codeme, removeChar(latLng.toString()), distanceorg); //Float.toString(distanceInMeters)


                        // Toast.makeText(Map_offline.this, String.valueOf(distanceInMeters) + " Meter", Toast.LENGTH_SHORT).show();
                        //    Toast.makeText(Map_offline.this, String.valueOf(distFrom(latitude, longitude, arg0.getLatitude(), arg0.getLongitude())) + " Meter", Toast.LENGTH_SHORT).show();

                        //If you only need one location, unregister the listener
                        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

                    }
                });

            }

        }
    }


    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "BuildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.out:
                slide.setVisibility(View.VISIBLE);
                break;
            case R.id.info:
                markerPoints.add(ert);
                markerPoints.add(mCurrLocation.getPosition());

                String url = getDirectionsUrl(mCurrLocation.getPosition(), ert);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
                break;
            case R.id.photo:
                if (checkPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.
                    takeImageFromCamera(v);
                }
                break;

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().toString().contains("Photo")) {
            BackGroundacphoto oiul = new BackGroundacphoto(Map_offline.this);
            oiul.execute(nameme, marker.getTitle().toString());
        }else{        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        }


        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        slide.setVisibility(View.GONE);


    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Drawable circleDrawable = getResources().getDrawable(R.drawable.srr);
        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);

        MarkerOptions markerOptions =
                new MarkerOptions().position(latLng).title("Favorite Place!").icon(markerIcon);
        markerOptions.draggable(true);

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 100));

    }

    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(Map_offline.this, LOCATION);

                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                    case LocationSettingsStatusCodes.CANCELED:
                        break;
                }

            }
        });
    }


    private void askForPermission(String permission, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(Map_offline.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Map_offline.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Map_offline.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Map_offline.this, new String[]{permission}, requestCode);
            }
        } else {
            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(Map_offline.this, new String[]{permission}, requestCode);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
                    askForGPS();
                    break;
                case CAMERA_REQUEST:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permissions granted.
                    } else {
                        // no permissions granted.
                    }
                    return;


            }
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    void UPDATEgps() {
        GPSTracker gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            if (gps.getPROVIDERgps().equals("null") == true) GPS = false;
            else GPS = true;

            GPSprovider = gps.getPROVIDERgps();

            OLDmapCenterLat = mapCenterLat;
            OLDmapCenterLon = mapCenterLon;

            mapCenterLat = gps.getLatitude();
            mapCenterLon = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        if (OLDmapCenterLat != mapCenterLat || OLDmapCenterLon != mapCenterLon) {
            Toast.makeText(this, "Lat: " + mapCenterLat + "\nLon: " + mapCenterLon, Toast.LENGTH_LONG).show();

        }
    }


    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

/*
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            mMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocation = mMap.addMarker(markerOptions);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        }

        */
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "onConnectionSuspended", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {
/*
        //remove previous current location marker and add new one at current position
        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        // markerOptions.icon(markerIcon);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocation = mMap.addMarker(markerOptions);
        //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));

        Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this); */
    }


    public void takeImageFromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            // img.setImageBitmap(mphoto);


            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.SECOND);
            i++;

            namep = "Photo" + nameme + String.valueOf(seconds) + String.valueOf(i);


            // img.setImageBitmap(mphoto);

          /*  ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byte[] byte_arr = stream.toByteArray();
            String encodedString = Base64.encodeToString(byte_arr, 0);*/


            upload("http://api2.randon.ili-studios.tn/ImageUpload.php?code=" + codeme + "&usr=" + nameme + "&namep=" + namep + "&pos=" + removeChar(mCurrLocation.getPosition().toString()), "POST", getEncoded64ImageStringFromBitmap(imageBitmap));

        }


    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CAMERA_REQUEST);
            return false;
        }
        return true;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// DataBase

    class BackGroundRandoPart extends AsyncTask<String, String, String> {
        String Codes;

        @Override
        protected String doInBackground(String... params) {


            String aa = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/affichecodepart.php?nom=" + aa);
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


            } catch (JSONException e) {
                e.printStackTrace();
            }
            codeme = Codes;
        }
    }


    class BackGroundemp extends AsyncTask<String, String, String> {
        String emporg;

        @Override
        protected String doInBackground(String... params) {


            String aa = params[0];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/returnempo.php?code=" + aa);
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
                emporg = user_data.getString("emp");
                String Nomo = user_data.getString("Nomo");

                Drawable circleDrawablee = getResources().getDrawable(R.drawable.guidde);
                BitmapDescriptor guidde = getMarkerIconFromDrawable(circleDrawablee);
                String[] latLngg = emporg.split(",");

                latitude = Double.parseDouble(latLngg[0]);
                longitude = Double.parseDouble(latLngg[1]);
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("The Organizer").snippet(Nomo).icon(guidde));
                ert = new LatLng(latitude, longitude);

                String url = getDirectionsUrl(mCurrLocation.getPosition(), ert);

                DownloadTaskk downloadTask = new DownloadTaskk();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static String removeChar(String str) {
        return str.substring(10, str.length() - 1);
    }

    private static String remove(String str) {
        if (str.contains("m")) {
            return str.substring(0, str.length() - 1);
        }
        return str.substring(0, str.length() - 2);

    }


    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);

        return -1;
    }

    private String getDirection(LatLng my_latlong, LatLng frnd_latlong) {
        // TODO Auto-generated method stub
        double my_lat = my_latlong.latitude;
        double my_long = my_latlong.longitude;

        double frnd_lat = frnd_latlong.latitude;
        double frnd_long = frnd_latlong.longitude;

        double radians = getAtan2((frnd_long - my_long), (frnd_lat - my_lat));
        double compassReading = radians * (180 / Math.PI);

        String[] coordNames = {"North", "North-East", "East", "South-East", "South", "South-West", "West", "North-West", "North"};
        int coordIndex = (int) Math.round(compassReading / 45);

        if (coordIndex < 0) {
            coordIndex = coordIndex + 8;
        }
        ;

        return coordNames[coordIndex]; // returns the coordinate value
    }

    private double getAtan2(double longi, double lat) {
        return Math.atan2(longi, lat);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// meter

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

    /**
     * A method to download json data from url
     */
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


    // Fetches data from url passed
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

    /**
     * A class to parse the Google Places in JSON format
     */
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
                lineOptions.color(Color.RED);

            }


            distanceorg = remove(distance);
            durationorg = duration;

            //  Toast.makeText(Map_offline.this,distance , Toast.LENGTH_SHORT).show();

            Toast.makeText(Map_offline.this, duration, Toast.LENGTH_SHORT).show();
            Toast.makeText(Map_offline.this, distance, Toast.LENGTH_SHORT).show();

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);

        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// unique distance


    private class DownloadTaskk extends AsyncTask<String, Void, String> {

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

            ParserTaskk parserTask = new ParserTaskk();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTaskk extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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


            }


            distanceorg = remove(distance);

            //  Toast.makeText(Map_offline.this,distance , Toast.LENGTH_SHORT).show();


            // Drawing polyline in the Google Map for the i-th route

        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    public void upload(String Url, String method, String imageString) {
        new AsyncTask<String, String, String>() {
            String method, imageString;
            int tmp;
            String data = "";

            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    method = params[1];
                    String urlParams = params[2];

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod(method);

                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setAllowUserInteraction(false);

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
            protected void onPostExecute(String msg) {
                Toast.makeText(Map_offline.this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }.execute(Url, method, imageString);
    }


    class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        int i = 1;

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
            i++;
            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.SECOND);
            MediaStore.Images.Media.insertImage(getContentResolver(), result, "Photo" + String.valueOf(i) + String.valueOf(seconds), "Picture tooked on a rando!");
            Toast.makeText(Map_offline.this,"Success picture download",Toast.LENGTH_SHORT).show();

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
        }
    }

    class BackGroundacphoto extends AsyncTask<String, String, String> {
        Context cx;
        String nameusr,namepho;

        public BackGroundacphoto(Context cx) {
            this.cx = cx;
        }

        @Override
        protected String doInBackground(String... params) {


            nameusr = params[0];
            namepho = params[1];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/accessphoto.php?usr=" + nameusr+"&namep="+namepho);
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

            if (s.startsWith("works1!")) {

                Toast.makeText(cx, "You changed the access type of the picture to private", Toast.LENGTH_LONG).show();

            }
            else if(s.startsWith("works2!"))
            {                Toast.makeText(cx, "You changed the access type of the picture to public", Toast.LENGTH_LONG).show();
            }
            else
            {                           Toast.makeText(cx, s, Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}