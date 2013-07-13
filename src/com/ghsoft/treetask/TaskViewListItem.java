package com.ghsoft.treetask;

import com.example.treetask.R;

import android.app.Activity;
import android.content.Context;
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
	String uname, pword;
	TaskNode task;

	public TaskViewListItem(Activity act, Context context, TaskNode task) {
		// Caches the LayoutInflater for quicker use
		this.inflater = LayoutInflater.from(context);
		// Sets the events data
		this.task = task;
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
		// TODO Auto-generated method stub
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		Task t = task.getChildren().get(position);
		
		convertView = this.inflater.inflate(R.layout.mainlistitem, null);

		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		completion = (TextView) convertView.findViewById(R.id.completion);
		
		name.setText(t.getName());
		description.setText(t.getDescription());
		completion.setText(Integer.toString(t.completion()));

		return convertView;
	}


	
}