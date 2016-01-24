package com.example.ajaytiwari.parselogin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajaytiwari.parselogin.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.GetDataCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;

import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;
    SharedPreferences pref;

    @InjectView(R.id.facebook_login)
    Button _facebookLoginButton;
    private Dialog progressDialog;
    public String public_Profile = "public_profile";
    String email = "email";
    String user_friends = "user_friends";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String shar=pref.getString("email",null);
        if (shar!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            this.finish();
        }

        ButterKnife.inject(this);
        _facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick(v);
            }
        });
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            showMainActivity();
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isValidate = validate();
                if (isValidate) {
                    loginn();
                } else {
                    onLoginFailed();
                }

            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity

                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);

                overridePendingTransition(R.anim.left_out, R.anim.left_out);
            }
        });
    }
    private void loginn() {
        String username = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // Validate the log in data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }
        if (password.length() == 0) {
            if (validationError) {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }
        validationErrorMessage.append(getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage(getString(R.string.progress_login));
        dialog.show();
        // Call the Parse login method

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                dialog.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    String currentUser = ParseUser.getCurrentUser().getEmail();


                   // Parse.initialize(this, "xxxxxxxxxxxxxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxxxxxxxxxxx");


                    final SharedPreferences.Editor editor = pref.edit();
                    editor.putString("id",ParseUser.getCurrentUser().getObjectId());
                    editor.putString("name",ParseUser.getCurrentUser().getUsername());
                    editor.putString("email", ParseUser.getCurrentUser().getEmail());




                    ParseUser parseUser = ParseUser.getCurrentUser();
                    ParseFile fileObject = (ParseFile)parseUser.get("image");
                    if (fileObject!=null){
                        fileObject.getDataInBackground(new GetDataCallback() {

                            @Override
                            public void done(byte[] data, com.parse.ParseException e) {
                                if (e == null) {
                                    if (data!=null){
                                        byte[] ajaya = data.clone();
                                        Bitmap bmp = BitmapFactory.decodeByteArray(ajaya, 0, ajaya.length);
                                        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                                        dialog.setMessage(getString(R.string.progress_signup));
                                        //dialog.show();
                                        // ImageView imageView = new ImageView(MainActivity.this);
// Set the Bitmap data to the ImageView
                                        editor.putString("imagePreferance", encodeTobase64(bmp));
                                        editor.apply();
                                        dialog.dismiss();}


// Get the Root View of the layout

                                } else {
                                    // something went wrong
                                }
                            }

                        });}


                    Toast.makeText(LoginActivity.this, currentUser, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {
            _emailText.setError("enter a user name");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;}

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public void onLoginClick(View v) {
       // progressDialog = ProgressDialog.show(LoginActivity.this, "", "loading", true);


        List<String> permissions = Arrays.asList(public_Profile, email, user_friends);
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                //  progressDialog.dismiss();
                if (user.isNew()) {
                    showUserDetailsActivity();
                } else {
                    showMainActivity();
                }
            }
        });
    }

    private void showUserDetailsActivity() {

        makeMeRequest();

        Intent intent = new Intent(this, AddDetails.class);
        startActivity(intent);
        this.finish();
    }
    private void showMainActivity() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    private void makeMeRequest() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {
                            JSONObject userProfile = new JSONObject();
                            ParseUser parseUser = ParseUser.getCurrentUser();
                            try {
                              parseUser.put("name", jsonObject.getString("name"));
                                parseUser.put("email", jsonObject.getString("email"));
                                parseUser.put("gender",jsonObject.getString("gender"));
                                parseUser.put("batch", "2011");
                                parseUser.put("education", "null");
                                parseUser.put("profession", "null");
                                parseUser.put("mobile", "9999999999");
                                parseUser.put("organization", "null");
                                parseUser.put("college", "null");
                                parseUser.put("location","null");
                                parseUser.put("salesHelp", false);
                                parseUser.put("marketingHelp", false);
                                parseUser.put("educationHelp", false);
                                parseUser.put("fbid",jsonObject.getString("id"));
                                final SharedPreferences.Editor editor = pref.edit();
                                editor.putString("fid", jsonObject.getString("id"));
                                editor.apply();
                                parseUser.saveInBackground();

                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (graphResponse.getError() != null) {
                            Toast.makeText(getApplicationContext(), graphResponse.getError().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString(Constants.FB_FIELDS, "id,email,gender,name");
        request.setParameters(parameters);
        request.executeAsync();
    }



}
