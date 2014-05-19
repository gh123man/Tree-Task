package com.ghsoft.treetaskapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ghsoft.treetask.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

public abstract class ModifyTaskActivity extends ActionBarActivity {

	private EditText name, description, weight;
	private Button submit;
	private CheckBox showPicker, showWeight;
	private LinearLayout pickerZone, customZone, weightZone;
	private ColorPicker picker;
	private boolean changeColor, changeWeight;
	private RadioGroup rg1, rg2, rg3;
	private SharedPreferences prefs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_task);

		name = (EditText) findViewById(R.id.name);
		description = (EditText) findViewById(R.id.description);
		submit = (Button) findViewById(R.id.submit);
		showPicker = (CheckBox) findViewById(R.id.show_picker);
		showWeight = (CheckBox) findViewById(R.id.show_weight_change);
		pickerZone = (LinearLayout) findViewById(R.id.picker_zone);
		picker = (ColorPicker) findViewById(R.id.picker);
		SaturationBar saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
		ValueBar valueBar = (ValueBar) findViewById(R.id.valuebar);
		customZone = (LinearLayout) findViewById(R.id.custom_zone);
		weightZone = (LinearLayout) findViewById(R.id.weight_zone);
		weight = (EditText) findViewById(R.id.task_weight);
		
		picker.addSaturationBar(saturationBar);
		picker.addValueBar(valueBar);
		picker.setShowOldCenterColor(false);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

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
		
		showWeight.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				hideInput();
				if (isChecked) {
					weightZone.setVisibility(View.VISIBLE);
					changeWeight = true;
				} else {
					weightZone.setVisibility(View.GONE);
					changeWeight = false;
				}
			}
		});

		rg1 = (RadioGroup) findViewById(R.id.defualtColors1);
		rg2 = (RadioGroup) findViewById(R.id.defualtColors2);
		rg3 = (RadioGroup) findViewById(R.id.defualtColors3);

		rg1.clearCheck();
		rg2.clearCheck();
		rg3.clearCheck();
		rg1.check(R.id.radio0);
		picker.setColor(getResources().getColor(R.color.color0));

		rg1.setOnCheckedChangeListener(listener1);
		rg2.setOnCheckedChangeListener(listener2);
		rg3.setOnCheckedChangeListener(listener3);

		((RadioButton) findViewById(R.id.radio1)).setBackgroundColor(prefs.getInt("color1", getResources().getColor(R.color.color1)));
		((RadioButton) findViewById(R.id.radio2)).setBackgroundColor(prefs.getInt("color2", getResources().getColor(R.color.color2)));
		((RadioButton) findViewById(R.id.radio3)).setBackgroundColor(prefs.getInt("color3", getResources().getColor(R.color.color3)));
		((RadioButton) findViewById(R.id.radio4)).setBackgroundColor(prefs.getInt("color4", getResources().getColor(R.color.color4)));
		((RadioButton) findViewById(R.id.radio5)).setBackgroundColor(prefs.getInt("color5", getResources().getColor(R.color.color5)));

	}

	private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			resetRadioGroup(rg2, listener2);
			resetRadioGroup(rg3, listener3);

			customZone.setVisibility(View.GONE);

			if (checkedId == R.id.radio0) {
				picker.setColor(getResources().getColor(R.color.color0));
			} else if (checkedId == R.id.radio1) {
				picker.setColor(prefs.getInt("color1", getResources().getColor(R.color.color1)));
			} else if (checkedId == R.id.radio2) {
				picker.setColor(prefs.getInt("color2", getResources().getColor(R.color.color2)));
			}

		}
	};

	private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			resetRadioGroup(rg1, listener1);
			resetRadioGroup(rg3, listener3);

			customZone.setVisibility(View.GONE);

			if (checkedId == R.id.radio3) {
				picker.setColor(prefs.getInt("color3", getResources().getColor(R.color.color3)));
			} else if (checkedId == R.id.radio4) {
				picker.setColor(prefs.getInt("color4", getResources().getColor(R.color.color4)));
			} else if (checkedId == R.id.radio5) {
				picker.setColor(prefs.getInt("color5", getResources().getColor(R.color.color5)));
			}

		}
	};

	private RadioGroup.OnCheckedChangeListener listener3 = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if (customZone.getVisibility() == View.GONE) {
				customZone.setVisibility(View.VISIBLE);
			} else {
				customZone.setVisibility(View.GONE);
			}

			resetRadioGroup(rg1, listener1);
			resetRadioGroup(rg2, listener2);

		}
	};

	private void resetRadioGroup(RadioGroup rg, RadioGroup.OnCheckedChangeListener listener) {
		rg.setOnCheckedChangeListener(null);
		rg.clearCheck();
		rg.setOnCheckedChangeListener(listener);
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
	
	public boolean getChangeWeight() {
		return changeWeight;
	}

	private void hideInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
	}
	
	public void hideChangeWeightOption(boolean hide) {
		if (hide) {
			showWeight.setVisibility(View.GONE);
		} else {
			showWeight.setVisibility(View.VISIBLE);
		}
	}
	
	public void setWeightField(int value) {
		weight.setText(String.valueOf(value));
	}
	
	public int getWeightFieldValue() {
		return Integer.parseInt(weight.getText().toString());
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