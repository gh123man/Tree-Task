package com.ghsoft.treetaskapp;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskDummy;
import com.ghsoft.treetask.TaskLeaf;
import com.ghsoft.treetask.TaskManager;
import com.ghsoft.treetask.TaskNode;

public class NewTask extends ModifyTaskActivity {

	private TaskNode task;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.new_task);
		getSubmitButton().setText(R.string.new_task);
		
		Object sTask = getIntent().getSerializableExtra("task");

		task = (TaskNode) sTask;

	}
	
	@Override
	public void onSubmit() {
		TaskLeaf t = new TaskLeaf(task);
		t.setTimeStamp(new Date());

		if (getNameField().getText().toString().length() < 1) {
			Toast.makeText(NewTask.this, R.string.supply_name, Toast.LENGTH_LONG).show();
			return;
		}

		if (t.setName(getNameField().getText().toString())) {
			if (t.setDescription(getdescriptionField().getText().toString())) {
				
				if (getChangeColor()) {
					t.setColor(getPicker().getColor());
				} else {
					t.setColor(getResources().getColor(R.color.darkgrey));
				}
				
				if (task.hasChildren()) {
					Task child = task.getChild(0);
					
					if (child instanceof TaskDummy) {
						task.deleteChild(child);
					}
				}

				TaskNode tn = (TaskNode) task;
				tn.addSubTask(t);

				TaskManager.save(task.getHead());
				
				Intent i = new Intent(NewTask.this, TaskView.class);
				i.putExtra("task", task);
				finish();
				startActivity(i);
				overridePendingTransition(R.anim.back_short_zoom, R.anim.slide_down);

			} else {
				Toast.makeText(NewTask.this, R.string.description_less_than + " " + Task.maxDescriptionLen + " " + R.string.characters, Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(NewTask.this, R.string.name_less_than + " " + Task.maxNameLen + " " + R.string.characters, Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i;
		
		if (task.numChildren() < 1) {
			
			if (task.getParent() == null) {
				i = new Intent(NewTask.this, Main.class);
			} else {
				i = new Intent(NewTask.this, TaskView.class);
				i.putExtra("task", task.getParent());
			}
		} else if (task.getChild(0) instanceof TaskDummy) {
			
			i = new Intent(NewTask.this, NewTreeView.class);
			i.putExtra("task", task);
			
		} else {
			
			i = new Intent(NewTask.this, TaskView.class);
			i.putExtra("task", task);
		}

		finish();
		startActivity(i);
		overridePendingTransition(R.anim.back_short_zoom, R.anim.slide_down);

	}


}