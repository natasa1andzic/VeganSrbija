package com.natasaandzic.vegansrbija.activities;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.natasaandzic.vegansrbija.R;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{

    LoginButton fbBtn;
    SignInButton googleBtn;

    CallbackManager callbackManager;
    ProgressDialog progressDialog;
    String id, firstName, lastName, email;
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

        fbBtn = (LoginButton) findViewById(R.id.main_regFacebookBtn);
        googleBtn = findViewById(R.id.main_regGoogleBtn);
        googleBtn.setSize(SignInButton.SIZE_STANDARD);

        userNameTv = (TextView) findViewById(R.id.userName);
        userLastNameTv = (TextView) findViewById(R.id.userLastname);
        userBdayTv = (TextView) findViewById(R.id.userBday);
        avatarImgIv = (ImageView) findViewById(R.id.avatarImg);

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,mGoogleSignInOptions ).build();


        /* --- Facebook login using Graph API --- */

        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "public_profile");
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        String concat = firstName + " " + lastName;
                        userNameTv.setText(concat);
                       userLastNameTv.setText(lastName);

                        Intent i = new Intent(MainActivity.this,ProfileActivity.class);
                        i.putExtra("name",firstName);
                        i.putExtra("surname",lastName);
                        i.putExtra("email",email );
                        i.putExtra("imageUrl",profilePicture.toString());
                        i.putExtra("code", 1);
                        startActivity(i);
                        finish();
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, location");
                parameters.putInt("code", FACEBOOK_REQ_CODE);
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