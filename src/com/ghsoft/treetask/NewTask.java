package com.ghsoft.treetask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ghsoft.treetask.R;

public class NewTask extends Activity {

	EditText name, description;
	Button submit;
	TaskNode task;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask);
		
		setTitle("New Task");
		
		Object sTask = getIntent().getSerializableExtra("task");

		task = (TaskNode) sTask;
		
		name = (EditText)findViewById(R.id.name);
		description = (EditText)findViewById(R.id.description);
		submit = (Button)findViewById(R.id.submit);
		
		
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

	@Override
	public void onBackPressed() {
		
		Intent i = new Intent(NewTask.this, TaskView.class);
		if (task.numChildren() < 1) {
			i.putExtra("task", task.getParent());
		} else {
			i.putExtra("task", task);
		}
		finish();
		startActivity(i);
		overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);
		

	}
}
