package com.natasaandzic.vegansrbija.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.activities.MainActivity;
import com.natasaandzic.vegansrbija.model.RestaurantsDataModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.MyViewHolder> {


	private ArrayList<RestaurantsDataModel> modelList;

	public RestaurantsAdapter(ArrayList<RestaurantsDataModel> modelList) {
		this.modelList = modelList;
	}


	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_view_restaurants, parent, false);
		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		RestaurantsDataModel model = modelList.get(position);

		String resName = model.getName();
		String resAddress = model.getAddress();
		String resDistance = model.getDistance();
		String resLogoURL = model.getLogo();


		Log.d("URL SLIKE", resLogoURL);

		holder.resNameTv.setText(resName);
		holder.resAddressTv.setText(resAddress);
		holder.resDistanceTv.setText(resDistance);

		if (resLogoURL.isEmpty())
			holder.resLogoIv.setImageResource(R.drawable.veg);
		else
			Picasso.with(holder.resAddressTv.getContext()).load(resLogoURL).into(holder.resLogoIv);
	}
	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}


	@Override
	public int getItemCount() {
		return modelList.size();
	}

	public class MyViewHolder extends RecyclerView.ViewHolder{

		public final TextView resNameTv;
		public final TextView resAddressTv;
		public final TextView resDistanceTv;
		public final ImageView resLogoIv;

		private MyViewHolder(View itemView) {
			super(itemView);
			resNameTv = (TextView)itemView.findViewById(R.id.resNameTv);
			resAddressTv = (TextView)itemView.findViewById(R.id.resAddressTv);
			resDistanceTv = (TextView)itemView.findViewById(R.id.resDistanceTv);
			resLogoIv = (ImageView)itemView.findViewById(R.id.resLogoIv);
		}
	}


}


