package com.ghsoft.treetask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewTaskTree extends Activity {

	EditText name, treeName;
	Button submit;
	TaskNode task;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtasktree);
		
		setTitle("New Tree");
		
		TaskHead th = new TaskHead();
		final TaskNode task = new TaskNode(th);

		//DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a");
		//Date date = new Date();

		//tn.setName(dateFormat.format(date));
		
		treeName = (EditText)findViewById(R.id.treeName);
		
		name = (EditText)findViewById(R.id.name);
		submit = (Button)findViewById(R.id.submit);
		
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TaskLeaf t = new TaskLeaf(task);
				
				if (name.getText().toString().length() < 1) {
					Toast.makeText(NewTaskTree.this, "You must supply a task name", Toast.LENGTH_LONG).show();
					return;
				}
				
				if (treeName.getText().toString().length() < 1) {
					Toast.makeText(NewTaskTree.this, "You must supply a tree name", Toast.LENGTH_LONG).show();
					return;
				}
				
				hideInput();
				
				if (task.setName(treeName.getText().toString())) {
					if (t.setName(name.getText().toString())) {
						
						final TaskNode tn = (TaskNode)task;
						tn.addSubTask(t);
						
						TaskManager.save(task.getHead());
						
						Intent i = new Intent(NewTaskTree.this, TaskView.class);
						i.putExtra("task", task);
						finish();
						startActivity(i);
						overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);
						
						
					} else {
						Toast.makeText(NewTaskTree.this, "Task Name must be less than " + Task.maxNameLen + " characters.", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(NewTaskTree.this, "Tree name must be less than " + Task.maxNameLen + " characters.", Toast.LENGTH_LONG).show();
				}
				
				
			}
		});
		
	}
	
	private void hideInput() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	    inputManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
	}

	@Override
	public void onBackPressed() {
		Intent i;
		
		i = new Intent(NewTaskTree.this, Main.class);
		finish();
		startActivity(i);
		overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);
		

	}
}
