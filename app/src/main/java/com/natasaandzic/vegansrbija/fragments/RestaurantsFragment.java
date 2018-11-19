package com.natasaandzic.vegansrbija.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.natasaandzic.vegansrbija.R;
import com.natasaandzic.vegansrbija.adapters.RestaurantsAdapter;
import com.natasaandzic.vegansrbija.model.InternetConnection;
import com.natasaandzic.vegansrbija.model.JSONParser;
import com.natasaandzic.vegansrbija.model.Keys;
import com.natasaandzic.vegansrbija.model.RestaurantsDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RestaurantsFragment extends Fragment {

	private RecyclerView recyclerView;
	private LinearLayoutManager lm;

	private ArrayList<RestaurantsDataModel> list;
	private RestaurantsAdapter adapter;

	public Toast toastMsg;

	private static final String RESTAURANTS_URL = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1z4RPtR6SnN6ejQZgimVejrLfzPt2kemDYo5A5aVTfHA&sheet=Sheet1";

	public RestaurantsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_restaurants, container, false);


		recyclerView = (RecyclerView) view.findViewById(R.id.myRecyclerView);
		recyclerView.setHasFixedSize(true);
		lm = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(lm);
		DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), lm.getOrientation());
		recyclerView.addItemDecoration(mDividerItemDecoration);

		list = new ArrayList<>();
		adapter = new RestaurantsAdapter(list);

		recyclerView.setAdapter(adapter);

		if (InternetConnection.checkConnection(getContext()))
			new RestaurantsFragment.GetDataTask().execute();
		else
			toastMsg.makeText(getContext(), "Internet connection is not available", Toast.LENGTH_LONG).show();

		return view;
	}

	/**
	 * Getting JSON data from the internet,
	 * converting it to strings,
	 * filling our textviews with those strings.
	 */
	class GetDataTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;
		int jIndex;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			jIndex = list.size();

			dialog = new ProgressDialog(getActivity());
			dialog.setTitle("Reading from database...");
			dialog.setMessage("Go make some coffee ^_^");
			dialog.show();
		}

		@Nullable
		@Override
		protected Void doInBackground(Void... params) {
			JSONObject jsonObject = JSONParser.getDataFromWeb(RESTAURANTS_URL);
			try {
				if (jsonObject != null) {
					if (jsonObject.length() > 0) {
						JSONArray array = jsonObject.getJSONArray(Keys.KEY_RESTAURANTS_BELGRADE);
						int lenArray = array.length();
						if (lenArray > 0) {
							for (; jIndex < lenArray; jIndex++) {

								RestaurantsDataModel model = new RestaurantsDataModel();
								JSONObject innerObject = array.getJSONObject(jIndex);
								String resName = innerObject.getString(Keys.KEY_RES_NAME);
								String resAddress = innerObject.getString(Keys.KEY_RES_ADDRESS);
								String resLogo = innerObject.getString(Keys.KEY_RES_LOGO);
								model.setName(resName);
								model.setAddress(resAddress);
								model.setLogo(resLogo);

								list.add(model);
							}
						}
					}
				}
			} catch (JSONException je) {
				Log.i("Events url", "" + je.getLocalizedMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			dialog.dismiss();
			if (list.size() > 0) {
				adapter.notifyDataSetChanged();
			} else {
				//Snackbar.make(findViewById(R.id.parentFrameLayout), "No Data Found", Snackbar.LENGTH_LONG).show();
			}
		}
	}

	private void makeDialog(int position) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
		alertDialogBuilder.setTitle(list.get(position).getName());
		alertDialogBuilder.setMessage(list.get(position).getAddress());

		alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
}

