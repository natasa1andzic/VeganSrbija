package com.natasaandzic.vegansrbija.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.natasaandzic.vegansrbija.fragments.MapsFragment;
import com.natasaandzic.vegansrbija.fragments.RestaurantsFragment;
import com.natasaandzic.vegansrbija.fragments.ShopsFragment;

/**
 * FragmentPagerAdapter koristimo za fragmente, da prikazemo listu naziva fragmenata,
 * Moramo da override-ujemo metode getPageTitle, getItem i getCount.
 * U konstruktoru prosledjujemo FragmentManager i numOfTabs.
 */
public class MyFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter{

	private String[] tabTitles = new String[]{"Restaurants", "Shops", "Maps"};
	int mNumOfTabs;

	public MyFragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
		super(fm);
		this.mNumOfTabs = NumOfTabs;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabTitles[position];
	}

	@Override
	public Fragment getItem(int position) {

		switch (position) {
			case 0:
				RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
				return restaurantsFragment;
			case 1:
				ShopsFragment shopsFragment = new ShopsFragment();
				return shopsFragment;
			case 2:
				MapsFragment mapsFragment = new MapsFragment();
				return mapsFragment;
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return mNumOfTabs;
	}
}

