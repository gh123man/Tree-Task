package com.ghsoft.treetask;

import com.example.treetask.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskViewListItem extends BaseAdapter {
	private LayoutInflater inflater;
	Context context;
	Activity act;
	TextView name, description, completion;
	TaskNode task;
	View header;

	public TaskViewListItem(Activity act, Context context, TaskNode task, View header) {
		// Caches the LayoutInflater for quicker use
		this.inflater = LayoutInflater.from(context);
		// Sets the events data
		this.task = task;
		this.act = act;
		this.header = header;
		this.context = context;
	}


	public Task getItem(int position) throws IndexOutOfBoundsException {
		return task.getChildren().get(position);
	}

	public long getItemId(int position) throws IndexOutOfBoundsException {
		if (position < getCount() && position >= 0) {
			return position;
		}
		return 0;
	}

	public int getViewTypeCount() {
		return task.numChildren();
	}
	
	@Override
	public int getCount() {
		return task.numChildren();
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		final Task t = task.getChildren().get(position);
		
		convertView = this.inflater.inflate(R.layout.taskviewlistitem, null);
		
		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		completion = (TextView) convertView.findViewById(R.id.completion);
		
		name.setText(t.getName());
		description.setText(t.getDescription());
		completion.setText(Integer.toString(t.completion()));
		
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("count", Integer.toString(position));
				
				if (t.hasChildren()) {
					
					Intent i = new Intent(context, TaskView.class);
					i.putExtra("task", t);
					act.startActivity(i);
					
				} else {
					TaskLeaf tl = (TaskLeaf)t;
					tl.setFinished(!tl.getFinished());
					
					completion = (TextView) v.findViewById(R.id.completion);
					
					completion.setText(Integer.toString(t.completion()));
					
					TaskViewListItem.this.notifyDataSetChanged();
					
					TextView completion = (TextView) header.findViewById(R.id.hcompletion);
					completion.setText(Integer.toString(task.completion()));
					
					TaskView tv = (TaskView)act;
					
					//Log.d("count", Integer.toString(t.completion()));
				}
			}
		});

		return convertView;
	}


	
}