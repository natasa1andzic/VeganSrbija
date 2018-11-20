package com.natasaandzic.vegansrbija.activities;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.Utils.SharedPrefManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

	TextView userName;
	TextView userLastName;
	TextView userEmail;

	String code;
	String nameString;
	String emailString;

	Button backBtn;
	Button signOutBtn;

	SharedPrefManager sharedPrefManager;
	private GoogleApiClient mGoogleApiClient;
	private GoogleSignInClient mGoogleSignInClient;
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

		mAuth = FirebaseAuth.getInstance();

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		sharedPrefManager = new SharedPrefManager(this);
		nameString = sharedPrefManager.getName();
		if (nameString==null) {
			Intent i = new Intent(ProfileActivity.this, BelgradeActivity.class);
			startActivity(i);
			Toast.makeText(ProfileActivity.this, "You are not logged in", Toast.LENGTH_SHORT).show();
		}
		//Log.d("NAME", nameString);
		emailString = sharedPrefManager.getUserEmail();
		//Log.d("EMAIL", emailString);
		String uri = sharedPrefManager.getPhoto();
		Uri photoUri = Uri.parse(uri);
		code=sharedPrefManager.getCode();

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
				if(code=="1") {
					LoginManager.getInstance().logOut();
					Log.d("Log out? ", mAuth.toString());
					Intent i = new Intent(ProfileActivity.this, MainActivity.class);
					Log.d("Intent??", "Main");
					startActivity(i);
				}
				else if(code=="2"){
					mGoogleSignInClient.signOut().addOnCompleteListener(ProfileActivity.this,
							new OnCompleteListener<Void>() {
								@Override
								public void onComplete(@NonNull Task<Void> task) {
									Intent i = new Intent(ProfileActivity.this, MainActivity.class);
									Log.d("Radi li gugl ? :D", "Main");
									startActivity(i);
								}
							});
				}
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
