package com.ghsoft.treetask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
		TextView description = (TextView) header.findViewById(R.id.hdescription);
		TextView completion = (TextView) header.findViewById(R.id.hcompletion);
		
		name.setText(task.getName());
		description.setText(task.getDescription());
		completion.setText(Integer.toString(task.completion()));
		
		
		taskList = (ListView) findViewById(R.id.taskList);
		
		taskList.addHeaderView(header); 
		
		TaskViewListItem adapter = new TaskViewListItem(this, getApplicationContext(), task, header);

		taskList.setAdapter(adapter);
		
		
	}
	
	
	public void refreshHeader(TaskNode task) {
		
		this.task = task;
		
		Log.d("complt", Integer.toString(task.completion()));
		
		View header = getLayoutInflater().inflate(R.layout.header, null); 
		
		TextView name = (TextView) header.findViewById(R.id.hname);
		TextView description = (TextView) header.findViewById(R.id.hdescription);
		TextView completion = (TextView) header.findViewById(R.id.hcompletion);
		
		name.setText(task.getName());
		description.setText(task.getDescription());
		completion.setText(Integer.toString(task.completion()));
	}

}
