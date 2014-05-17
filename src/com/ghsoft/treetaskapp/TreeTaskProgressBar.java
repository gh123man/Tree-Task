package com.ghsoft.treetaskapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;

import com.ghsoft.treetask.R;

public class TreeTaskProgressBar extends ProgressBar {


	public TreeTaskProgressBar(Context context) {
		super(context);
		setupColor();
	}
	
	public TreeTaskProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupColor();
	}
	
	public TreeTaskProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupColor();
	}

	private void setupColor() {
		LayerDrawable progressDrawable = (LayerDrawable) this.getProgressDrawable();
		
		SharedPreferences general = PreferenceManager.getDefaultSharedPreferences(getContext());
		int color = general.getInt("prog_color", getResources().getColor(R.color.progDefault));

		Drawable drawable = new ColorDrawable(color);
		ClipDrawable cd = new ClipDrawable(drawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
		progressDrawable.setDrawableByLayerId(android.R.id.progress, cd);

		Drawable bgdfraw = new ColorDrawable(Color.parseColor("#111111"));
		progressDrawable.setDrawableByLayerId(android.R.id.background, bgdfraw);
	}

}
