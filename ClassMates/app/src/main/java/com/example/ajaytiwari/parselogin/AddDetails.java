package com.example.ajaytiwari.parselogin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajaytiwari.parselogin.utils.ContactsCompletionView;
import com.example.ajaytiwari.parselogin.utils.Person;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ajay.tiwari on 24-11-2015.
 */
public class AddDetails extends AppCompatActivity implements View.OnClickListener, TokenCompleteTextView.TokenListener {
    ParseUser m_perseUser;
    String education;
    String profession;
    Button save;
    EditText loc;
    SharedPreferences pref;
    ImageButton add_edu;
    ImageButton add_prof;
    EditText college_et;
    EditText batch_et;
    EditText mobile_et;
    CheckBox education_help;
    CheckBox sales_help;
    CheckBox marketing_help;
    RadioGroup organisation;
    RadioButton gov;
    RadioButton pvt;
    ImageView user_image;
    private int PICK_IMAGE_REQUEST = 1;
    ContactsCompletionView completionView;
    ContactsCompletionView completionView2;
    Person[] people;
    ArrayAdapter<Person> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add details");
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        people = new Person[]{
                new Person("Marshall Weir", "marshall@example.com"),
                new Person("Margaret Smith", "margaret@example.com"),
                new Person("Max Jordan", "max@example.com"),
                new Person("Meg Peterson", "meg@example.com"),
                new Person("Amanda Johnson", "amanda@example.com"),
                new Person("Terry Anderson", "terry@example.com")
        };

        adapter = new FilteredArrayAdapter<Person>(this, R.layout.person_layout, people) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                Person p = getItem(position);
                ((TextView) convertView.findViewById(R.id.name)).setText(p.getName());
                ((TextView) convertView.findViewById(R.id.email)).setText(p.getEmail());

                return convertView;
            }

            @Override
            protected boolean keepObject(Person person, String mask) {
                mask = mask.toLowerCase();
                return person.getName().toLowerCase().startsWith(mask) || person.getEmail().toLowerCase().startsWith(mask);
            }
        };
        completionView = (ContactsCompletionView) findViewById(R.id.searchView);
        completionView.setAdapter(adapter);
        completionView.setTokenListener(this);
        completionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        completionView2 = (ContactsCompletionView) findViewById(R.id.searchView2);
        completionView2.setAdapter(adapter);
        completionView2.setTokenListener(this);
        completionView2.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
        college_et = (EditText) findViewById(R.id.college_add);
        batch_et = (EditText) findViewById(R.id.batch_add);
        mobile_et = (EditText) findViewById(R.id.mobile);
        loc = (EditText) findViewById(R.id.locatn);
        education_help = (CheckBox) findViewById(R.id.education_radio);
        sales_help = (CheckBox) findViewById(R.id.sales_radio);
        marketing_help = (CheckBox) findViewById(R.id.marketing_radio);
        organisation = (RadioGroup) findViewById(R.id.organisation);
        gov = (RadioButton) findViewById(R.id.govt);
        pvt = (RadioButton) findViewById(R.id.pvt);
        save = (Button) findViewById(R.id.save_details);
        add_edu = (ImageButton) findViewById(R.id.add_edu);
        add_prof = (ImageButton) findViewById(R.id.add_prof);

        add_edu.setOnClickListener(this);
        add_prof.setOnClickListener(this);
        save.setOnClickListener(this);
        batch_et.setOnClickListener(this);
        education_help.setOnClickListener(this);
        sales_help.setOnClickListener(this);
        marketing_help.setOnClickListener(this);

        user_image = (ImageView) findViewById(R.id.user_image);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });


        m_perseUser = ParseUser.getCurrentUser();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                user_image.setImageBitmap(bitmap);
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

    public void show() {
        final Dialog d = new Dialog(AddDetails.this);
        d.setTitle("Select Batch");
        d.setContentView(R.layout.dialog);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        np.setMaxValue(year);
        np.setMinValue(1962);
        np.setValue(year);
        np.setWrapSelectorWheel(false);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                batch_et.setText(String.valueOf(newVal));
                Log.d("stark", String.valueOf(newVal));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d.dismiss();
            }
        });
        d.show();


    }

    public void saveDetails() {

        String college;
        String batch;
        String mobile;
        String organization;
        String locatnn;
        ArrayList<String> helps = new ArrayList<>();
        college = college_et.getText().toString();
        batch = batch_et.getText().toString();
        mobile = mobile_et.getText().toString();
        locatnn = loc.getText().toString();

        if (gov.isChecked()) {
            organization = "Govt.";

        } else {
            organization = "Pvt.";
        }
        completionView2.append(",");
        profession = completionView2.getObjects().toString();
        completionView.append(",");
        education = completionView.getObjects().toString();
        m_perseUser.put("batch", batch);
        m_perseUser.put("education", education);
        m_perseUser.put("profession", profession);
        m_perseUser.put("mobile", mobile);
        m_perseUser.put("organization", organization);
        m_perseUser.put("college", college);
        m_perseUser.put("location", locatnn);
        m_perseUser.put("salesHelp", false);
        m_perseUser.put("marketingHelp", false);
        m_perseUser.put("educationHelp", false);
        m_perseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                final SharedPreferences.Editor editor = pref.edit();
                editor.putString("id", m_perseUser.getObjectId());
                editor.putString("name", m_perseUser.getString("name"));
                editor.putString("email", m_perseUser.getEmail());
                ParseUser parseUser = ParseUser.getCurrentUser();
                ParseFile fileObject = (ParseFile) parseUser.get("image");
                if (fileObject != null) {
                    fileObject.getDataInBackground(new GetDataCallback() {

                        @Override
                        public void done(byte[] data, com.parse.ParseException e) {
                            if (e == null) {
                                if (data != null) {
                                    byte[] ajaya = data.clone();
                                    Bitmap bmp = BitmapFactory.decodeByteArray(ajaya, 0, ajaya.length);
                                    final ProgressDialog dialog = new ProgressDialog(AddDetails.this);
                                    dialog.setMessage(getString(R.string.progress_signup));
                                    dialog.show();
                                    editor.putString("imagePreferance", encodeTobase64(bmp));
                                    editor.apply();
                                    dialog.dismiss();
                                }

                            }
                        }

                    });
                }
                editor.apply();
                AddDetails.this.finish();
                Intent intent = new Intent(AddDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.save_details: {
                if ((college_et.getText().toString().equalsIgnoreCase("") ||
                        loc.getText().toString().equalsIgnoreCase("") ||
                        batch_et.getText().toString().equalsIgnoreCase("") ||
                        completionView.getObjects().toString().equalsIgnoreCase("") ||
                        completionView2.getObjects().toString().equalsIgnoreCase("") ||
                        mobile_et.getText().toString().equalsIgnoreCase(""))) {

                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();

                } else {
                    saveDetails();
                }
                break;
            }


            case R.id.batch_add: {
                show();
                break;
            }
            case R.id.education_radio: {
                if (education_help.isChecked()) {
                    m_perseUser.put("educationHelp", true);
                } else {
                    m_perseUser.put("educationHelp", false);
                }
                break;
            }
            case R.id.sales_radio: {
                if (sales_help.isChecked()) {
                    m_perseUser.put("salesHelp", true);
                } else {
                    m_perseUser.put("salesHelp", false);
                }
                break;
            }
            case R.id.marketing_radio: {
                if (marketing_help.isChecked()) {
                    m_perseUser.put("marketingHelp", true);
                } else {
                    m_perseUser.put("marketingHelp", false);
                }
                break;
            }

            case R.id.add_edu: {
                Log.d("ajaya", completionView.getText().toString());
                completionView.append(",");

                Log.d("ajaya", completionView.getObjects().toString());
                education = completionView.getObjects().toString();
                break;

            }
            case R.id.add_prof: {

                completionView2.append(",");
                Log.d("ajaya", completionView2.getObjects().toString());
                profession = completionView2.getObjects().toString();
                break;

            }
        }

    }

    @Override
    public void onTokenAdded(Object token) {

    }

    @Override
    public void onTokenRemoved(Object token) {

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
}
