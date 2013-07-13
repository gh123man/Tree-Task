package com.ghsoft.treetask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.treetask.R;


public class TaskView extends Activity {

	ListView taskList;
	TaskNode task;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskview);
		
		Object sTask = getIntent().getSerializableExtra("task");
		
		task = (TaskNode)sTask;
		
		View header = getLayoutInflater().inflate(R.layout.header, null); 
		
		TextView name = (TextView) header.findViewById(R.id.hname);
		TextView path = (TextView) header.findViewById(R.id.path);
		TextView description = (TextView) header.findViewById(R.id.hdescription);
		
		ProgressBar completion = (ProgressBar) header.findViewById(R.id.hcompletion);
		
		name.setText(task.getName());
		path.setText(task.getPath());
		description.setText(task.getDescription());
		
		completion.setMax(100);
		completion.setProgress(task.completion());
		
		
		taskList = (ListView) findViewById(R.id.taskList);
		
		taskList.addHeaderView(header); 
		
		TaskViewListItem adapter = new TaskViewListItem(this, getApplicationContext(), task, header);

		taskList.setAdapter(adapter);
		
	}
	
	@Override
	public void onBackPressed() {
		if (task.isHead()) {
			
			Intent i = new Intent(TaskView.this, Main.class);
			i.putExtra("task", task.getParent());
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.backslide, R.anim.backzoom);
			
		} else {
			
			Intent i = new Intent(TaskView.this, TaskView.class);
			i.putExtra("task", task.getParent());
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.backshortzoom, R.anim.slideto);
			
		}
		
		
	}

}
