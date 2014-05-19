package com.ghsoft.treetaskapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.ghsoft.treetask.R;

public class Settings extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);

		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		Preference button = (Preference) findPreference("reset_all");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				AlertDialog.Builder builder;
				AlertDialog alert;
				builder = new AlertDialog.Builder(Settings.this);
				builder.setMessage(R.string.sure_to_reset).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						settings.edit().clear().commit();
						Intent intent = getIntent();
						finish();
						startActivity(intent);
						overridePendingTransition(0, 0);

					}
				}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				alert = builder.create();
				alert.show();

				return true;
			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent i;
		i = new Intent(Settings.this, Main.class);
		finish();
		startActivity(i);
		overridePendingTransition(R.anim.back_short_zoom, R.anim.slide_down);

	}
}