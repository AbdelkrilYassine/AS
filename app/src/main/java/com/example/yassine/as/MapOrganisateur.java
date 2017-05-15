package com.example.yassine.as;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapOrganisateur extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {


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
    Button out, photo, info, appel;
    private static final int CAMERA_REQUEST = 1888;
    String[] permissions = new String[]{
            android.Manifest.permission.CAMERA};
    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 6;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    String nameme, codeme, namep,Dest;
    int i = 0;
    Bitmap imageBitmap;

    private int PICK_IMAGE_REQUEST = 1;
    private ArrayList<LatLng> arrayPoints= new ArrayList<>();
    private PolylineOptions polylineOptions;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_organisateur);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapo);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();

        nameme = intent.getExtras().getString("me");
        BackGroundRandoOrg er = new BackGroundRandoOrg();
        er.execute(nameme);

        slide = (LinearLayout) findViewById(R.id.slidee);
        slide.setVisibility(View.GONE);

        out = (Button) findViewById(R.id.outt);
        photo = (Button) findViewById(R.id.photoo);
        info = (Button) findViewById(R.id.infoo);
        appel = (Button) findViewById(R.id.appel);

        out.setOnClickListener(this);
        photo.setOnClickListener(this);
        info.setOnClickListener(this);
        appel.setOnClickListener(this);
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

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(-33.865143, 151.209900))
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.signthat))));

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
                    if (marker.getTitle().toString().startsWith("Photo")) {
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
                        Drawable circleDrawable = getResources().getDrawable(R.drawable.guidde);
                        BitmapDescriptor markerIconn = getMarkerIconFromDrawable(circleDrawable);
                        latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        // markerOptions.icon(markerIcon);
                        markerOptions.icon(markerIconn);
                        mCurrLocation = mMap.addMarker(markerOptions);

                        new Downloader(MapOrganisateur.this, "http://api2.randon.ili-studios.tn/allmarkers.php?code=" + codeme + "&nom=" + nameme, mMap).execute();
                        BackGroundSafep se = new BackGroundSafep(MapOrganisateur.this);
                        se.execute(nameme, codeme, removeChar(latLng.toString()));

                        new DownloaderPhoto(MapOrganisateur.this, "http://api2.randon.ili-studios.tn/allphoto.php?code=" + codeme + "&usr=" + nameme, mMap).execute();
                        new BackGroundAlert(MapOrganisateur.this).execute(codeme);

                        //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));

                        //    Toast.makeText(MapOrganisateur.this, "Location Changed", Toast.LENGTH_SHORT).show();

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
            case R.id.outt:
                slide.setVisibility(View.VISIBLE);
                break;
            case R.id.infoo:
                new Donwloadertm(MapOrganisateur.this, "http://api2.randon.ili-studios.tn/affichetrajetm.php?code=" + codeme, mMap, arrayPoints).execute();

                break;
            case R.id.photoo:
                if (checkPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.
                    takeImageFromCamera(v);
                }
                break;

            case R.id.appel:
                BackGroundAppel app = new BackGroundAppel(MapOrganisateur.this);
                app.execute(nameme, codeme);
                break;

        }


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().toString().contains("Photo")) {
            BackGroundAccessphoto oiul = new BackGroundAccessphoto(MapOrganisateur.this);
            oiul.execute(nameme, marker.getTitle().toString());
        } else {
            Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

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
                            status.startResolutionForResult(MapOrganisateur.this, LOCATION);

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

        if (ContextCompat.checkSelfPermission(MapOrganisateur.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapOrganisateur.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MapOrganisateur.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MapOrganisateur.this, new String[]{permission}, requestCode);
            }
        } else {
            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MapOrganisateur.this, new String[]{permission}, requestCode);

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

    public void takeImageFromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            // img.setImageBitmap(mphoto);

          /*  ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byte[] byte_arr = stream.toByteArray();
            String encodedString = Base64.encodeToString(byte_arr, 0);*/


            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.SECOND);
            i++;

            namep = "Photo" + nameme + String.valueOf(seconds) + String.valueOf(i);


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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class BackGroundRandoOrg extends AsyncTask<String, String, String> {
        String Codes;

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
                Dest = user_data.getString("Dest");



            } catch (JSONException e) {
                e.printStackTrace();
            }
            codeme = Codes;
            String[] latLng2 = Dest.split(",");

            double latitude2 = Double.parseDouble(latLng2[0]);
            double longitude2 = Double.parseDouble(latLng2[1]);
            LatLng locationy2= new LatLng(latitude2, longitude2);

            mMap.addMarker(new MarkerOptions().position(locationy2).title("Destination Address"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationy2));

            addLines(locationy2);
        }
    }

    private static String removeChar(String str) {
        return str.substring(10, str.length() - 1);
    }

    private void addLines(LatLng latLng) {

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.CYAN);
        polylineOptions.width(10);
        arrayPoints.add(latLng);
        polylineOptions.addAll(arrayPoints);
        mMap.addPolyline(polylineOptions);
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
                Toast.makeText(MapOrganisateur.this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }.execute(Url, method, imageString);
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
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
            Toast.makeText(MapOrganisateur.this, "Success picture download", Toast.LENGTH_SHORT).show();
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


    class BackGroundAccessphoto extends AsyncTask<String, String, String> {
        Context cx;
        String nameusr, namepho;

        public BackGroundAccessphoto(Context cx) {
            this.cx = cx;
        }

        @Override
        protected String doInBackground(String... params) {


            nameusr = params[0];
            namepho = params[1];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/accessphoto.php?usr=" + nameusr + "&namep=" + namepho);
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

            } else if (s.startsWith("works2!")) {
                Toast.makeText(cx, "You changed the access type of the picture to public", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}



