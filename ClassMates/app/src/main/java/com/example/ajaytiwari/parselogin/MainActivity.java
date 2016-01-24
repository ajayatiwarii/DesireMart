package com.example.ajaytiwari.parselogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int PICK_IMAGE_REQUEST = 1;
    TextView user_name_head, user_email_head;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;
    String email;
    String name;
    ParseUser newuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_contact);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
            }
        });
        String userId = pref.getString("fid", null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ImageView img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        LinearLayout headerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        user_name_head = (TextView) headerView.findViewById(R.id.user_name_head);
        user_email_head = (TextView) headerView.findViewById(R.id.email_user_head);

        if (userId != null) {
            user_name_head.setText(pref.getString("name", null));
            user_email_head.setText(pref.getString("email", null));
        } else {
            user_name_head.setText(ParseUser.getCurrentUser().getString("name"));
            user_email_head.setText(ParseUser.getCurrentUser().getEmail());
        }
        Log.d("name", email + name + newuser);

        final ImageView profilePic = (ImageView) headerView.findViewById(R.id.profile_image);
        String prefImage = pref.getString("imagePreferance", null);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {

            Picasso
                    .with(this)
                    .load("https://graph.facebook.com/" + ParseUser.getCurrentUser().getString("fbid") + "/picture?type=large")
                    .into(profilePic);
        } else if (prefImage != null) {
            profilePic.setImageBitmap(decodeBase64(prefImage));
        } else {

            profilePic.setImageDrawable(getResources().getDrawable(R.drawable.a));
            ParseUser user = ParseUser.getCurrentUser();
            ParseFile fileObject = (ParseFile) user.get("image");
            if (fileObject != null) {
                fileObject.getDataInBackground(new GetDataCallback() {

                    @Override
                    public void done(byte[] data, com.parse.ParseException e) {
                        if (e == null) {
                            if (data != null) {
                                byte[] ajaya = data.clone();
                                Bitmap bmp = BitmapFactory.decodeByteArray(ajaya, 0, ajaya.length);
                                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                                dialog.setMessage(getString(R.string.progress_signup));
                                profilePic.setImageBitmap(bmp);
                                dialog.dismiss();
                            }

                        }
                    }

                });
            }

        }
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.nav_camera) {
            // Handle the camera action
            Snackbar.make(getCurrentFocus(), "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] dataa = stream.toByteArray();
                ParseFile file = new ParseFile("3529.jpeg", dataa);
                file.saveInBackground();
                ParseUser user = ParseUser.getCurrentUser();
                user.put("image", file);
                user.saveInBackground();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
