package com.natasaandzic.vegansrbija;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class EmailRegisterActivity extends AppCompatActivity {

    Button registerBackBtn;
    Button registerNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);
    }
}
