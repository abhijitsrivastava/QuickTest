package com.eduglasses.frontflip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.eduglasses.frontflip.constants.Constants;
import com.eduglasses.frontflip.model.LoginData;
import com.eduglasses.frontflip.utils.FrontFlipTask;

public class LoginActivity extends Activity {

	private Button loginButton;

	private EditText editTextPresentationId;
	private EditText editTextStudentId;

	private String presentationId;
	private String studentId;
	private LinearLayout linearLayoutPlayStore;
	private ImageButton imageButtonPlayStore;
	private int REQUEST_CODE_FOR_PLAY_STORE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		editTextPresentationId = (EditText) findViewById(R.id.editTextPresentationId);
		loginButton = (Button) findViewById(R.id.buttonLogin);
		editTextStudentId = (EditText) findViewById(R.id.editTextStudentId);

		linearLayoutPlayStore = (LinearLayout) findViewById(R.id.linearLayoutPlayStore);
		imageButtonPlayStore = (ImageButton) findViewById(R.id.imageButtonPlayStore);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkForMediaPlayer();

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				presentationId = editTextPresentationId.getText().toString();
				studentId = editTextStudentId.getText().toString();
				if (!"".equalsIgnoreCase(presentationId)
						&& !"".equalsIgnoreCase(studentId)) {

					new FrontFlipTask(LoginActivity.this, Constants.BASE_URL
							+ Constants.SERVICE + presentationId).execute();
				}
			}
		});

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
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	boolean FLAG_MEDIA_PLAYER_INSTALLED = false;

	private void checkForMediaPlayer() {
		// TODO Auto-generated method stub

		Intent intent = getPackageManager().getLaunchIntentForPackage(
				"org.videolan.vlc.betav7neon");
		if (null == intent) {
			linearLayoutPlayStore.setVisibility(View.VISIBLE);
		} else {
			linearLayoutPlayStore.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_FOR_PLAY_STORE) {
			if (requestCode == RESULT_OK) {
				// TODO
			}
		}
	}

	public void onValidationComplete(boolean validation) {
		// TODO Auto-generated method stub
		if (validation) {
			Intent intent = new Intent(LoginActivity.this,
					PresentationActivity.class);
			LoginData.getInstance().setLessonId(presentationId);
			LoginData.getInstance().setStudentId(studentId);

			startActivity(intent);
		} else {
			editTextPresentationId.setText("");
		}
		
	}

}
