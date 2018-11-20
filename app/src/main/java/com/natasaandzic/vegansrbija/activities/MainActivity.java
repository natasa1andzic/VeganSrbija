package com.natasaandzic.vegansrbija.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.natasaandzic.vegansrbija.R;


import android.app.ProgressDialog;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.natasaandzic.vegansrbija.Utils.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	private LoginButton fbBtn;
	private SignInButton googleBtn;
	private Button continueBtn;
	Button signout;
	Button disc;

	private String idToken;
	public SharedPrefManager sharedPrefManager;
	private final Context mContext = this;


	ProgressDialog progressDialog;
	private String name, email;
	private String photo;
	private Uri photoUri;

	private TextView mStatusTextView;
	private TextView mDetailTextView;
	private TextView fbstatus;


	TextView userNameTv;
	TextView userLastNameTv;
	TextView userEmailTv;
	ImageView avatarImgIv;

	private static final String TAG = "GoogleActivity";
	private static final int RC_SIGN_IN = 9001;

	private FirebaseAuth mAuth;

	private GoogleSignInClient mGoogleSignInClient;

	CallbackManager callbackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		fbBtn = (LoginButton) findViewById(R.id.main_regFacebookBtn);
		googleBtn = findViewById(R.id.main_regGoogleBtn);
		googleBtn.setSize(SignInButton.SIZE_WIDE);


		userNameTv = (TextView) findViewById(R.id.userName);
		userLastNameTv = (TextView) findViewById(R.id.userLastname);
		userEmailTv = (TextView) findViewById(R.id.userEmail);
		avatarImgIv = (ImageView) findViewById(R.id.avatarImg);
		continueBtn = (Button) findViewById(R.id.continueBtn);

		// Views
		mStatusTextView = findViewById(R.id.status);
		mDetailTextView = findViewById(R.id.detail);
		fbstatus=findViewById(R.id.fbstatus);

		// Button listeners
		findViewById(R.id.main_regGoogleBtn).setOnClickListener(this);
		findViewById(R.id.signOutButton).setOnClickListener(this);
		findViewById(R.id.disconnectbtn).setOnClickListener(this);


		continueBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, BelgradeActivity.class);
				startActivity(i);
			}
		});

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		mAuth = FirebaseAuth.getInstance();

		callbackManager = CallbackManager.Factory.create();

		List<String> permissionNeeds = Arrays.asList("user_photos", "email", "public_profile");
		fbBtn.setReadPermissions(permissionNeeds);
		fbBtn.registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess (LoginResult loginResult) {
				handleFacebookAccessToken(loginResult.getAccessToken());
			}

			@Override
			public void onCancel() {
				updateUI(null);
			}

			@Override
			public void onError(FacebookException error) {
				Log.d(TAG, "facebook:onError", error);
				updateUI(null);
			}
		});
	}




	private void handleFacebookAccessToken(AccessToken token) {
		Log.d(TAG, "handleFacebookAccessToken:" + token);
		showProgressDialog();

		AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							Log.d(TAG, "signInWithCredential:success");
							FirebaseUser user = mAuth.getCurrentUser();
							updateUI(user);
						} else {
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							Toast.makeText(MainActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
							updateUI(null);
						}
						hideProgressDialog();
					}
				});
	}

	@Override
	public void onStart() {
		super.onStart();
		FirebaseUser currentUser = mAuth.getCurrentUser();
		updateUI(currentUser);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
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

				firebaseAuthWithGoogle(account);
			} catch(ApiException e) {
				Log.w(TAG, "Google sign in failed", e);
				updateUI(null);
			}
		}
		else
			callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
		showProgressDialog();

		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							Log.d(TAG, "signInWithCredential:success");
							FirebaseUser user = mAuth.getCurrentUser();
							Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							finish();
						} else {
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							Snackbar.make(findViewById(R.id.avatarImg), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
							updateUI(null);
						}
						hideProgressDialog();
					}
				});
	}

	private void signIn() {
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	private void signOut() {
		mAuth.signOut();

		mGoogleSignInClient.signOut().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						updateUI(null);
					}
				});
	}

	private void revokeAccess() {
		mAuth.signOut();

		// Google revoke access
		mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						updateUI(null);
					}
				});
	}

	private void updateUI(FirebaseUser user) {
		hideProgressDialog();
		if (user != null) {
			mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
			fbstatus.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));
			mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

			findViewById(R.id.main_regGoogleBtn).setVisibility(View.GONE);
			findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
		} else {
			mStatusTextView.setText("Signed out");
			mDetailTextView.setText(null);

			findViewById(R.id.main_regGoogleBtn).setVisibility(View.VISIBLE);
			findViewById(R.id.signOutButton).setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.main_regGoogleBtn) {
			signIn();
		} else if (i == R.id.signOutButton) {
			signOut();
		} else if (i == R.id.disconnectbtn) {
			revokeAccess();
		}
	}
}