package com.example.ajaytiwari.parselogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ajay.tiwari on 26-11-2015.
 */
public class UserDetails extends AppCompatActivity {

    TextView branches_et,org_et,desc_et,loc_et,comp_et,name_et,year_et,mobile_dtls;
ImageView profile;
    ParseUser parseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);
        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        String college= extras.getString("college");
        final String organi= extras.getString("organization");
        String desigina= extras.getString("designation");
        String location= extras.getString("location");
        String company= extras.getString("education");
        String profession=extras.getString("profession");
        String tmp = extras.getString("name");
        String mobile=extras.getString("mobile");
        final String parse_user = extras.getString("id");
        profile=(ImageView)findViewById(R.id.profile_pp);
       // Bitmap bitmap = (Bitmap) intent.getParcelableExtra("bitmap");
        String years=extras.getString("batch");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", parse_user);

        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(final List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    // The query was successful.

                    parseUser = objects.get(0);

                    if (objects.get(0).getString("fbid")!=null){
                        Picasso
                                .with(getApplicationContext())
                                .load("https://graph.facebook.com/" + objects.get(0).getString("fbid") + "/picture?type=large")
                                .into(profile);
                    }else {
                    //Log.d("fbid",objects.get(0).getString("fbid"));
                    ParseFile fileObject = (ParseFile) parseUser.get("image");
                    if (fileObject != null) {
                        fileObject.getDataInBackground(new GetDataCallback() {

                            @Override
                            public void done(byte[] data, com.parse.ParseException e) {
                                if (e == null) {
                                    if (data != null) {

                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        profile.setImageBitmap(bmp);

                                        // ImageView imageView = new ImageView(MainActivity.this);
// Set the Bitmap data to the ImageView


                                    }




// Get the Root View of the layout

                                } else {
                                    // something went wrong

                                }
                            }

                        });
                    }
                } }else {
                    // Something went wrong.
                }

            }
        });


        profile=(ImageView)findViewById(R.id.profile_pp);
        mobile_dtls=(TextView)findViewById(R.id.mobile_dtl);
        branches_et=(TextView)findViewById(R.id.bran);
        org_et=(TextView)findViewById(R.id.organiz);
        desc_et=(TextView)findViewById(R.id.design);
        loc_et=(TextView)findViewById(R.id.locat);
        comp_et=(TextView)findViewById(R.id.compan);
        name_et=(TextView)findViewById(R.id.nm);
        year_et=(TextView)findViewById(R.id.yer);
        branches_et.setText(college);
        org_et.setText(organi);
        desc_et.setText(profession);
        loc_et.setText(location);
        comp_et.setText(company);
        name_et.setText(tmp);
        mobile_dtls.setText(mobile);
        year_et.setText(years);


       /* profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetails.this, MainActivity.class));
            }
        });*/
        Button mes=(Button)findViewById(R.id.msg);
      /*  mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentX= new Intent(UserDetails.this,Messages.class);
                intentX.putExtra("id",parse_user);
                startActivity(intentX);
            }
        });*/
        //profile.setImageBitmap(bitmap);
       // Toast.makeText(getApplicationContext(),batch+organi+desigina+location+company+tmp,Toast.LENGTH_SHORT).show();
    }
}
