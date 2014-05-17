package com.ghsoft.treetaskapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
	private InputMethodManager inputManager;
	private CheckBox showPicker;
	private LinearLayout pickerZone;
	private ColorPicker picker;
	private boolean changeColor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_task);

		inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

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

		showInput();

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hideInput();
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

	private void showInput() {
		inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	private void hideInput() {
		inputManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
	}

	@Override
	public void onPause() {
		super.onPause();
		hideInput();
	}
	
	@Override
	public void onBackPressed() {
		hideInput();
	}

}