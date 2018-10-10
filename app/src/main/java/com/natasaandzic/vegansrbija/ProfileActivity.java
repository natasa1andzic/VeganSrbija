package com.natasaandzic.vegansrbija;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    ImageView avatar;
    TextView userName;
    TextView userLastName;
    TextView userEmail;
    TextView userBday;

    Bundle mBundle;

    String avatarString;
    String nameString;
    String lastNameString;
    String bdayString;
    String emailString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        avatar = (ImageView)findViewById(R.id.profileAvatar);
        userName = (TextView)findViewById(R.id.profileName);
       // userLastName = (TextView)findViewById(R.id.profileLastname);
        userBday = (TextView)findViewById(R.id.profileBday);
        userEmail = (TextView)findViewById(R.id.profileEmail);

        mBundle = getIntent().getExtras();

        avatarString = mBundle.getString("imageUrl");
        nameString = mBundle.getString("name");
        lastNameString = mBundle.getString("surname");
        bdayString = mBundle.getString("birthday");
        emailString = mBundle.getString("email");

        userName.setText(nameString);
        //userLastName.setText(lastNameString);
        userEmail.setText(emailString);
        userBday.setText(bdayString);

        Glide
                .with(this)
                .load(avatarString)
                .override(250, 250)
                .into(avatar);
    }

}
