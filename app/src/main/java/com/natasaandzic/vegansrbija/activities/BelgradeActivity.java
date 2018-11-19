package com.natasaandzic.vegansrbija.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.adapters.MyFragmentPagerAdapter;

public class BelgradeActivity extends AppCompatActivity {

	/**
	 * Za rad sa fragmentima, potreban nam je TabLayout, ViewPager i FragmentPagerAdapter
	 */
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private MyFragmentPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_belgrade);

		tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.addTab(tabLayout.newTab().setText("Restaurants"));
		tabLayout.addTab(tabLayout.newTab().setText("Shops"));
		tabLayout.addTab(tabLayout.newTab().setText("Maps"));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		viewPager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
		viewPager.setAdapter(adapter);

		tabLayout.setupWithViewPager(viewPager); // this will automatically bind tab clicks to viewpager fragments

	}
}
