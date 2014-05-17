package com.ghsoft.treetaskapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ghsoft.treetask.R;

public abstract class ModifyTaskActivity extends ActionBarActivity {

	private EditText name, description;
	private Button submit;
	private InputMethodManager inputManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_task);

		inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		name = (EditText) findViewById(R.id.name);
		description = (EditText) findViewById(R.id.description);
		submit = (Button) findViewById(R.id.submit);

		showInput();

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onSubmit();
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