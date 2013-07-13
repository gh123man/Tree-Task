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
		
		
		View header = getLayoutInflater().inflate(R.layout.header, null); 

		
		taskList = (ListView) findViewById(R.id.taskList);
		
		//MainListItem adapter = new MainListItem(getActivity(), getActivity()
		//		.getApplicationContext(), t);

		//taskList.setAdapter(ListAdapter);
		
		taskList.addHeaderView(header); 
		
		taskList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
			}
		});
	}

}
