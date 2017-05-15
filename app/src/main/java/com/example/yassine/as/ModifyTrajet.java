package com.example.yassine.as;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.URLEncoder;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SimpleAdapter;

public class ModifyTrajet extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    static final Integer LOCATION = 0x1;
    GoogleApiClient client;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    Button confirmm, cancell;
    EditText lieu, hdep, datte, distancepp, hrr, lieudep, nbplace, hre;
    private Pattern pattern;
    private Matcher matcher;
    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543,
            -73.998585);
    private static final LatLng TIMES_SQUARE = new LatLng(40.7577, -73.9857);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);

    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    private ArrayList<LatLng> arrayPoints;
    private PolylineOptions polylineOptions;
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyBr9dRIraIsPXF3R6DJ1ScEXjD-6ZjE_-k";

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Button btnFindPath;
    private AutoCompleteTextView etOrigin;
    private AutoCompleteTextView etDestination;
    ArrayList<LatLng> markerPoints;
    AutoCompleteTextView atvPlaces;
    PlacesTaskauto placesTaskauto;
    ParserTaskauto parserTaskauto;
    List<Marker> listmarker;
    List<MarkerOptions> listgms, listgms2;
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
    private static char[] hextable = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    String chaine = null;
    List<Address> adrr, adm;
    List<String> list1, list2;
    List<String> tab1, tab2;
    String codehim;
//http://maps.googleapis.com/maps/api/directions/json?origin=paris&destination=london

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        pattern = Pattern.compile(TIME24HOURS_PATTERN);


        Intent intent = getIntent();

        codehim = intent.getExtras().getString("code");

      //  uoo = intent.getExtras().getString("usro");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();
        Button sear = (Button) findViewById(R.id.searchb);

        sear.setOnClickListener(this);
        confirmm = (Button) findViewById(R.id.confirmm);
        cancell = (Button) findViewById(R.id.cancell);
        confirmm.setOnClickListener(this);
        cancell.setOnClickListener(this);


        btnFindPath = (Button) findViewById(R.id.btnFindPath);

        etOrigin = (AutoCompleteTextView) findViewById(R.id.etOrigin);
        etOrigin.setThreshold(1);
        etOrigin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (placesTaskauto != null) {
                    Log.i("--placesDownloadTask--", "progress_status : " + placesTaskauto.getStatus());
                    placesTaskauto.cancel(true);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                placesTaskauto = new ModifyTrajet.PlacesTaskauto();
                placesTaskauto.execute(s.toString());
            }
        });

        etDestination = (AutoCompleteTextView) findViewById(R.id.etDestination);
        etDestination.setThreshold(1);
        etDestination.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTaskauto = new ModifyTrajet.PlacesTaskauto();
                placesTaskauto.execute(s.toString());

                if (placesTaskauto != null) {
                    Log.i("--placesDownloadTask--", "progress_status : " + placesTaskauto.getStatus());
                    placesTaskauto.cancel(true);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                placesTaskauto = new ModifyTrajet.PlacesTaskauto();
                placesTaskauto.execute(s.toString());
            }
        });

        btnFindPath.setOnClickListener(this);

        atvPlaces = (AutoCompleteTextView) findViewById(R.id.loc);
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //placesTaskauto = new MapsActivity.PlacesTaskauto();
                //placesTaskauto.execute(s.toString());
                if (placesTaskauto != null) {
                    Log.i("--placesDownloadTask--", "progress_status : " + placesTaskauto.getStatus());
                    placesTaskauto.cancel(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                placesTaskauto = new ModifyTrajet.PlacesTaskauto();
                placesTaskauto.execute(s.toString());


            }
        });


    }
/*
        lieu = (EditText) findViewById(R.id.lieu);
        hdep = (EditText) findViewById(R.id.hdep);
        datte = (EditText) findViewById(R.id.datte);
        distancepp = (EditText) findViewById(R.id.dparr);
        hrr = (EditText) findViewById(R.id.harr);
        lieudep = (EditText) findViewById(R.id.lieudep);
        nbplace = (EditText) findViewById(R.id.nbplace);
        hre = (EditText) findViewById(R.id.hre);
*/


//**********************************************************************************************************


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


        if (mMap != null) {


            listmarker = new ArrayList<Marker>();
            listgms = new ArrayList<MarkerOptions>();
            listgms2 = new ArrayList<MarkerOptions>();
            adrr = new ArrayList<Address>();
            adm = new ArrayList<Address>();
            list1 = new ArrayList<String>();
            list2 = new ArrayList<String>();
            tab1 = new ArrayList<String>();
            tab2 = new ArrayList<String>();
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
                    remo.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //  marker.remove();

                        }
                    });


                    return v;
                }


            });

        }


        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
        arrayPoints = new ArrayList<LatLng>();

        //********************************************************************************************************************

/*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(arg0.getLatitude(), arg0.getLongitude()));
                    circleOptions.radius(1000);
                    mMap.addCircle(circleOptions);
                    //    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 12.0f));
                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 12.0f));


                }
            });

        }


    /*    new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }, 0, 10000);*/


    }


    private void askForPermission(String permission, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(ModifyTrajet.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ModifyTrajet.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am  just asking the permission again

                ActivityCompat.requestPermissions(ModifyTrajet.this, new String[]{permission}, requestCode);


            } else {


                ActivityCompat.requestPermissions(ModifyTrajet.this, new String[]{permission}, requestCode);

            }
        } else {

            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(ModifyTrajet.this, new String[]{permission}, requestCode);

        }
    }

    //*************************************************************************************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:

                    askForGPS();
                    break;

            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();

    }


    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(ModifyTrajet.this, LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {


        List<Address> addressList4 = null;
        List<Address> addressList5 = null;
        List<Address> addressList6 = null;

        // EditText loc = (EditText) findViewById(R.id.loc);


        switch (v.getId()) {
            case R.id.searchb:

                String loca = atvPlaces.getText().toString();

                if (loca.isEmpty() || loca.equals("")) {
                    Toast.makeText(getApplicationContext(), "Location is empty!", Toast.LENGTH_LONG).show();
                } else {
                    Geocoder geo = new Geocoder(this);
                    try {
                        addressList4 = geo.getFromLocationName(loca, 1);
                        Address address = addressList4.get(0);

                        LatLng iaLng = new LatLng(address.getLatitude(), address.getLongitude());
                        if (iaLng.latitude != 0 || iaLng.longitude != 0) {
                            Marker marker = mMap.addMarker(new MarkerOptions().position(iaLng).title(loca + "- " + address.getCountryName()).snippet(address.getPostalCode() + "- " + address.getLocality()).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                            //   mMap.animateCamera(CameraUpdateFactory.newLatLng(iaLng));
                            marker.isDraggable();
                            listmarker.add(marker);
                            adm.add(address);
                            list2.add(address.toString());
                            tab1.add(removeChar(iaLng.toString()));

                            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(iaLng,10);
                            mMap.animateCamera(yourLocation);

                            Toast.makeText(this, loca, Toast.LENGTH_LONG).show();

                            addLines(iaLng);
                        } else {
                            Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    } catch (Throwable ex) {
                        Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                    }


                }
                break;
            case R.id.cancell:


                mMap.clear();
                arrayPoints.clear();
                listmarker.clear();
                listgms.clear();
                listgms2.clear();
                adrr.clear();
                adm.clear();
                list1.clear();
                list2.clear();
                tab1.clear();
                tab2.clear();
                clearForm((ViewGroup) findViewById(R.id.father));

                break;
            case R.id.confirmm:

                if (listmarker.size() + listgms2.size() < 3) {
                    Toast.makeText(getApplicationContext(), "Minimum markers is 3,Draw your mountain Path", Toast.LENGTH_LONG).show();

                } else if (listgms.size() != 2) {
                    Toast.makeText(getApplicationContext(), "You have to choose Destination and origin to Draw the road path", Toast.LENGTH_LONG).show();
                } else {

                   new  BackGroundMDtrajetR(ModifyTrajet.this).execute(codehim,removeChar(tab2.get(0)),removeChar(tab2.get(1)));
                    for (int i = 0; i < tab1.size(); i++)
                    {
                        new BackGroundMDtrajetM(ModifyTrajet.this).execute(codehim,tab1.get(i));
                    }


                    /*  chaine = generatecode();


                    Intent maiin = new Intent(ModifyTrajet.this, ConfigMap.class);
                    maiin.putExtra("chaine", chaine);
                    //    maiin.putExtra("troute", (Parcelable) adrr);
                    //    maiin.putExtra("tm", (Parcelable) adm);


                    Bundle b = new Bundle();
                    b.putStringArrayList("pathroad", (ArrayList<String>) tab2);
                    b.putStringArrayList("pathmoun", (ArrayList<String>) tab1);
                    maiin.putExtras(b);

                    maiin.putExtra("usrme", uoo);


                    startActivity(maiin);*/
                }

                break;

            case R.id.btnFindPath:
                String dest = etDestination.getText().toString();
                String origin = etOrigin.getText().toString();


                if (origin.isEmpty() || dest.isEmpty() || origin.equals("") || dest.equals("")) {
                    Toast.makeText(getApplicationContext(), "Address Origin/Destination is empty!", Toast.LENGTH_LONG).show();

                } else if (!placed(dest) && !placeo(origin))

                {
                    Toast.makeText(getApplicationContext(), "Address Origin/Destination cannot be found!", Toast.LENGTH_LONG).show();
                } else if (!placed(dest))

                {
                    Toast.makeText(getApplicationContext(), "Address Destination cannot be found!", Toast.LENGTH_LONG).show();
                } else if (!placeo(origin))

                {
                    Toast.makeText(getApplicationContext(), "Address Origin cannot be found!", Toast.LENGTH_LONG).show();
                } else {

                    tri9(posplaced(dest), posplaceo(origin));

                }
        }

/*
                if (origin.isEmpty() || dest.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Address Origin/Destination is empty!", Toast.LENGTH_LONG).show();
                    break;

                } else if (!existplace(dest)) {
                    Toast.makeText(getBaseContext(), "No Destination found", Toast.LENGTH_SHORT).show();
                    break;

                } else if (!existplace(origin)) {
                    Toast.makeText(getBaseContext(), "No Origin found", Toast.LENGTH_SHORT).show();
                    break;

                }
                else if (!existplace(origin)&&!existplace(dest)) {
                    Toast.makeText(getBaseContext(), "No Destination & Origin found", Toast.LENGTH_SHORT).show();
                    break;

                }
                else

                {
                    Geocoder geo1 = new Geocoder(this);

                    try {
                        addressList5 = geo1.getFromLocationName(dest, 1);
                        Toast.makeText(this, dest, Toast.LENGTH_LONG).show();


                    } catch (IOException e) {
                        e.printStackTrace();

                    }


                    Address address1 = addressList5.get(0);
                    LatLng iaLng1 = new LatLng(address1.getLatitude(), address1.getLongitude());


                    Geocoder geo2 = new Geocoder(this);
                    try {
                        addressList6 = geo2.getFromLocationName(origin, 1);
                        Toast.makeText(this, origin, Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                    Address address2 = addressList6.get(0);
                    LatLng iaLng2 = new LatLng(address2.getLatitude(), address2.getLongitude());

                    // Already two locations
                    if (markerPoints.size() > 1) {
                        markerPoints.clear();
                        mMap.clear();
                    }
                    // Adding new item to the ArrayList
                    markerPoints.add(iaLng1);
                    markerPoints.add(iaLng2);

                    // Creating MarkerOptions
                    MarkerOptions options1 = new MarkerOptions();
                    MarkerOptions options2 = new MarkerOptions();

                    //  Setting the position of the marker
                    options2.position(iaLng2);
                    options1.position(iaLng1);

                    CameraUpdate yourLocation1 = CameraUpdateFactory.newLatLngZoom(iaLng1, 20);
                    mMap.animateCamera(yourLocation1);
                    CameraUpdate yourLocation2 = CameraUpdateFactory.newLatLngZoom(iaLng2, 20);
                    mMap.animateCamera(yourLocation2);

                /*    Location locationA = new Location("point A");

                    locationA.setLatitude(iaLng1.latitude);
                    locationA.setLongitude(iaLng1.longitude);

                    Location locationB = new Location("point B");

                    locationB.setLatitude(iaLng2.latitude);
                    locationB.setLongitude(iaLng2.longitude);
                    float distance = locationA.distanceTo(locationB);
                    TextView tvdistance = (TextView) findViewById(R.id.tvDistance);
                    String s = Float.toString(distance);

                    tvdistance.setText(s+"KM");*/

                 /*   if (markerPoints.size() == 1) {

                        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    } else if (markerPoints.size() == 2) {
                        options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    }

                    // Add new marker to the Google Map Android API V2
                    mMap.addMarker(options1);


                    mMap.addMarker(options2);


                    // Checks, whether start and end locations are captured
                    if (markerPoints.size() >= 2) {


                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(iaLng2, iaLng1);

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }

                }
                break;

        */
    }


//*****************************************************************************************************************************


    @Override
    public boolean onMarkerClick(Marker marker) {

        String name = marker.getSnippet();
        if (name.equalsIgnoreCase("New place!")) {

        }

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(ModifyTrajet.this,
                "onMapClick:\n" + latLng.latitude + " : " + latLng.longitude,
                Toast.LENGTH_LONG).show();

       /*
        // Already two locations
        if(markerPoints.size()>1){
            markerPoints.clear();
            mMap.clear();
        }

        // Adding new item to the ArrayList
        markerPoints.add(latLng);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(latLng);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.

        if(markerPoints.size()==1){
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }else if(markerPoints.size()==2){
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Add new marker to the Google Map Android API V2
        mMap.addMarker(options);

        // Checks, whether start and end locations are captured
        if(markerPoints.size() >= 2){
            LatLng origin = markerPoints.get(0);
            LatLng dest = markerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }
    }*/

    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        List<Address> addressList15 = null;

        Geocoder geo = new Geocoder(ModifyTrajet.this.getApplicationContext(), Locale.getDefault());

        try {
            addressList15 = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);

        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        Address address20 = addressList15.get(0);

        Toast.makeText(ModifyTrajet.this,
                "New Mark: " + address20.getCountryName(),
                Toast.LENGTH_LONG).show();

        //Add marker on LongClick position
        MarkerOptions markerOptions =
                new MarkerOptions().position(latLng).title(address20.getLocality() + " " + address20.getCountryName()).snippet(address20.getPostalCode() + "," + address20.getCountryCode()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        markerOptions.draggable(true);
        adm.add(address20);
        list2.add(address20.toString());
        mMap.addMarker(markerOptions);
        listgms2.add(markerOptions);
        tab1.add(removeChar(latLng.toString()));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng,10);
        mMap.animateCamera(yourLocation);
        addLines(latLng);
    }

    private static String removeChar(String str) {
        return str.substring(10, str.length() - 1);
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));


    }


    private void addLines(LatLng latLng) {

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.YELLOW);
        polylineOptions.width(10);
        arrayPoints.add(latLng);
        polylineOptions.addAll(arrayPoints);
        mMap.addPolyline(polylineOptions);
    }


    //****************************************************************************************************************


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
            lineOptions.color(Color.MAGENTA);

        }
        TextView distancen = (TextView) findViewById(R.id.tvDistance);
        TextView durationn = (TextView) findViewById(R.id.tvDuration);


        distancen.setText(distance);
        durationn.setText(duration);

        // Drawing polyline in the Google Map for the i-th route
        mMap.addPolyline(lineOptions);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        for (LatLng point : points) {
            builder.include(point);
        }

        LatLngBounds bounds = builder.build();
        int padding = 20; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                padding);
        mMap.moveCamera(cu);
        mMap.animateCamera(cu, 2000, null);
    }
}

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }

            if (view instanceof TextView) {
                TextView distancen = (TextView) findViewById(R.id.tvDistance);
                TextView durationn = (TextView) findViewById(R.id.tvDuration);
                distancen.setText("0 km");
                durationn.setText("0 min");
            }

            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }


    //**************************************************************************************autocomplete************************


    private String downloadUrlauto(String strUrl) throws IOException {
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


// Fetches all places from GooglePlaces AutoComplete Web Service
private class PlacesTaskauto extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... place) {
        // For storing data from web service
        String data = "";

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyBr9dRIraIsPXF3R6DJ1ScEXjD-6ZjE_-k";

        String input = "";

        try {
            input = "input=" + URLEncoder.encode(place[0], "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }


        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        try {
            // Fetching the data from web service in background
            data = downloadUrlauto(url);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Creating ParserTask
        parserTaskauto = new ModifyTrajet.ParserTaskauto();

        // Starting Parsing the JSON string returned by Web Service
        parserTaskauto.execute(result);
    }

}

/**
 * A class to parse the Google Places in JSON format
 */
private class ParserTaskauto extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

    JSONObject jObject;

    @Override
    protected List<HashMap<String, String>> doInBackground(String... jsonData) {

        List<HashMap<String, String>> places = null;

        PlaceJSONParser placeJsonParser = new PlaceJSONParser();

        try {
            jObject = new JSONObject(jsonData[0]);

            // Getting the parsed data as a List construct
            places = placeJsonParser.parse(jObject);

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return places;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> result) {

        String[] from = new String[]{"description"};
        int[] to = new int[]{android.R.id.text1};

        // Creating a SimpleAdapter for the AutoCompleteTextView
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, R.layout.autocomplete_list_item, from, to);
        SimpleAdapter adapter2 = new SimpleAdapter(getBaseContext(), result, R.layout.autocomplete_list_item2, from, to);
        SimpleAdapter adapter3 = new SimpleAdapter(getBaseContext(), result, R.layout.autocomplete_list_item3, from, to);

        // Setting the adapter
        atvPlaces.setAdapter(adapter2);
        etOrigin.setAdapter(adapter3);
        etDestination.setAdapter(adapter);
    }

}


    boolean placeo(String place) {
        boolean exist = true;
        List<Address> addressList0 = null;


        Geocoder geo = new Geocoder(this);
        try {
            addressList0 = geo.getFromLocationName(place, 1);
            Address address = addressList0.get(0);
            LatLng iaLng = new LatLng(address.getLatitude(), address.getLongitude());

            if (iaLng.latitude != 0 || iaLng.longitude != 0) {
                exist = true;

            } else {
                // Toast.makeText(getBaseContext(), "No Origin found", Toast.LENGTH_SHORT).show();
                exist = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            exist = false;
            //Toast.makeText(getBaseContext(), "No Origin found", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            //Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            exist = false;
        } catch (Throwable ex) {
            exist = false;
            //Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
        }
        return exist;
    }

    LatLng posplaceo(String place) {
        LatLng exist = null;
        List<Address> addressList1 = null;


        Geocoder geo1 = new Geocoder(this);

        try {
            addressList1 = geo1.getFromLocationName(place, 1);
            Address address1 = addressList1.get(0);
            LatLng iaLng1 = new LatLng(address1.getLatitude(), address1.getLongitude());


            exist = iaLng1;


        } catch (IOException e) {
            e.printStackTrace();

            exist = null;

        }
        return exist;

    }

    boolean placed(String place) {
        List<Address> addressList2 = null;
        boolean exist = true;
        Geocoder geo = new Geocoder(this);


        try {

            addressList2 = geo.getFromLocationName(place, 1);

            Address address = addressList2.get(0);
            LatLng iaLng = new LatLng(address.getLatitude(), address.getLongitude());

            if (iaLng.latitude != 0 || iaLng.longitude != 0) {
                exist = true;
            } else {
                // Toast.makeText(getBaseContext(), "NO Destination found", Toast.LENGTH_SHORT).show();
                exist = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(getBaseContext(), "No Destination found", Toast.LENGTH_SHORT).show();
            exist = false;

        } catch (Exception ex) {
            //Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            exist = false;
        } catch (Throwable ex) {
            exist = false;
            //Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
        }
        return exist;
    }


    LatLng posplaced(String place) {
        LatLng exist = null;
        List<Address> addressList3 = null;


        Geocoder geo1 = new Geocoder(this);

        try {
            addressList3 = geo1.getFromLocationName(place, 1);
            Address address1 = addressList3.get(0);
            LatLng iaLng1 = new LatLng(address1.getLatitude(), address1.getLongitude());


            exist = iaLng1;


        } catch (IOException e) {
            e.printStackTrace();

            exist = null;

        }
        return exist;

    }

    void tri9(LatLng iaLng2, LatLng iaLng1) {
        String dest = etDestination.getText().toString();
        String origin = etOrigin.getText().toString();
        List<Address> addressList10 = null;
        List<Address> addressList11 = null;


        Geocoder geo1 = new Geocoder(this);

        try {
            addressList10 = geo1.getFromLocationName(dest, 1);


        } catch (IOException e) {
            e.printStackTrace();

        }


        Address address1 = addressList10.get(0);


        Geocoder geo2 = new Geocoder(this);
        try {
            addressList11 = geo2.getFromLocationName(origin, 1);
            Toast.makeText(this, origin + " vers " + dest, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();

        }

        Address address2 = addressList11.get(0);

        adrr.add(address1);
        adrr.add(address2);
        list1.add(address1.toString());
        list1.add(address2.toString());

        // Already two locations
        if (markerPoints.size() > 1) {
            markerPoints.clear();
            mMap.clear();
        }
        // Adding new item to the ArrayList
        markerPoints.add(iaLng1);
        markerPoints.add(iaLng2);

        // Creating MarkerOptions
        MarkerOptions options1 = new MarkerOptions();
        MarkerOptions options2 = new MarkerOptions();

        //  Setting the position of the marker
        options2.position(iaLng1);
        options1.position(iaLng2);

        CameraUpdate yourLocation1 = CameraUpdateFactory.newLatLngZoom(iaLng1, 20);
        mMap.animateCamera(yourLocation1);
        CameraUpdate yourLocation2 = CameraUpdateFactory.newLatLngZoom(iaLng2, 20);
        mMap.animateCamera(yourLocation2);

        if (markerPoints.size() == 1) {

            options1.title(dest + "- " + address1.getCountryName());
            options1.snippet(address1.getPostalCode() + "- " + address1.getLocality());
            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else if (markerPoints.size() == 2) {
            options2.title(origin + "- " + address2.getCountryName());
            options2.snippet(address2.getPostalCode() + "- " + address2.getLocality());
            options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }

        // Add new marker to the Google Map Android API V2


        mMap.addMarker(options1);


        mMap.addMarker(options2);

        listgms.add(options1);
        listgms.add(options2);
        tab2.add(iaLng1.toString());
        tab2.add(iaLng2.toString());


        // Checks, whether start and end locations are captured
        if (markerPoints.size() >= 2) {
            addLines(iaLng2);


            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(iaLng2, iaLng1);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

        }


    }




}




