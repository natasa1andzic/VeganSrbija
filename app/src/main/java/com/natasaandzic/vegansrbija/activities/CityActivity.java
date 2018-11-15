package com.natasaandzic.vegansrbija.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.natasaandzic.vegansrbija.R;

public class CityActivity extends AppCompatActivity {

	private Button beogradBtn;
	private Button noviSadBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);

		beogradBtn = (Button)findViewById(R.id.beogradBtn);
		noviSadBtn = (Button)findViewById(R.id.noviSadBtn);

		beogradBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(CityActivity.this, BelgradeActivity.class);
				startActivity(i);
			}
		}
		);

		noviSadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(CityActivity.this, NoviSadActivity.class);
				startActivity(i);
			}
		});
	}
}
