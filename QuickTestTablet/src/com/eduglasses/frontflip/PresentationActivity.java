package com.eduglasses.frontflip;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

	private RelativeLayout relativeLayoutVideoView;
	private VideoView videoView;
	private LinearLayout linearLayoutWebViewPresentation;

	private RelativeLayout linearLayoutPlayStore;
	private ImageButton imageButtonPlayStore;

	private int REQUEST_CODE_FOR_MEDIA_PLAYER = 1;
	private int REQUEST_CODE_FOR_PLAY_STORE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presentation);
		activity = this;

		studentId = LoginData.getInstance().getStudentId();

		webView = (WebView) findViewById(R.id.webViewPresentation);
		imageViewUnableToConnect = (ImageView) findViewById(R.id.imageViewUnableToConnect);

		relativeLayoutVideoView = (RelativeLayout) findViewById(R.id.linearLayoutVideoView);
		videoView = (VideoView) findViewById(R.id.videoView);
		
		linearLayoutPlayStore = (RelativeLayout) findViewById(R.id.linearLayoutPlayStore);
		imageButtonPlayStore = (ImageButton) findViewById(R.id.imageButtonPlayStore);
		imageButtonPlayStore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					Intent i = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("https://play.google.com/store/apps/details?id=org.videolan.vlc.betav7neon&hl=en"));
					startActivityForResult(i, REQUEST_CODE_FOR_PLAY_STORE);
			}
		});
		
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
						Constants.BASE_URL_EDUGRADE + Constants.SERVICE_UPDATE_ANSWER,
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
						Constants.BASE_URL_EDUGRADE + Constants.SERVICE_UPDATE_ANSWER,
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
						Constants.BASE_URL_EDUGRADE + Constants.SERVICE_UPDATE_ANSWER,
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
						Constants.BASE_URL_EDUGRADE + Constants.SERVICE_UPDATE_ANSWER,
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
						Constants.BASE_URL_EDUGRADE + Constants.SERVICE_UPDATE_ANSWER,
						assessmentId + "", questionNumber + "", studentId, "E" };
				task = new UpdateAnswerTask();
				task.execute(paramsE);
				break;

			default:
				break;
			}
			Toast.makeText(activity, "you answered : " + answer,
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
		}else{
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
		/*
		 * JSONObject json = new JSONObject(); try { json.put("type",
		 * Constants.FLAG_MQTT_CONNECTED); json.put("url", ""); } catch
		 * (JSONException e) { e.printStackTrace(); } ((Session)
		 * getApplication()).setMqttConnection(mqttConnection);
		 * mqttConnection.publishMessage(json.toString(), topic);
		 */
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
				/*
				 * runOnUiThread(new Runnable() {
				 * 
				 * @Override public void run() { // TODO Auto-generated method
				 * stub Utils.dLog("EDUTEACH_LAUNCH");
				 * relativeLayoutVideoView.setVisibility(View.VISIBLE); } });
				 */
				break;

			case "EDUTEACH_START_STREAMING":

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String url = messageJSONObject.optString("url");
						String rtmpUrl;
						rtmpUrl = URLDecoder.decode(url);
						/*
						 * relativeLayoutVideoView.setVisibility(View.VISIBLE);
						 * linearLayoutWebViewPresentation
						 * .setVisibility(View.GONE);
						 */
						Utils.dLog("decodedUrl : " + rtmpUrl);
						String rtspUrl = rtmpUrl.replace("rtmp", "rtsp");
						Utils.dLog("rtspUrl : " + rtspUrl);
						Toast.makeText(activity, rtspUrl, Toast.LENGTH_LONG)
								.show();
						/*
						 * videoView.setVideoURI(Uri.parse(
						 * "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
						 * )); videoView.requestFocus(); videoView.start();
						 */

						/*
						 * Intent i = new Intent(activity, WebViewPlayer.class);
						 * i.putExtra("url", decodedUrl); startActivity(i);
						 */
						try {
							Intent i = new Intent(
									android.content.Intent.ACTION_VIEW);
							i.setData(Uri.parse(rtmpUrl));
							startActivityForResult(i,
									REQUEST_CODE_FOR_MEDIA_PLAYER);
						} catch (android.content.ActivityNotFoundException anfe) {
							/*
							 * linearLayoutWebViewPresentation
							 * .setVisibility(View.GONE);
							 */
							linearLayoutPlayStore.setVisibility(View.VISIBLE);
							
						}
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
						if (relativeLayoutVideoView.getVisibility() == View.VISIBLE) {
							relativeLayoutVideoView.setVisibility(View.GONE);
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

	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CODE_FOR_MEDIA_PLAYER) {
			/*
			 * if(resultCode == RESULT_OK){ String
			 * result=data.getStringExtra("result"); } if (resultCode ==
			 * RESULT_CANCELED) { //Write your code if there's no result }
			 */
		}
		if (requestCode == REQUEST_CODE_FOR_PLAY_STORE) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					linearLayoutPlayStore.setVisibility(View.GONE);
				}
			});

			/*
			 * if(resultCode == RESULT_OK){ String
			 * result=data.getStringExtra("result"); } if (resultCode ==
			 * RESULT_CANCELED) { //Write your code if there's no result }
			 */
		}
	}
}

/*
 * ////////////////////////// 1st Technique //////////////////// //enable
 * Javascript myWebView.getSettings().setJavaScriptEnabled(true);
 * 
 * //loads the WebView completely zoomed out
 * myWebView.getSettings().setLoadWithOverviewMode(true);
 * 
 * //true makes the Webview have a normal viewport such as a normal desktop
 * browser //when false the webview will have a viewport constrained to it's own
 * dimensions myWebView.getSettings().setUseWideViewPort(true);
 * mqttCallbackListener //override the web client to open all links in the same
 * webview myWebView.setWebViewClient(new MyWebViewClient());
 * myWebView.setWebChromeClient(new MyWebChromeClient());
 * 
 * //Injects the supplied Java object into this WebView. The object is injected
 * into the //JavaScript context of the main frame, using the supplied name.
 * This allows the //Java object's public methods to be accessed from
 * JavaScript. myWebView.addJavascriptInterface(new JavaScriptInterface(this),
 * "Android");
 * 
 * //load the home page URL myWebView.loadUrl(Constant.PRESENTATION_URL +
 * presentationId); ////////////////////////// 1st Technique
 * ////////////////////
 */

/*
 * //customize your web view client to open links from your own site in the
 * //same web view otherwise just open the default browser activity with the URL
 * private class MyWebViewClient extends WebViewClient {
 * 
 * @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
 * if (Uri.parse(url).getHost().equals("demo.mysamplecode.com")) { return false;
 * } Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
 * startActivity(intent); return true; } }
 * 
 * private class MyWebChromeClient extends WebChromeClient {
 * 
 * //display alert message in Web View
 * 
 * @Override public boolean onJsAlert(WebView view, String url, String message,
 * JsResult result) { Log.d(LOG_TAG, message); new
 * AlertDialog.Builder(view.getContext())
 * .setMessage(message).setCancelable(true).show(); result.confirm(); return
 * true; }
 * 
 * }
 * 
 * public class JavaScriptInterface { Context mContext;
 * 
 * // Instantiate the interface and set the context JavaScriptInterface(Context
 * c) { mContext = c; }
 * 
 * //using Javascript to call the finish activity public void closeMyActivity()
 * { finish(); }
 * 
 * }
 * 
 * //Web view has record of all pages visited so you can go back and forth
 * //just override the back button to go back in history if there is page
 * //available for display
 * 
 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
 * ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
 * myWebView.goBack(); return true; } return super.onKeyDown(keyCode, event); }
 */

