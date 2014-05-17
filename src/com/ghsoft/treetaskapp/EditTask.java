package com.ghsoft.treetaskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskDummy;
import com.ghsoft.treetask.TaskManager;
import com.ghsoft.treetask.TaskNode;

public class EditTask extends ModifyTaskActivity {

	private Task task;
	private boolean fromList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.edit_task);
		getSubmitButton().setText(R.string.update);

		Object sTask = getIntent().getSerializableExtra("task");
		fromList = getIntent().getBooleanExtra("fromListView", false);

		task = (Task) sTask;

		getNameField().setText(task.getName());
		getdescriptionField().setText(task.getDescription());

	}

	@Override
	public void onSubmit() {
		// TODO Auto-generated method stub
		if (getNameField().getText().toString().length() < 1) {
			Toast.makeText(EditTask.this, R.string.supply_name, Toast.LENGTH_LONG).show();
			return;
		}

		if (task.setName(getNameField().getText().toString())) {
			if (task.setDescription(getdescriptionField().getText().toString())) {

				TaskManager.save(task.getHead());
				Intent i = null;

				if (task instanceof TaskNode) {

					if (((TaskNode) task).getChild(0) instanceof TaskDummy) {
						i = new Intent(EditTask.this, NewTreeView.class);
					} else {
						i = new Intent(EditTask.this, TaskView.class);
					}
				} else {
					i = new Intent(EditTask.this, TaskView.class);
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
				Toast.makeText(EditTask.this, R.string.description_less_than + " " + Task.maxDescriptionLen + " " + R.string.characters, Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(EditTask.this, R.string.name_less_than + " " + Task.maxNameLen + " " + R.string.characters, Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent i = null;

		if (task instanceof TaskNode) {

			if (((TaskNode) task).getChild(0) instanceof TaskDummy) {
				i = new Intent(EditTask.this, NewTreeView.class);
			} else {
				i = new Intent(EditTask.this, TaskView.class);
			}
		} else {
			i = new Intent(EditTask.this, TaskView.class);
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
