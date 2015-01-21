package com.eduglasses.frontflip.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.eduglasses.frontflip.constants.Constants;

public class UpdateAnswerTask extends AsyncTask<String, String, String> {

	private ProgressDialog progress;

	@Override
	protected void onPreExecute() {
		// progress = ProgressDialog.show(, "Loading", "Please wait...");
		super.onPreExecute();
	}

	@Override
	protected void onCancelled() {
		if (progress != null) {
			progress.dismiss();
		}
		super.onCancelled();
	}

	@Override
	protected String doInBackground(String... params) {

		String responseString = "";

		Utils.dLog("URL: " + params[0]);
		Utils.dLog("Assessment Id: " + params[1]);
		Utils.dLog("Question Number: " + params[2]);
		Utils.dLog("Student Name: " + params[3]);
		Utils.dLog("Selected Answer: " + params[4]);

		try {

			// convert parameters into JSON object
			JSONObject holder = new JSONObject();
			holder.put(Constants.KEY_ASSESSMENT_ID, params[1]);
			holder.put(Constants.KEY_QUESTION_NUMBER, params[2]);
			holder.put(Constants.KEY_STUDENT_NAME, params[3]);
			holder.put(Constants.KEY_SELECTED_ANSWER, params[4]);

			// Execute HTTP Post Request
			HttpResponse response = Utils.makePostRequest(params[0], holder);
			if (response != null) {
				responseString = EntityUtils.toString(response.getEntity());
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		Utils.eLog("Response: " + result);
		super.onPostExecute(result);
		if (!"".equals(result)) {
			try {
				JSONObject response = new JSONObject(result);
				String message = response.optString("message");
				String statusCode = response.optString("statusCode");

				// fragment.onCompleteAnswerUpdate(holder, message, statusCode);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Utils.eLog("Getting empty response");
		}

		if (progress != null) {
			progress.dismiss();
		}
	}
}
