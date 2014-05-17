package com.ghsoft.treetaskapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ghsoft.treetask.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

public abstract class ModifyTaskActivity extends ActionBarActivity {

	private EditText name, description;
	private Button submit;
	private CheckBox showPicker;
	private LinearLayout pickerZone;
	private ColorPicker picker;
	private boolean changeColor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_task);

		name = (EditText) findViewById(R.id.name);
		description = (EditText) findViewById(R.id.description);
		submit = (Button) findViewById(R.id.submit);
		showPicker = (CheckBox) findViewById(R.id.show_picker);
		pickerZone = (LinearLayout) findViewById(R.id.picker_zone);
		picker = (ColorPicker) findViewById(R.id.picker);
		SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
		ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);

		picker.addSaturationBar(saturationBar);
		picker.addValueBar(valueBar);
		picker.setOldCenterColor(getResources().getColor(R.color.darkgrey));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onSubmit();
			}
		});

		showPicker.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hideInput();
				if (isChecked) {
					pickerZone.setVisibility(View.VISIBLE);
					changeColor = true;
				} else {
					pickerZone.setVisibility(View.GONE);
					changeColor = false;
				}
			}
		});

	}

	public abstract void onSubmit();

	public EditText getNameField() {
		return name;
	}

	public EditText getdescriptionField() {
		return description;
	}

	public Button getSubmitButton() {
		return submit;
	}

	public ColorPicker getPicker() {
		return picker;
	}

	public boolean getChangeColor() {
		return changeColor;
	}

	private void hideInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// hideInput();
	}

}