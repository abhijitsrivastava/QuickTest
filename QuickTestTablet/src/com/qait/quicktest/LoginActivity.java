package com.qait.quicktest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.qait.quicktest.constants.Constants;
import com.qait.quicktest.model.LoginData;
import com.qait.quicktest.utils.FrontFlipTask;

public class LoginActivity extends Activity {

	private Button loginButton;

	private EditText editTextPresentationId;
	private EditText editTextStudentId;

	private String presentationId;
	private String studentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		editTextPresentationId = (EditText) findViewById(R.id.editTextPresentationId);
		loginButton = (Button) findViewById(R.id.buttonLogin);
		editTextStudentId = (EditText) findViewById(R.id.editTextStudentId);
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
