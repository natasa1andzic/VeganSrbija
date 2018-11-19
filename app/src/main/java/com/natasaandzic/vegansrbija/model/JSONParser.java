package com.natasaandzic.vegansrbija.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JSONParser {

	private static Response response;

	public static JSONObject getDataFromWeb(String url) {
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			response = client.newCall(request).execute();
			return new JSONObject(response.body().string());
		} catch (@NonNull IOException | JSONException e) {
			Log.e(url, "" + e.getLocalizedMessage());
		}
		return null;
	}
}
