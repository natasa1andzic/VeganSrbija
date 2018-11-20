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
import com.facebook.login.LoginManager;
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

	private String idToken, name, email, photo, code;
	private Uri photoUri;
	public SharedPrefManager sharedPrefManager;
	private final Context mContext = this;

	private static final String TAG = "GoogleActivity";
	private static final int RC_SIGN_IN = 9001;
	private FirebaseAuth mAuth;
	private GoogleSignInClient mGoogleSignInClient;
	private CallbackManager callbackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		fbBtn = (LoginButton) findViewById(R.id.main_regFacebookBtn);
		googleBtn = findViewById(R.id.main_regGoogleBtn);
		googleBtn.setSize(SignInButton.SIZE_WIDE);

		continueBtn = (Button) findViewById(R.id.continueBtn);

		// Button listeners
		findViewById(R.id.main_regGoogleBtn).setOnClickListener(this);

		continueBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, BelgradeActivity.class);
				startActivity(i);
			}
		});

		mAuth = FirebaseAuth.getInstance();

		//Google sign-in
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		//Facebook sign-in
		callbackManager = CallbackManager.Factory.create();

		List<String> permissionNeeds = Arrays.asList("user_photos", "email", "public_profile");
		fbBtn.setReadPermissions(permissionNeeds);
		fbBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				Log.d(TAG, "handleFacebookAccessToken:" + loginResult.getAccessToken());
				showProgressDialog();

				AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
				mAuth.signInWithCredential(credential)
						.addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								if (task.isSuccessful()) {
									Log.d(TAG, "signInWithCredential:success");
									FirebaseUser user = mAuth.getCurrentUser();

									idToken = user.getUid();
									name = user.getDisplayName();
									Log.d("IME", name);
									email = user.getEmail();
									photoUri = user.getPhotoUrl();
									photo = photoUri.toString();
									code = "1";

									sharedPrefManager = new SharedPrefManager(mContext);
									sharedPrefManager.saveIsLoggedIn(mContext, true);

									sharedPrefManager.saveEmail(mContext, email);
									sharedPrefManager.saveName(mContext, name);
									sharedPrefManager.savePhoto(mContext, photo);
									sharedPrefManager.saveCode(mContext, code);

									sharedPrefManager.saveToken(mContext, idToken);
									sharedPrefManager.saveIsLoggedIn(mContext, true);
									hideProgressDialog();

									Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(intent);
									finish();
								} else {
									Log.w(TAG, "signInWithCredential:failure", task.getException());
									Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
									hideProgressDialog();
								}
								hideProgressDialog();
							}
						});
			}

			@Override
			public void onCancel() {
				hideProgressDialog();
			}

			@Override
			public void onError(FacebookException error) {
				Log.d(TAG, "facebook:onError", error);
				hideProgressDialog();
			}
		});
	}

	//Isto je i za fb i za google
	@Override
	public void onStart() {
		super.onStart();
		FirebaseUser currentUser = mAuth.getCurrentUser();
		hideProgressDialog();
	}

	//Google sign-in, prvi if, a drugi if je fb login
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
				code = "2";

				sharedPrefManager = new SharedPrefManager(mContext);
				sharedPrefManager.saveIsLoggedIn(mContext, true);

				sharedPrefManager.saveEmail(mContext, email);
				sharedPrefManager.saveName(mContext, name);
				sharedPrefManager.savePhoto(mContext, photo);
				sharedPrefManager.saveCode(mContext, code);

				sharedPrefManager.saveToken(mContext, idToken);
				sharedPrefManager.saveIsLoggedIn(mContext, true);

				firebaseAuthWithGoogle(account);
			} catch (ApiException e) {
				Log.w(TAG, "Google sign in failed", e);
				hideProgressDialog();
			}
		} else {
			callbackManager.onActivityResult(requestCode, resultCode, data);

		}
	}

	//Google sign-in
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
							hideProgressDialog();
						}
						hideProgressDialog();
					}
				});
	}

	//Google sign-in
	private void signInWithGoogle() {
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	private void signOutFromGoogle() {
		mAuth.signOut();
		mGoogleSignInClient.signOut().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						hideProgressDialog();
					}
				});
	}

	private void disconnectFromGoogle() {
		mAuth.signOut();
		mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						hideProgressDialog();
					}
				});
	}

	public void signOutFromFB() {
		mAuth.signOut();
		LoginManager.getInstance().logOut();
		hideProgressDialog();
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.main_regGoogleBtn) {
			signInWithGoogle();
		}
	}
}