package com.natasaandzic.vegansrbija.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.model.ShopsDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.MyViewHolder> {

	private ArrayList<ShopsDataModel> modelList;

	public ShopsAdapter(ArrayList<ShopsDataModel> modelList) {
		this.modelList = modelList;
	}


	@NonNull
	@Override
	public ShopsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_view_shops, parent, false);
		return new ShopsAdapter.MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ShopsAdapter.MyViewHolder holder, int position) {
		ShopsDataModel model = modelList.get(position);

		String shopName = model.getName();
		String shopAddress = model.getAddress();
		String shopDistance = model.getDistance();
		String shopLogoURL = model.getLogo();

		Log.d("URL SLIKE", shopLogoURL);

		holder.shopNameTv.setText(shopName);
		holder.shopAddressTv.setText(shopAddress);
		holder.shopDistanceTv.setText(shopDistance);

		if (shopLogoURL.isEmpty())
			holder.shopLogoIV.setImageResource(R.drawable.veg);
		else
			Picasso.with(holder.shopNameTv.getContext()).load(shopLogoURL).into(holder.shopLogoIV);
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

		public final TextView shopNameTv;
		public final TextView shopAddressTv;
		public final TextView shopDistanceTv;
		public final ImageView shopLogoIV;

		private MyViewHolder(View itemView) {
			super(itemView);
			shopNameTv = (TextView)itemView.findViewById(R.id.shopNameTv);
			shopAddressTv = (TextView)itemView.findViewById(R.id.shopAddressTv);
			shopDistanceTv = (TextView)itemView.findViewById(R.id.shopDistanceTv);
			shopLogoIV = (ImageView)itemView.findViewById(R.id.shopLogoIv);
		}
	}

}
