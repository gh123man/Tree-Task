package com.ghsoft.treetask;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainListItem extends BaseAdapter {
	private LayoutInflater inflater;
	Context context;
	Activity act;
	TextView name, description, percent, subcount;
	ProgressBar completion;
	String uname, pword;
	ArrayList<TaskHead> tasks;

	public MainListItem(Activity act, Context context, ArrayList<TaskHead> tasks) {
		// Caches the LayoutInflater for quicker use
		this.inflater = LayoutInflater.from(context);
		// Sets the events data
		this.tasks = tasks;
	}

	public TaskHead getItem(int position) throws IndexOutOfBoundsException {
		return tasks.get(position);
	}

	public long getItemId(int position) throws IndexOutOfBoundsException {
		if (position < getCount() && position >= 0) {
			return position;
		}
		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tasks.size();
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = this.inflater.inflate(R.layout.mainlistitem, null);

		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		completion = (ProgressBar) convertView.findViewById(R.id.completion);
		percent = (TextView) convertView.findViewById(R.id.percent);
		subcount = (TextView) convertView.findViewById(R.id.subcount);
		
		name.setText(tasks.get(position).getTask().getName());
		description.setText(tasks.get(position).getTask().getDescription());
		completion.setMax(100);
		completion.setProgress(tasks.get(position).getTask().completion());
		percent.setText(tasks.get(position).getTask().completion() + "%");
		subcount.setText(tasks.get(position).getTask().subTaskCount() + " subtask(s)");
		
		
		return convertView;
	}

}