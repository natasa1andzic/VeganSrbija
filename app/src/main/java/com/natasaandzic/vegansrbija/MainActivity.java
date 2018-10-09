package com.natasaandzic.vegansrbija;

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


import android.app.ProgressDialog;
import android.content.Intent;
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

    TextView userEmail;
    TextView userBday;
    ImageView avatarImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        loginBtn = (Button) findViewById(R.id.main_loginBtn);
        emailRegisterBtn = (Button) findViewById(R.id.main_emailRegisterBtn);
        fbBtn = (LoginButton) findViewById(R.id.main_regFacebookBtn);
       // googleBtn = (Button) findViewById(R.id.main_regGoogleBtn);

        userEmail = (TextView) findViewById(R.id.userEmail);
        userBday = (TextView) findViewById(R.id.userBday);
        avatarImg = (ImageView) findViewById(R.id.avatarImg);

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

                final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        progressDialog.dismiss();
                        Log.d("Response", response.toString());
                        getData(object);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, email, birthday");
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

        if (AccessToken.getCurrentAccessToken() != null) {
            userEmail.setText(AccessToken.getCurrentAccessToken().getUserId());
        }

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

    private void getData(JSONObject object) {

        try {
            URL profilePicture = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
            Picasso.with(this).load(profilePicture.toString()).into(avatarImg);

            userEmail.setText(object.getString("email"));
            userBday.setText(object.getString("birthday"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

