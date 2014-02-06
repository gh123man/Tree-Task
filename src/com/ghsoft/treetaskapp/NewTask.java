package com.ghsoft.treetaskapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskDummy;
import com.ghsoft.treetask.TaskLeaf;
import com.ghsoft.treetask.TaskManager;
import com.ghsoft.treetask.TaskNode;

public class NewTask extends Activity {

	private EditText name, description;
	private Button submit;
	private TaskNode task;
	private InputMethodManager inputManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask);

		setTitle("New Task");
		
		inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		Object sTask = getIntent().getSerializableExtra("task");

		task = (TaskNode) sTask;

		name = (EditText) findViewById(R.id.name);
		description = (EditText) findViewById(R.id.description);
		submit = (Button) findViewById(R.id.submit);

		showInput();

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TaskLeaf t = new TaskLeaf(task);

				if (name.getText().toString().length() < 1) {
					Toast.makeText(NewTask.this, "You must supply a name", Toast.LENGTH_LONG).show();
					return;
				}

				if (t.setName(name.getText().toString())) {
					if (t.setDescription(description.getText().toString())) {
						
						Task child = task.getChild(0);
						
						if (child instanceof TaskDummy) {
							task.deleteChild(child);
						}

						final TaskNode tn = (TaskNode) task;
						tn.addSubTask(t);

						TaskManager.save(task.getHead());

						Intent i = new Intent(NewTask.this, TaskView.class);
						i.putExtra("task", task);
						finish();
						startActivity(i);
						overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);

					} else {
						Toast.makeText(NewTask.this, "Description must be less than " + Task.maxDescriptionLen + " characters.", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(NewTask.this, "Name must be less than " + Task.maxNameLen + " characters.", Toast.LENGTH_LONG).show();
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
		
		if (task.getChild(0) instanceof TaskDummy) {
			
			i = new Intent(NewTask.this, NewTreeView.class);
			i.putExtra("task", task);
			
		} else if (task.numChildren() < 1) {
			
			if (task.getParent() == null) {
				i = new Intent(NewTask.this, Main.class);
			} else {
				i = new Intent(NewTask.this, TaskView.class);
				i.putExtra("task", task.getParent());
			}

		} else {
			
			i = new Intent(NewTask.this, TaskView.class);
			i.putExtra("task", task);
		}

		finish();
		startActivity(i);
		overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);

	}

}
