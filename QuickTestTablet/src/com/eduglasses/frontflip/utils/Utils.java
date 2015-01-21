package com.eduglasses.frontflip.utils;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	private static final String TAG = "EduFlip";

	/**
	 * Save value to the SharedPreferences
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveStringPreferences(Context context, String key,
			String value) {
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Delete String value saved in SharedPreferences
	 * 
	 * @param context
	 * @param key
	 *            as String type
	 */
	public static void deleteStringPreferences(Context context, String key) {
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sPrefs.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * Return String value saved in SharedPreferences
	 * 
	 * @param context
	 * @param key
	 *            as String type
	 * @return
	 */
	public static String getStringPreferences(Context context, String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String savedPref = sharedPreferences.getString(key, "");
		return savedPref;
	}

	/**
	 * Display Toast notification
	 * 
	 * @param context
	 * @param message
	 * @param length
	 *            e.g. Toast.LENGTH_LONG or Toast.LENGTH_SHORT
	 */
	public static void showToast(final Activity context, final String message,
			final int length) {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, message, length).show();
			}
		});
	}

	/**
	 * Simply perform an HTTP GET call
	 * 
	 * @param url
	 *            as String type
	 * @return JSONObject
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject makeRequest(String url)
			throws ClientProtocolException, IOException, JSONException {

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");

		// Execute HTTP Get Request
		HttpResponse httpResponse = httpclient.execute(httpGet);
		JSONObject jsonResponse = null;
		if (httpResponse != null) {
			// Convert HTTPResponse object to JSONObject
			jsonResponse = new JSONObject(EntityUtils.toString(httpResponse
					.getEntity()));
		}
		return jsonResponse;
	}

	/**
	 * Simply perform an HTTP POST call. The value of params should be null if
	 * you want to perform an HTTP POST call without parameter
	 * 
	 * @param url
	 *            as String type
	 * @param params
	 *            as JSONObject type
	 * @return JSONObject
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject makeRequest(String url, JSONObject params)
			throws ClientProtocolException, IOException, JSONException {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		if (params != null) {
			// passes the results to a string builder/entity
			StringEntity se = new StringEntity(params.toString());

			// sets the post request as the resulting string
			httppost.setEntity(se);
		}

		// sets a request header so the page receving the request
		// will know what to do with it
		httppost.setHeader("Accept", "application/json");
		httppost.setHeader("Content-type", "application/json");

		// Execute HTTP Post Request
		HttpResponse httpResponse = httpclient.execute(httppost);

		JSONObject jsonResponse = null;
		if (httpResponse != null) {
			// Convert HTTPResponse object to JSONObject
			jsonResponse = new JSONObject(EntityUtils.toString(httpResponse
					.getEntity()));
		}
		return jsonResponse;
	}

	/**
	 * Return the Glass Hardware ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getGlassSerial(Context context) {
		String prop = "ro.serialno.glass";
		String result = null;
		try {
			Class SystemProperties = context.getClassLoader().loadClass(
					"android.os.SystemProperties");

			Class[] paramtypes = new Class[1];
			paramtypes[0] = String.class;
			Object[] paramvalues = new Object[1];
			paramvalues[0] = new String(prop);
			Method get = SystemProperties.getMethod("get", paramtypes);

			result = (String) get.invoke(SystemProperties, paramvalues);
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	public static HttpResponse makePostRequest(String url, JSONObject params)
			throws ClientProtocolException, IOException {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		// passes the results to a string builder/entity
		StringEntity se = new StringEntity(params.toString());

		// sets the post request as the resulting string
		httppost.setEntity(se);

		// sets a request header so the page receving the request
		// will know what to do with it
		httppost.setHeader("Accept", "application/json");
		httppost.setHeader("Content-type", "application/json");

		// Execute HTTP Post Request
		return httpclient.execute(httppost);
	}

	public static void dLog(String message) {
		Log.d(TAG, message);
	}

	public static void eLog(String message) {
		Log.e(TAG, message);
	}

}
