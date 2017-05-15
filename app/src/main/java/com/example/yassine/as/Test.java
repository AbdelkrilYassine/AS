package com.example.yassine.as;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.path;

public class Test extends AppCompatActivity implements View.OnClickListener {
    TextView name, username, email, birthdate, password, txt1, txt2;
    private Menu menu;
    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    TextView popupText;
    Button pic, mdprofile;
    ImageView img;
    Toolbar bar;
    private final int requestCode = 20;

    private ArrayList<MyImage> images;
    private ImageAdapter imageAdapter;
    private ListView listView;
    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    String usernames, passwords;
    String USERNAME, PASSWORD, EMAIL, NAME;



    private static final int CAMERA_REQUEST = 1888;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        name = (TextView) findViewById(R.id.namet);
        username = (TextView) findViewById(R.id.usernamet);
        password = (TextView) findViewById(R.id.passwordt);
        email = (TextView) findViewById(R.id.emailt);
        birthdate = (TextView) findViewById(R.id.birthdayt);
        img = (ImageView) findViewById(R.id.img);
        pic = (Button) findViewById(R.id.mdpic);
        mdprofile = (Button) findViewById(R.id.mdprofile);
        pic.setOnClickListener(this);
        mdprofile.setOnClickListener(this);


        //  String user = intent.getExtras().getString("usernametab");
        // this.setTitle("Welcome " + user);

        Intent intent = getIntent();

        Bundle b = intent.getExtras();

        usernames = b.getString("usernametest");
        passwords = b.getString("passwordtest");
        username.setText(usernames);
        password.setText(passwords);

        //colorPrimary will be the toolbar color.


        this.setTitle("Welcome " + usernames);


        BackGroundProfile bi = new BackGroundProfile();
        bi.execute(usernames, passwords);


// Construct the data source
        images = new ArrayList();
        // Create the adapter to convert the array to views
        imageAdapter = new ImageAdapter(this, images);
        // Attach the adapter to a ListView
        // listView = (ListView) findViewById(R.id.main_list_view);
        //  listView.setAdapter(imageAdapter);


    }


  /*  @Override
    public void onBackPressed()
    {
    }

    */

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu, menu);


        MenuItem item = menu.findItem(R.id.menu_logout);
        SpannableStringBuilder builder = new SpannableStringBuilder("        Logout     ");
        builder.setSpan(new ImageSpan(this, R.drawable.logout), 17, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);

        MenuItem item1 = menu.findItem(R.id.menu_settings);
        SpannableStringBuilder builder1 = new SpannableStringBuilder("        Settings   ");
        builder1.setSpan(new ImageSpan(this, R.drawable.settings), 17, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item1.setTitle(builder1);


        MenuItem item2 = menu.findItem(R.id.menu_help);
        SpannableStringBuilder builder2 = new SpannableStringBuilder("        Help           ");
        builder2.setSpan(new ImageSpan(this, R.drawable.help), 20, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item2.setTitle(builder2);


        MenuItem item3 = menu.findItem(R.id.menu_about);
        SpannableStringBuilder builder3 = new SpannableStringBuilder("        About          ");
        builder3.setSpan(new ImageSpan(this, R.drawable.about), 19, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item3.setTitle(builder3);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:

                MediaPlayer mp = MediaPlayer.create(this, R.raw.soundmenu);
                mp.start();

                showPopup(this, "About :", "Cette application Randon-ili a pour objectifs de découvrir et d'augmenter le nombre des randonnées, d'améliorer leur qualité et de faciliter d'avoir des randonneurs.Développé par Yassine Ben Abdelkrim");


                return true;
            case R.id.menu_help:
                MediaPlayer mp1 = MediaPlayer.create(this, R.raw.soundmenu);
                mp1.start();
                showPopup(this, "Help", "Contact : yassine_abdelkrim@yahoo.fr");

                return true;
            case R.id.menu_logout:
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.soundmenu);
                mp2.start();
                Intent s = new Intent(Test.this, Connexion.class);
                startActivity(s);


                return true;

            case R.id.menu_settings:
                MediaPlayer mp3 = MediaPlayer.create(this, R.raw.soundmenu);
                mp3.start();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // The method that displays the popup.
    private void showPopup(final Activity context, String ch1, String ch2) {
        int popupWidth = 1000;
        int popupHeight = 900;


        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);


        txt1 = (TextView) layout.findViewById(R.id.textView1);
        txt2 = (TextView) layout.findViewById(R.id.textView2);

        txt1.setText(ch1);
        txt2.setText(ch2);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 220;
        int OFFSET_Y = 900;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setAnimationStyle(android.R.style.Animation_Dialog);

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, OFFSET_X, OFFSET_Y);

        Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

    }
*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mdpic:
                Toast.makeText(Test.this, "Change your profile picture.", Toast.LENGTH_SHORT).show();

                btnAddOnClick(v);

                break;
            case R.id.mdprofile:
                Toast.makeText(Test.this, "Modifier vos données.", Toast.LENGTH_SHORT).show();
                Intent main = new Intent(Test.this, Mdprofile.class);
                main.putExtra("usr", usernames);
                startActivity(main);

                break;
        }

    }


    @Override
    public void onBackPressed() {
    }


    public void notifyThis(String title, String message) {

/*
        Intent intent = new Intent(this, Connexion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat notification = new NotificationCompat.Builder(this)
                .setTicker("Randon-ili")
                .setSmallIcon(R.drawable.usrnew)

                .setContentTitle(title).setContentText(message)
                .setAutoCancel(true)
                .setContent(null)
                .setSound(sound)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);




*/

        /*fNotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.usrnew)
                        .setContentTitle(title)
                        .setContentText(message);
        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());*/

    }


//**********************************************************************************************IMAGE

    public void btnAddOnClick(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.setTitle("Alert Dialog View");
        Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnChoosePath)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  activeGallery();

                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);

                    }
                });
        dialog.findViewById(R.id.btnTakePhoto)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // activeTakePhoto();
                        if (checkPermissions()) {
                            // carry on the normal flow, as the case of  permissions  granted.
                            takeImageFromCamera(v);
                        }


                    }
                });

        // show dialog on screen
        dialog.show();
    }

    /**
     * take a photo
     */
    /*
    private void activeTakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);
            takePictureIntent
                    .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * to gallery
     */

    /*
    private void activeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode,
                                              Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE &&
                        resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver()
                            .query(selectedImage, filePathColumn, null, null,
                                    null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    MyImage image = new MyImage();
                    image.setTitle("Test");
                    image.setDescription(
                            "test choose a photo from gallery and add it to " +
                                    "list view");
                    image.setDatetime(System.currentTimeMillis());
                    image.setPath(picturePath);
                    images.add(image);
                }
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE &&
                        resultCode == RESULT_OK) {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor =
                            managedQuery(mCapturedImageURI, projection, null,
                                    null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(
                            MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);
                    MyImage image = new MyImage();
                    image.setTitle("Test");
                    image.setDescription(
                            "test take a photo and add it to list view");
                    image.setDatetime(System.currentTimeMillis());
                    image.setPath(picturePath);
                    images.add(image);
                }
        }
    }

    */
    public void takeImageFromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(mphoto);

        }


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bmp = null;
            try {
                ParcelFileDescriptor parcelFileDescriptor =
                        getContentResolver().openFileDescriptor(selectedImage, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
// or Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver‌​(), uri);

                parcelFileDescriptor.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            img.setImageBitmap(bmp);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
                    // no permissions granted.
                }
                return;
            }
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class BackGroundProfile extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {


            usernames = params[0];
            passwords = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://api2.randon.ili-studios.tn/afficheprofile.php?username=" + usernames + "&password=" + passwords);
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
                USERNAME = user_data.getString("Username");
                PASSWORD = user_data.getString("Password");
                EMAIL = user_data.getString("Email");
                NAME = user_data.getString("Name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            username.setText(USERNAME);
            password.setText(PASSWORD);
            email.setText(EMAIL);
            name.setText(NAME);

        }
    }


}