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
import com.squareup.picasso.Picasso;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    Button emailRegisterBtn;
    LoginButton fbBtn;
    Button googleBtn;
    CallbackManager callbackManager;
    ProgressDialog progressDialog;
    String id, firstName, lastName, birthday, gender;
    private URL profilePicture;

    TextView userName;
    TextView userLastName;
    TextView userBday;
    ImageView avatarImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.main_loginBtn);
        emailRegisterBtn = (Button) findViewById(R.id.main_emailRegisterBtn);
        fbBtn = (LoginButton) findViewById(R.id.main_regFacebookBtn);
        // googleBtn = (Button) findViewById(R.id.main_regGoogleBtn);

        userName = (TextView) findViewById(R.id.userName);
        userLastName = (TextView) findViewById(R.id.userLastname);
        userBday = (TextView) findViewById(R.id.userBday);
        avatarImg = (ImageView) findViewById(R.id.avatarImg);


        callbackManager = CallbackManager.Factory.create();

        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile");
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


                        Picasso.with(MainActivity.this).load(profilePicture.toString()).into(avatarImg);

                        userName.setText(firstName);
                        userLastName.setText(lastName);
                        userBday.setText(birthday);

                        Intent i = new Intent(MainActivity.this,ProfileActivity.class);
                        i.putExtra("name",firstName);
                        i.putExtra("surname",lastName);
                        i.putExtra("birthday",birthday);
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


       /* googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trazi permission
                //kada se dobije permission, prebaci intent na HomeActivity
                //podaci profila su podaci gmail naloga
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}