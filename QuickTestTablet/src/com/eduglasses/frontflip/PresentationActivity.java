package com.eduglasses.frontflip;

import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eduglasses.frontflip.constants.Constants;
import com.eduglasses.frontflip.messaging.MQTTCallbackListener;
import com.eduglasses.frontflip.messaging.MQTTConnection;
import com.eduglasses.frontflip.model.LoginData;
import com.eduglasses.frontflip.utils.UpdateAnswerTask;
import com.eduglasses.frontflip.utils.Utils;

public class PresentationActivity extends Activity {

	private WebView webView;// , myWebView;
	private Activity activity;
	private String presentationId;
	private ImageView imageViewUnableToConnect;

	private String LOG_TAG = "PresentationActivity";

	private boolean isConnected;
	private String topic;
	private MQTTConnection mqttConnection;
	private MQTTCallbackListener mqttCallbackListener;

	private LinearLayout linearLayoutAnswerSheet;
	private TextView textviewQuestionNo;
	private Button btnA;
	private Button btnB;
	private Button btnC;
	private Button btnD;
	private Button btnE;

	private String assessmentId;
	private String teacherId;
	private String serial;
	private String answers;
	private String questionNumber;

	private String studentId;

	private UpdateAnswerTask task;

	private Animation fadeIn, fadeOut;
	private LinearLayout linearLayoutWebViewPresentation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presentation);
		activity = this;

		studentId = LoginData.getInstance().getStudentId();

		webView = (WebView) findViewById(R.id.webViewPresentation);
		imageViewUnableToConnect = (ImageView) findViewById(R.id.imageViewUnableToConnect);

		linearLayoutWebViewPresentation = (LinearLayout) findViewById(R.id.linearLayoutWebViewPresentation);

		linearLayoutAnswerSheet = (LinearLayout) findViewById(R.id.linearLayoutAnswerSheet);
		textviewQuestionNo = (TextView) findViewById(R.id.textviewQuestionNo);
		btnA = (Button) findViewById(R.id.btnA);
		btnB = (Button) findViewById(R.id.btnB);
		btnC = (Button) findViewById(R.id.btnC);
		btnD = (Button) findViewById(R.id.btnD);
		btnE = (Button) findViewById(R.id.btnE);

		topic = Constants.TOPIC_SLIDE_NUMBER
				+ LoginData.getInstance().getLessonId();

		btnA.setOnClickListener(responseListener);
		btnB.setOnClickListener(responseListener);
		btnC.setOnClickListener(responseListener);
		btnD.setOnClickListener(responseListener);
		btnE.setOnClickListener(responseListener);

		mqttCallbackListener = new MQTTCallbackListener() {

			@Override
			public void onReceiveMessage(byte[] messageBytes) {
				processMessage(messageBytes);
			}
		};

		// load animations
		fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in);
		fadeOut = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_out);
	}

	private OnClickListener responseListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String answer = "";
			switch (v.getId()) {
			case R.id.btnA:
				// start fade in animation
				// linearLayoutAnswerSheet.startAnimation(fadeOut);
				answer = "A";
				linearLayoutAnswerSheet.setVisibility(View.GONE);
				String[] paramsA = {
						Constants.BASE_URL_EDUGRADE
								+ Constants.SERVICE_UPDATE_ANSWER,
						assessmentId + "", questionNumber + "", studentId, "A" };
				task = new UpdateAnswerTask();
				task.execute(paramsA);
				break;
			case R.id.btnB:
				// start fade in animation
				// linearLayoutAnswerSheet.startAnimation(fadeOut);
				answer = "B";
				linearLayoutAnswerSheet.setVisibility(View.GONE);
				String[] paramsB = {
						Constants.BASE_URL_EDUGRADE
								+ Constants.SERVICE_UPDATE_ANSWER,
						assessmentId + "", questionNumber + "", studentId, "B" };
				task = new UpdateAnswerTask();
				task.execute(paramsB);
				break;

			case R.id.btnC:
				// start fade in animation
				// linearLayoutAnswerSheet.startAnimation(fadeOut);
				answer = "C";
				linearLayoutAnswerSheet.setVisibility(View.GONE);
				String[] paramsC = {
						Constants.BASE_URL_EDUGRADE
								+ Constants.SERVICE_UPDATE_ANSWER,
						assessmentId + "", questionNumber + "", studentId, "C" };
				task = new UpdateAnswerTask();
				task.execute(paramsC);
				break;

			case R.id.btnD:
				// start fade in animation
				// linearLayoutAnswerSheet.startAnimation(fadeOut);
				answer = "D";
				linearLayoutAnswerSheet.setVisibility(View.GONE);
				String[] paramsD = {
						Constants.BASE_URL_EDUGRADE
								+ Constants.SERVICE_UPDATE_ANSWER,
						assessmentId + "", questionNumber + "", studentId, "D" };
				task = new UpdateAnswerTask();
				task.execute(paramsD);
				break;

			case R.id.btnE:
				// start fade in animation
				// linearLayoutAnswerSheet.startAnimation(fadeOut);
				answer = "E";
				linearLayoutAnswerSheet.setVisibility(View.GONE);
				String[] paramsE = {
						Constants.BASE_URL_EDUGRADE
								+ Constants.SERVICE_UPDATE_ANSWER,
						assessmentId + "", questionNumber + "", studentId, "E" };
				task = new UpdateAnswerTask();
				task.execute(paramsE);
				break;

			default:
				break;
			}
			Toast.makeText(activity, "You Answered : " + answer,
					Toast.LENGTH_LONG).show();

		}
	};

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Utils.dLog("OnStart");

		if (mqttConnection != null) {
			onMqttConnected(mqttConnection);
		} else {
			mqttConnection = new MQTTConnection(PresentationActivity.this,
					mqttCallbackListener, topic);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Utils.dLog("onResume");
		presentationId = LoginData.getInstance().getLessonId();

		// //////////////////////// 2nd Technique ////////////////////
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setTitle("Loading...");
				activity.setProgress(progress * 100);

				if (progress < 100) {

					// myWebView.setVisibility(View.VISIBLE);
				}
				if (progress == 100) {
					activity.setTitle(R.string.app_name);
					imageViewUnableToConnect.setVisibility(View.GONE);
					webView.setVisibility(View.VISIBLE);
				}
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				Utils.dLog("Error Occured while loading the url");

				webView.setVisibility(View.GONE);
				imageViewUnableToConnect.setVisibility(View.VISIBLE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		/*
		 * Toast.makeText(getApplicationContext(), Constants.PRESENTATION_URL +
		 * presentationId, Toast.LENGTH_LONG) .show();
		 */

		webView.loadUrl(Constants.PRESENTATION_URL + presentationId);
		webView.setVerticalScrollBarEnabled(true);

		// //////////////////////// 2nd Technique ////////////////////

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Utils.dLog("onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mqttConnection != null) {
			mqttConnection.close();
			mqttConnection = null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.presentation_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.refresh) {
			webView.loadUrl(Constants.PRESENTATION_URL + presentationId);
		}
		return super.onOptionsItemSelected(item);
	}

	public void onMqttConnected(MQTTConnection mqttConnection) {

		Utils.dLog("MQTT Connected");
		isConnected = true;
	}

	JSONObject messageJSONObject;

	private void processMessage(byte[] messageBytes) {
		// TODO Auto-generated method stub
		/*
		 * Toast.makeText(PresentationActivity.this, "Msg Received on Topic",
		 * Toast.LENGTH_LONG) .show();
		 */

		String messageString = new String(messageBytes);
		Utils.dLog("Msg Received  : " + messageString);

		try {
			messageJSONObject = new JSONObject(messageString);
			String type = messageJSONObject.optString("type");
			Utils.dLog("type : " + type);

			switch (type) {
			case "ASSESSMENT_CREATE":
				assessmentId = messageJSONObject.optString("assessmentId");
				teacherId = messageJSONObject.optString("teacherId");
				serial = messageJSONObject.optString("serial");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Utils.dLog("inside ASSESSMENT_CREATE");
						linearLayoutAnswerSheet.setVisibility(View.GONE);
					}
				});

				break;

			case "QUESTION_START":
				assessmentId = messageJSONObject.optString("assessmentId");
				teacherId = messageJSONObject.optString("teacherId");
				serial = messageJSONObject.optString("serial");
				questionNumber = messageJSONObject.optString("questionNumber");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Utils.dLog("inside QUESTION_START");
						textviewQuestionNo.setText("Question #"
								+ questionNumber);
						linearLayoutAnswerSheet.setVisibility(View.VISIBLE);
						// start fade in animation
						// linearLayoutAnswerSheet.startAnimation(fadeIn);

					}
				});

				break;

			case "QUESTION_STOP":
				assessmentId = messageJSONObject.optString("assessmentId");
				teacherId = messageJSONObject.optString("teacherId");
				serial = messageJSONObject.optString("serial");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Utils.dLog("inside QUESTION_STOP");
						if (linearLayoutAnswerSheet.getVisibility() == View.VISIBLE) {
							linearLayoutAnswerSheet.setVisibility(View.GONE);
							// linearLayoutAnswerSheet.startAnimation(fadeOut);
						}
					}
				});

				break;

			case "EDUGRADE_OPEN_CHART":
				answers = messageJSONObject.optString("answers");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Utils.dLog("inside QUESTION_STOP");
						if (linearLayoutAnswerSheet.getVisibility() == View.VISIBLE) {
							linearLayoutAnswerSheet.setVisibility(View.GONE);
							// linearLayoutAnswerSheet.startAnimation(fadeOut);
						}
					}
				});
				break;

			case "EDUTEACH_LAUNCH":
				break;

			case "EDUTEACH_START_STREAMING":

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String url = messageJSONObject.optString("url");
						String rtmpUrl;
						rtmpUrl = URLDecoder.decode(url);

						Utils.dLog("decodedUrl : " + rtmpUrl);
						String rtspUrl = rtmpUrl.replace("rtmp", "rtsp");
						Utils.dLog("rtspUrl : " + rtspUrl);
						Toast.makeText(activity, rtspUrl, Toast.LENGTH_LONG)
								.show();
						/*
						 * try { Intent i = new Intent(
						 * android.content.Intent.ACTION_VIEW);
						 * i.setData(Uri.parse(rtmpUrl));
						 * startActivityForResult(i,
						 * REQUEST_CODE_FOR_MEDIA_PLAYER); } catch
						 * (android.content.ActivityNotFoundException anfe) {
						 * linearLayoutPlayStore.setVisibility(View.VISIBLE); }
						 */
					}
				});
				break;

			case "CONNECTED":

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Utils.dLog("inside CONNECTED");
						if (linearLayoutAnswerSheet.getVisibility() == View.VISIBLE) {
							linearLayoutAnswerSheet.setVisibility(View.GONE);
							// linearLayoutAnswerSheet.startAnimation(fadeOut);
						}
					}
				});
				break;

			default:
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Utils.dLog("inside QUESTION_STOP");
						if (linearLayoutAnswerSheet.getVisibility() == View.VISIBLE) {
							linearLayoutAnswerSheet.setVisibility(View.GONE);
							// linearLayoutAnswerSheet.startAnimation(fadeOut);
						}
					}
				});
				break;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
