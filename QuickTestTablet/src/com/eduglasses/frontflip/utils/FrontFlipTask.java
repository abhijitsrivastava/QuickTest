package com.eduglasses.frontflip.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.eduglasses.frontflip.LoginActivity;
import com.eduglasses.frontflip.PresentationActivity;
import com.eduglasses.frontflip.R;
import com.eduglasses.frontflip.model.LoginData;

public class FrontFlipTask extends AsyncTask<Object, Void, JSONArray> {
	private LoginActivity activity;
	private ProgressDialog progress;
	private String serviceUrl;
	
	public FrontFlipTask(LoginActivity activity,String serviceUrl) {
		super();
		this.activity = activity;
		this.serviceUrl = serviceUrl;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress = ProgressDialog.show(activity, activity.getString(R.string.app_name), "Validating Presentation Id, please wait...");
	}

	@Override
	protected JSONArray doInBackground(Object... params) {
		JSONArray jsonArray = null;
		try {
			JSONObject response = Utils.makeRequest(serviceUrl);
			if (response != null) {
				Utils.dLog("Successfully obtained FrontFlip data");
				jsonArray = response.getJSONArray("sections");
			}
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
		return jsonArray;
	}
	
	@Override
	protected void onPostExecute(JSONArray result) {
		
		super.onPostExecute(result);
		hideProgress();
		if(result == null) {
			Utils.eLog("JSONObject response is null");
			Utils.showToast(activity, "Invalid Prasentation Id, please try again...", Toast.LENGTH_LONG);
			activity.onValidationComplete(false);
		} else {
			activity.onValidationComplete(true);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		hideProgress();
	}
	
	private void hideProgress() {

		if (progress != null && progress.isShowing()) {
			progress.dismiss();
			progress = null;
		}
	}
}
