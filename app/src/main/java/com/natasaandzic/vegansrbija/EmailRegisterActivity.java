package com.natasaandzic.vegansrbija;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmailRegisterActivity extends AppCompatActivity {

    Button backBtn;
    Button nextBtn;

    EditText emailEt;
    EditText passwordEt;
    EditText repeatPasswordEt;

    String emailString;
    String passwordString;
    String repeatPasswordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);

        backBtn = (Button) findViewById(R.id.emailReg_BackBtn);
        nextBtn = (Button) findViewById(R.id.emailReg_NextBtn);
        emailEt = (EditText)findViewById(R.id.emailReg_EmailEt);
        passwordEt = (EditText)findViewById(R.id.emailReg_PasswordEt);
        repeatPasswordEt = (EditText)findViewById(R.id.emailReg_RepeatPasswordEt);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmailRegisterActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailString = emailEt.getText().toString().trim();
                passwordString = passwordEt.getText().toString().trim();
                repeatPasswordString = repeatPasswordEt.getText().toString().trim();

                if (passwordString == repeatPasswordString) {

                Intent i = new Intent(EmailRegisterActivity.this, ProfileActivity.class);
                i.putExtra("name", emailString);
                startActivity(i);
            }

            else
                    Toast.makeText(getApplicationContext(), "passwords do not match, try again",Toast.LENGTH_SHORT ).show();

        }});
        }


        }
