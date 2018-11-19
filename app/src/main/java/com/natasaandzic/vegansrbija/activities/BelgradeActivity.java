package com.natasaandzic.vegansrbija.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.adapters.MyFragmentPagerAdapter;
import com.natasaandzic.vegansrbija.fragments.MapsFragment;
import com.natasaandzic.vegansrbija.fragments.ProfileFragment;
import com.natasaandzic.vegansrbija.fragments.RestaurantsFragment;
import com.natasaandzic.vegansrbija.fragments.ShopsFragment;

public class BelgradeActivity extends AppCompatActivity{

	final Fragment fragment1 = new RestaurantsFragment();
	final Fragment fragment2 = new ShopsFragment();
	final Fragment fragment3 = new MapsFragment();
	final Fragment fragment4 = new ProfileFragment();
	final FragmentManager fm = getSupportFragmentManager();
	Fragment active = fragment1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_belgrade);

		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		navigation.setItemIconTintList(null);

		fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
		fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
		fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit();
	}

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navRestaurants:
					fm.beginTransaction().hide(active).show(fragment1).commit();
					active = fragment1;
					return true;

				case R.id.navShops:
					fm.beginTransaction().hide(active).show(fragment2).commit();
					active = fragment2;
					return true;

				case R.id.navMaps:
					fm.beginTransaction().hide(active).show(fragment3).commit();
					active = fragment3;
					return true;
			}
			return false;
		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			startActivity(new Intent(BelgradeActivity.this, ProfileActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
