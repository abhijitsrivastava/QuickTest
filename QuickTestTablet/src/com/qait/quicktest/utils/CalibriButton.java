package com.qait.quicktest.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

public class CalibriButton extends Button {

	public CalibriButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CalibriButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalibriButton(Context context) {
		super(context);
		init();
	}

	private void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/calibri.otf");
		setTypeface(tf);
	}
}