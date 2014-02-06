package com.ghsoft.treetaskapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskDummy;
import com.ghsoft.treetask.TaskManager;
import com.ghsoft.treetask.TaskNode;

public class EditTask extends Activity {

	private EditText name, description;
	private Button submit;
	private Task task;
	private boolean fromList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittask);

		setTitle("Edit task");

		Object sTask = getIntent().getSerializableExtra("task");
		fromList = getIntent().getBooleanExtra("fromListView", false);

		task = (Task) sTask;

		name = (EditText) findViewById(R.id.name);
		description = (EditText) findViewById(R.id.description);
		submit = (Button) findViewById(R.id.submit);

		name.setText(task.getName());
		description.setText(task.getDescription());

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (name.getText().toString().length() < 1) {
					Toast.makeText(EditTask.this, "You must supply a name", Toast.LENGTH_LONG).show();
					return;
				}

				if (task.setName(name.getText().toString())) {
					if (task.setDescription(description.getText().toString())) {

						TaskManager.save(task.getHead());
						Intent i = null;
						
						if (task instanceof TaskNode) {
							
							if (((TaskNode) task).getChild(0) instanceof TaskDummy) {
								i = new Intent(EditTask.this, NewTreeView.class);
							} else {
								i = new Intent(EditTask.this, TaskView.class);
							}
						}

						if (fromList) {
							i.putExtra("task", task.getParent());
						} else {
							i.putExtra("task", task);
						}

						finish();
						startActivity(i);
						overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);

					} else {
						Toast.makeText(EditTask.this, "Description must be less than " + Task.maxDescriptionLen + " characters.", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(EditTask.this, "Name must be less than " + Task.maxNameLen + " characters.", Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	@Override
	public void onBackPressed() {
		
		Intent i = null;
		
		if (task instanceof TaskNode) {
			
			if (((TaskNode) task).getChild(0) instanceof TaskDummy) {
				i = new Intent(EditTask.this, NewTreeView.class);
			} else {
				i = new Intent(EditTask.this, TaskView.class);
			}
		}
		
		if (fromList) {
			i.putExtra("task", task.getParent());
		} else {
			i.putExtra("task", task);
		}

		finish();
		startActivity(i);
		overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);

	}
}
