package com.natasaandzic.vegansrbija.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.model.RestaurantsDataModel;

import java.util.List;


/**
 *
 * - RestaurantsArrayAdapter koristimo za punjenje liste objekata.
 * - ArrayAdapter funkcionise tako sto konstruktoru prosledimo context i listu objekata.
 * - Moramo da override-ujemo metodu getItem(position).
 * - Pravimo staticku unutrasnju klasu ViewHolder sa metodom ViewHolder create(layout),
 *   koja vraca sta nam sve ide u view kao (layout, textviews).
 * - Metoda getView uzima View, inflate-uje ga sa nasim layout_row_view i puni podacima iz ViewHolder-a.
 *
 */

public class RestaurantsArrayAdapter extends ArrayAdapter<RestaurantsDataModel>{

		List<RestaurantsDataModel> modelList;
		Context context;
		private LayoutInflater mInflater;

		// Constructors
		public RestaurantsArrayAdapter(Context context, List<RestaurantsDataModel> objects) {
			super(context, 0, objects);
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
			modelList = objects;
		}

		@Override
		public RestaurantsDataModel getItem(int position) {

			return modelList.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder vh;
			if (convertView == null) {
				View view = mInflater.inflate(R.layout.layout_row_view_restaurants, parent, false);
				vh = ViewHolder.create((RelativeLayout) view);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			RestaurantsDataModel item = getItem(position);

			vh.resNameTv.setText(item.getName());
			vh.resAddressTv.setText(item.getAddress());
			vh.resDistanceTv.setText(item.getDistance());

			return vh.rootView;
		}


		private static class ViewHolder {

			public final RelativeLayout rootView;

			public final TextView resNameTv;
			public final TextView resAddressTv;
			public final TextView resDistanceTv;

			private ViewHolder(RelativeLayout rootView, TextView resNameTv, TextView resAddressTv, TextView resDistanceTv) {
				this.rootView = rootView;
				this.resNameTv = resNameTv;
				this.resAddressTv = resAddressTv;
				this.resDistanceTv = resDistanceTv;
			}

			public static ViewHolder create(RelativeLayout rootView) {
				TextView resNameTv = (TextView) rootView.findViewById(R.id.restaurantNameTv);
				TextView resAddressTv = (TextView) rootView.findViewById(R.id.restaurantAddressTv);
				TextView resDistanceTv = (TextView) rootView.findViewById(R.id.resDistanceTv);
				return new ViewHolder(rootView, resNameTv, resAddressTv, resDistanceTv);
			}
		}
	}
