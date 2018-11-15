package com.natasaandzic.vegansrbija.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.splashxml)
        //Notice that we do not have setContentView() for this SplashActivity.
        // View is displaying from the theme and this way it is faster than creating a layout.

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
