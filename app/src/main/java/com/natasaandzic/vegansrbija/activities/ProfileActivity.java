package com.natasaandzic.vegansrbija.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.Utils.SharedPrefManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.VISIBLE;


public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

	TextView userName;
	TextView userLastName;
	TextView userEmail;

	Bundle mBundle;

	String avatarString;
	String nameString;
	String lastNameString;
	String emailString;

	Button backBtn;
	Button signOutBtn;

	SharedPrefManager sharedPrefManager;
	private GoogleApiClient mGoogleApiClient;
	private FirebaseAuth mAuth;

	private CircleImageView avatar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		avatar = (CircleImageView) findViewById(R.id.profileAvatar);
		userName = (TextView) findViewById(R.id.profileName);
		userLastName = (TextView) findViewById(R.id.profileLastname);
		userEmail = (TextView) findViewById(R.id.profileEmail);
		signOutBtn = (Button) findViewById(R.id.signOutButton);
		backBtn = (Button)findViewById(R.id.backButton);

		sharedPrefManager = new SharedPrefManager(this);
		nameString = sharedPrefManager.getName();
		Log.d("NAME", nameString);
		emailString = sharedPrefManager.getUserEmail();
		Log.d("EMAIL", emailString);
		String uri = sharedPrefManager.getPhoto();
		Uri photoUri = Uri.parse(uri);

		Picasso.with(this)
				.load(photoUri)
				.placeholder(android.R.drawable.sym_def_app_icon)
				.error(android.R.drawable.sym_def_app_icon)
				.into(avatar);

		userName.setText(nameString);
		userEmail.setText(emailString);

		configureSignIn();

		signOutBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new SharedPrefManager(getApplicationContext()).clear();
				mAuth.signOut();

				Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
						new ResultCallback<Status>() {
							@Override
							public void onResult(@NonNull Status status) {
								Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
								startActivity(intent);
							}
						}
				);

			}
		});

		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileActivity.this, BelgradeActivity.class);
				startActivity(i);
			}
		});

		//Facebook bundle varijanta
        /*mBundle = getIntent().getExtras();

        avatarString = mBundle.getString("imageUrl");
        nameString = mBundle.getString("name");
        lastNameString = mBundle.getString("surname");
        bdayString = mBundle.getString("birthday");
        emailString = mBundle.getString("email");

        userName.setText(nameString);

        if(mBundle.getInt("code")==1) {
            userLastName.setVisibility(VISIBLE);
            userLastName.setText(lastNameString);
        }
        userEmail.setText(emailString);

        Glide
                .with(this)
                .load(avatarString)
                .override(250, 250)
                .into(avatar);
    }
*/
	}

	// This method configures Google SignIn
	public void configureSignIn(){
		GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, options)
				.build();
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
}
