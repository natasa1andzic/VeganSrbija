package com.natasaandzic.vegansrbija;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button loginBtn;
    Button emailRegisterBtn;
    Button fbBtn;
    Button googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button)findViewById(R.id.main_loginBtn);
        emailRegisterBtn = (Button)findViewById(R.id.main_emailRegisterBtn);
        fbBtn = (Button)findViewById(R.id.main_regFacebookBtn);
        googleBtn = (Button)findViewById(R.id.main_regGoogleBtn);

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

        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trazi permission
                //kada se dobije permission, prebaci intent na HomeActivity
                //podaci profila su podaci sa fejsbuka
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //trazi permission
                //kada se dobije permission, prebaci intent na HomeActivity
                //podaci profila su podaci gmail naloga
            }
        });

    }





}
