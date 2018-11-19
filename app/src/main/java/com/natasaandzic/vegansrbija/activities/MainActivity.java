package com.natasaandzic.vegansrbija.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ServerValue;
import com.natasaandzic.vegansrbija.R;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.natasaandzic.vegansrbija.Utils.Constants;
import com.natasaandzic.vegansrbija.Utils.SharedPrefManager;
import com.natasaandzic.vegansrbija.Utils.Utils;
import com.natasaandzic.vegansrbija.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements  GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,
		View.OnClickListener {

	private LoginButton fbBtn;
	private SignInButton googleBtn;
	private Button continueBtn;
	CallbackManager callbackManager;

	ProgressDialog progressDialog;
	private String name, email;
	private String photo;
	private Uri photoUri;

	private GoogleApiClient mGoogleApiClient;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private static final int RC_SIGN_IN = 9001;
	private static final String TAG = "MainActivity";
	private String idToken;
	public SharedPrefManager sharedPrefManager;
	private final Context mContext = this;

	TextView userNameTv;
	TextView userLastNameTv;
	TextView userEmailTv;
	ImageView avatarImgIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		fbBtn = (LoginButton) findViewById(R.id.main_regFacebookBtn);
		googleBtn = findViewById(R.id.main_regGoogleBtn);
		googleBtn.setSize(SignInButton.SIZE_WIDE);

		googleBtn.setOnClickListener(this);

		configureSignIn();

		mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

		userNameTv = (TextView) findViewById(R.id.userName);
		userLastNameTv = (TextView) findViewById(R.id.userLastname);
		userEmailTv = (TextView) findViewById(R.id.userEmail);
		avatarImgIv = (ImageView) findViewById(R.id.avatarImg);
		continueBtn = (Button) findViewById(R.id.continueBtn);


		continueBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, BelgradeActivity.class);
				startActivity(i);
			}
		});

		/*callbackManager = CallbackManager.Factory.create();

		List<String> permissionNeeds = Arrays.asList("user_photos", "email", "public_profile");
		fbBtn.setReadPermissions(permissionNeeds);
		fbBtn.registerCallback(callbackManager,new FacebookCallback<LoginResult>()

		{
			@Override
			public void onSuccess (LoginResult loginResult){
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("Retrieving data...");
				progressDialog.show();
				System.out.println("onSuccess");


				String accessToken = loginResult.getAccessToken().getToken();
				if (AccessToken.getCurrentAccessToken() != null) {
					Intent i = new Intent(MainActivity.this, ProfileActivity.class);
					startActivity(i);
				}


				final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {
						progressDialog.dismiss();
						Log.d("Response", response.toString());

						try {

							id = object.getString("id");
							//photoUri = new URL("https://graph.facebook.com/" + id + "/picture?width=250&height=250");
							if (object.has("email"))
								email = object.getString("email");
							if (object.has("first_name"))
								firstName = object.getString("first_name");
							if (object.has("last_name"))
								lastName = object.getString("last_name");
						} catch (JSONException e) {
							e.printStackTrace();
						} *//*catch (MalformedURLException e) {
							e.printStackTrace();
						}*//*
						String concat = firstName + " " + lastName;
						userNameTv.setText(concat);
						userLastNameTv.setText(lastName);

						Intent i = new Intent(MainActivity.this, ProfileActivity.class);
						i.putExtra("name", firstName);
						i.putExtra("surname", lastName);
						i.putExtra("email", email);
						i.putExtra("imageUri", photoUri.toString());
						i.putExtra("code", 1);
						startActivity(i);
						finish();
					}
				});

				Bundle parameters = new Bundle();
				parameters.putString("fields", "id, first_name, last_name, email, location");
				request.setParameters(parameters);
				request.executeAsync();

			}

			@Override
			public void onCancel () {
				System.out.println("onCancel");
			}

			@Override
			public void onError (FacebookException exception){
				Log.v("LoginActivity", exception.getCause().toString());
			}
		});
*/
		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					createUserInFirebaseHelper();
					Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
				} else {
					Log.d(TAG, "onAuthStateChanged:signed_out");
				}
			}
		};
	}

	private void createUserInFirebaseHelper() {

		final String encodedEmail = Utils.encodeEmail(email.toLowerCase());
		final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(encodedEmail);

		userLocation.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
			@Override
			public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
				if (dataSnapshot.getValue() == null) {
					HashMap<String, Object> timestampJoined = new HashMap<>();
					timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

					User newUser = new User(name, photo, encodedEmail, timestampJoined);
					userLocation.setValue(newUser);

					Toast.makeText(MainActivity.this, "Account created!", Toast.LENGTH_SHORT).show();

					// After saving data to Firebase, goto next activity
                   Intent intent = new Intent(MainActivity.this, NavDrawerActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);
                   finish();
				}
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {

				Log.d(TAG, getString(R.string.log_error_occurred) + firebaseError.getMessage());
				if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
				} else {
					Toast.makeText(MainActivity.this, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void configureSignIn() {
		GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(MainActivity.this.getResources().getString(R.string.web_client_id))
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(mContext)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, options)
				.build();
		mGoogleApiClient.connect();
	}

	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			if (result.isSuccess()) {
				GoogleSignInAccount account = result.getSignInAccount();

				idToken = account.getIdToken();

				name = account.getDisplayName();
				Log.d("IME", name);
				email = account.getEmail();
				photoUri = account.getPhotoUrl();
				photo = photoUri.toString();

				sharedPrefManager = new SharedPrefManager(mContext);
				sharedPrefManager.saveIsLoggedIn(mContext, true);

				sharedPrefManager.saveEmail(mContext, email);
				sharedPrefManager.saveName(mContext, name);
				sharedPrefManager.savePhoto(mContext, photo);

				sharedPrefManager.saveToken(mContext, idToken);
				sharedPrefManager.saveIsLoggedIn(mContext, true);

				AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
				firebaseAuthWithGoogle(credential);
			} else {
				Log.e(TAG, "Login Unsuccessful. ");
				Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void firebaseAuthWithGoogle(AuthCredential credential) {
		showProgressDialog();
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
						if (!task.isSuccessful()) {
							Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
							task.getException().printStackTrace();
							Toast.makeText(MainActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						} else {
							createUserInFirebaseHelper();
							Toast.makeText(MainActivity.this, "Login successful",
									Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(MainActivity.this, NavDrawerActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							finish();
						}
						hideProgressDialog();
					}
				});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mAuthListener != null) {
			FirebaseAuth.getInstance().signOut();
		}
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
	}

	@Override
	public void onConnectionSuspended(int i) {
	}

	@Override
	public void onClick(View view) {

		Utils utils = new Utils(this);
		int id = view.getId();

		if (id == R.id.main_regGoogleBtn) {
			if (utils.isNetworkAvailable()) {
				signIn();
			} else {
				Toast.makeText(MainActivity.this, "Oops! no internet connection!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
}



