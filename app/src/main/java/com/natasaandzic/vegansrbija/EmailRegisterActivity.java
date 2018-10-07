package com.natasaandzic.vegansrbija;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmailRegisterActivity extends AppCompatActivity {

    Button backBtn;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);

        backBtn = (Button) findViewById(R.id.emailReg_BackBtn);
        nextBtn = (Button) findViewById(R.id.emailReg_NextBtn);

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
                Intent i = new Intent(EmailRegisterActivity.this, HomeActivity.class);
            }
        });

    }
}
