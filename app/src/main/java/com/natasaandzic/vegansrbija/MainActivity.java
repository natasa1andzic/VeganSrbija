package com.natasaandzic.vegansrbija;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{

    Button loginBtn;
    Button emailRegisterBtn;

    LoginButton fbBtn;
    SignInButton googleBtn;

    CallbackManager callbackManager;
    ProgressDialog progressDialog;
    String id, firstName, lastName, birthday, gender, email;
    private URL profilePicture;

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGoogleSignInOptions;
    private static final int GOOGLE_REQ_CODE = 9001;
    private static final int FACEBOOK_REQ_CODE = 1;

    TextView userNameTv;
    TextView userLastNameTv;
    TextView userBdayTv;
    ImageView avatarImgIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.main_loginBtn);
        emailRegisterBtn = (Button) findViewById(R.id.main_emailRegisterBtn);
        fbBtn = (LoginButton) findViewById(R.id.main_regFacebookBtn);
        googleBtn = findViewById(R.id.main_regGoogleBtn);
        googleBtn.setSize(SignInButton.SIZE_STANDARD);

        userNameTv = (TextView) findViewById(R.id.userName);
        userLastNameTv = (TextView) findViewById(R.id.userLastname);
        userBdayTv = (TextView) findViewById(R.id.userBday);
        avatarImgIv = (ImageView) findViewById(R.id.avatarImg);

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,mGoogleSignInOptions ).build();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        emailRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EmailRegisterActivity.class);
                startActivity(i);
            }
        });

        /* --- Facebook login using Graph API --- */

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "user_gender");
        fbBtn.setReadPermissions(permissionNeeds);

        fbBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Retrieving data...");
                progressDialog.show();
                System.out.println("onSuccess");

                String accessToken = loginResult.getAccessToken().getToken();
               // if (AccessToken.getCurrentAccessToken() != null)
               //     userEmail.setText(AccessToken.getCurrentAccessToken().getUserId());

                final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        progressDialog.dismiss();
                        Log.d("Response", response.toString());

                        try {

                            id = object.getString("id");
                            profilePicture = new URL("https://graph.facebook.com/" + id + "/picture?width=250&height=250");
                            if(object.has("email"))
                                email = object.getString("email");
                            if(object.has("first_name"))
                                firstName = object.getString("first_name");
                            if(object.has("last_name"))
                                lastName = object.getString("last_name");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        String concat = firstName + " " + lastName;
                        userNameTv.setText(concat);
                       // userLastNameTv.setText(lastName);
                        userBdayTv.setText(birthday);

                        Intent i = new Intent(MainActivity.this,ProfileActivity.class);
                        i.putExtra("name",firstName);
                        i.putExtra("surname",lastName);
                        i.putExtra("birthday",birthday);
                        i.putExtra("email",email );
                        i.putExtra("imageUrl",profilePicture.toString());
                        startActivity(i);
                        finish();
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender , location");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });


        /* --- Google login using Google API --- */

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(i,GOOGLE_REQ_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        //gugl
        if (requestCode == GOOGLE_REQ_CODE) {
                getGoogleCredentials(data);
            }
        }


    private void getGoogleCredentials(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String avatarUrl = account.getPhotoUrl().toString();

            userNameTv.setText(name);
            userBdayTv.setText(email);

            // Picasso.with(MainActivity.this).load(avatarUrl).into(avatarImgIv);
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            i.putExtra("name",name);
            i.putExtra("email",email);
            i.putExtra("imageUrl",avatarUrl);
            startActivity(i);
    }}


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}