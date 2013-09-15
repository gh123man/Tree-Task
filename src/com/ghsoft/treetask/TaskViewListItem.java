package com.ghsoft.treetask;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TaskViewListItem extends BaseAdapter {
	private LayoutInflater inflater;
	Context context;
	Activity act;
	TextView name, description, percent, subcount;
	ProgressBar completion;
	TaskNode task;
	View header;
	ImageView check;

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
		return task.getChild(position);
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
		
		
		final Task t = task.getChild(position);
		
		if (t.hasChildren()) {
			
			convertView = this.inflater.inflate(R.layout.taskviewlistitem, null);
			layoutHasChildren(convertView, t);
			
		} else {
			
			convertView = this.inflater.inflate(R.layout.taskviewlistitemnochildren, null);
			layoutNoChildren(convertView, t);
			
		}
		
		return convertView;
	}

	private void layoutHasChildren(View convertView, Task t) {
		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		completion = (ProgressBar) convertView.findViewById(R.id.completion);
		percent = (TextView) convertView.findViewById(R.id.percent);
		subcount = (TextView) convertView.findViewById(R.id.subcount);
		
		name.setText(t.getName());
		description.setText(t.getDescription());
		
		completion.setMax(100);
		completion.setProgress(t.completion());
		percent.setText(t.completion() + "%");
		
		subcount.setText(t.subTaskCount() + " subtask(s)");
	}
	
	private void layoutNoChildren(View convertView, Task t) {
		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		check = (ImageView) convertView.findViewById(R.id.check);
		
		name.setText(t.getName());
		description.setText(t.getDescription());
		
		if (t.completion() == 100) 
			check.setVisibility(View.VISIBLE);
		
	}

	
}