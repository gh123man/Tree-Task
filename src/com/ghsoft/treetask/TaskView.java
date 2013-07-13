package com.ghsoft.treetask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.treetask.R;


public class TaskView extends Activity {

	ListView taskList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskview);
		
		Object sTask = getIntent().getSerializableExtra("task");
		
		TaskNode task = (TaskNode)sTask;
		
		
		View header = getLayoutInflater().inflate(R.layout.header, null); 

		
		taskList = (ListView) findViewById(R.id.taskList);
		
		taskList.addHeaderView(header); 
		
		TaskViewListItem adapter = new TaskViewListItem(this, getApplicationContext(), task);

		taskList.setAdapter(adapter);
		
		
		
		taskList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
			}
		});
	}

}
