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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskview);
		
		Object sTask = getIntent().getSerializableExtra("task");
		
		final TaskNode task = (TaskNode)sTask;
		
		
		View header = getLayoutInflater().inflate(R.layout.header, null); 
		
		TextView name = (TextView) header.findViewById(R.id.hname);
		TextView description = (TextView) header.findViewById(R.id.hdescription);
		TextView completion = (TextView) header.findViewById(R.id.hcompletion);
		
		name.setText(task.getName());
		description.setText(task.getDescription());
		completion.setText(Integer.toString(task.completion()));
		
		
		taskList = (ListView) findViewById(R.id.taskList);
		
		taskList.addHeaderView(header); 
		
		TaskViewListItem adapter = new TaskViewListItem(this, getApplicationContext(), task);

		taskList.setAdapter(adapter);
		
		
		
		taskList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				position--;
				
				if (task.getChildren().get(position).hasChildren()) {
					Intent i = new Intent(TaskView.this, TaskView.class);
					i.putExtra("task", task.getChildren().get(position));
					startActivity(i);
				} else {
					TaskLeaf t = (TaskLeaf)task.getChildren().get(position);
					t.setFinished(!t.getFinished());
					Log.d("count", Integer.toString(t.completion()));
				}
				
			}
		});
	}

}
