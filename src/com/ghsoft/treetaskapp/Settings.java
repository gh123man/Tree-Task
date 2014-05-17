package com.ghsoft.treetaskapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ghsoft.treetask.R;

public class Settings extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent i;
		i = new Intent(Settings.this, Main.class);
		finish();
		startActivity(i);
		overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);

	}
}