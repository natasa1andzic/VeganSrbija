package com.natasaandzic.vegansrbija.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.model.RestaurantsDataModel;

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

		holder.resNameTv.setText(resName);
		holder.resAddressTv.setText(resAddress);
		holder.resDistanceTv.setText(resDistance);

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

		private MyViewHolder(View itemView) {
			super(itemView);
			resNameTv = (TextView)itemView.findViewById(R.id.restaurantNameTv);
			resAddressTv = (TextView)itemView.findViewById(R.id.restaurantAddressTv);
			resDistanceTv = (TextView)itemView.findViewById(R.id.resDistanceTv);
		}


	}
}


