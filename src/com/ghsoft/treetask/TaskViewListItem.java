package com.ghsoft.treetask;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TaskViewListItem extends BaseAdapter {
	private LayoutInflater inflater;
	Context context;
	Activity act;
	TextView name, description;
	ProgressBar completion;
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
		
		convertView = this.inflater.inflate(R.layout.taskviewlistitem, null);
		
		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		completion = (ProgressBar) convertView.findViewById(R.id.completion);
		
		name.setText(t.getName());
		description.setText(t.getDescription());
		
		completion.setMax(100);
		completion.setProgress(t.completion());
		
		
		return convertView;
	}


	
}