package com.ghsoft.treetaskapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskDummy;
import com.ghsoft.treetask.TaskHead;
import com.ghsoft.treetask.TaskNode;

public class NewTreeTask extends ActionBarActivity {

	private EditText description, treeName;
	private Button submit;
	private InputMethodManager inputManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtasktree);

		setTitle(R.string.new_tree);

		inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		TaskHead th = new TaskHead();
		final TaskNode task = new TaskNode(th);

		treeName = (EditText) findViewById(R.id.treeName);

		description = (EditText) findViewById(R.id.description);
		submit = (Button) findViewById(R.id.submit);

		showInput();

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TaskDummy t = new TaskDummy(task);

				if (treeName.getText().toString().length() < 1) {
					Toast.makeText(NewTreeTask.this, R.string.supply_name, Toast.LENGTH_LONG).show();
					return;
				}
				
				if (task.setName(treeName.getText().toString())) {
					
					if (task.setDescription(description.getText().toString())) {
						final TaskNode tn = (TaskNode) task;
						tn.addSubTask(t);
						Intent i = new Intent(NewTreeTask.this, NewTreeView.class);
						i.putExtra("task", task);
						finish();
						startActivity(i);
						overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);

					} else {
						Toast.makeText(NewTreeTask.this, R.string.description_less_than + " " + Task.maxDescriptionLen + " " + R.string.characters, Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(NewTreeTask.this, R.string.name_less_than + " " + Task.maxNameLen + " " + R.string.characters, Toast.LENGTH_LONG).show();
				}

			}
		});

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
		Intent i;
		hideInput();
		i = new Intent(NewTreeTask.this, Main.class);
		finish();
		startActivity(i);
		overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);

	}
}
