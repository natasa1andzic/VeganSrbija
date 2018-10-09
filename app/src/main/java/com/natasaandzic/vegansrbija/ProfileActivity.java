package com.natasaandzic.vegansrbija;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView userEmail;
    TextView userBday;
   // ImageView avatarImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userEmail = (TextView)findViewById(R.id.userEmail);
        userBday = (TextView)findViewById(R.id.userBday);
        //avatarImg = (ImageView)findViewById(R.id.avatarImg);
    }
}
