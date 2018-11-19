package com.natasaandzic.vegansrbija.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.natasaandzic.vegansrbija.R;

import static android.view.View.VISIBLE;


public class ProfileActivity extends AppCompatActivity {

    ImageView avatar;
    TextView userName;
    TextView userLastName;
    TextView userEmail;

    Bundle mBundle;

    String avatarString;
    String nameString;
    String lastNameString;
    String bdayString;
    String emailString;

    Button backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        avatar = (ImageView)findViewById(R.id.profileAvatar);
        userName = (TextView)findViewById(R.id.profileName);
        userLastName = (TextView)findViewById(R.id.profileLastname);
        userEmail = (TextView)findViewById(R.id.profileEmail);

        mBundle = getIntent().getExtras();

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

}
