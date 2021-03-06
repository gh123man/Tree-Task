package com.ghsoft.treetaskapp;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskDummy;
import com.ghsoft.treetask.TaskHead;
import com.ghsoft.treetask.TaskNode;

public class NewTreeTask extends ModifyTaskActivity {

	private TaskNode task;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.new_tree);

		TaskHead th = new TaskHead();
		task = new TaskNode(th);
		
		task.setTimeStamp(new Date());
		
		getSubmitButton().setText(R.string.create_tree);
		hideChangeWeightOption(true);
		
	}

	@Override
	public void onSubmit() {
		// TODO Auto-generated method stub
		TaskDummy t = new TaskDummy(task);

		if (getNameField().getText().toString().length() < 1) {
			Toast.makeText(NewTreeTask.this, R.string.supply_name, Toast.LENGTH_LONG).show();
			return;
		}

		if (task.setName(getNameField().getText().toString())) {

			if (task.setDescription(getdescriptionField().getText().toString())) {
				
				if (getChangeColor()) {
					task.setColor(getPicker().getColor());
				} else {
					task.setColor(getResources().getColor(R.color.darkgrey));
				}
				
				final TaskNode tn = (TaskNode) task;
				tn.addSubTask(t);
				Intent i = new Intent(NewTreeTask.this, NewTreeView.class);
				i.putExtra("task", task);
				finish();
				startActivity(i);
				overridePendingTransition(R.anim.back_short_zoom, R.anim.slide_down);

			} else {
				Toast.makeText(NewTreeTask.this, R.string.description_less_than + " " + Task.maxDescriptionLen + " " + R.string.characters, Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(NewTreeTask.this, R.string.name_less_than + " " + Task.maxNameLen + " " + R.string.characters, Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent i;
		i = new Intent(NewTreeTask.this, Main.class);
		finish();
		startActivity(i);
		overridePendingTransition(R.anim.back_short_zoom, R.anim.slide_down);

	}

}
