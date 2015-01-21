package com.eduglasses.frontflip.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CalibriTextView extends TextView {

	public CalibriTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CalibriTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalibriTextView(Context context) {
		super(context);
		init();
	}

	private void init() {
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/calibri.otf");
		setTypeface(tf);
	}
}