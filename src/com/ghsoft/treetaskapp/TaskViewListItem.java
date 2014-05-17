package com.ghsoft.treetaskapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskNode;

public class TaskViewListItem extends BaseAdapter {
	private LayoutInflater inflater;
	private TextView name, description, percent, subcount, timeStamp;
	private ProgressBar completion;
	private TaskNode task;
	private ImageView check;

	public TaskViewListItem(Context context, TaskNode task, View header) {
		this.inflater = LayoutInflater.from(context);
		this.task = task;
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
	
	public void move(int from, int to) {
		task.moveChild(from, to);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		final Task t = task.getChild(position);

		if (t.hasChildren()) {

			convertView = this.inflater.inflate(R.layout.task_view_list_item, null);
			layoutHasChildren(convertView, t);

		} else {

			convertView = this.inflater.inflate(R.layout.task_view_list_item_no_children, null);
			layoutNoChildren(convertView, t);

		}

		return convertView;
	}

	private void layoutHasChildren(View convertView, Task t) {
		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		completion = (ProgressBar) convertView.findViewById(R.id.completion);
		percent = (TextView) convertView.findViewById(R.id.percent);
		subcount = (TextView) convertView.findViewById(R.id.subcountmainlistitem);
	    timeStamp = (TextView) convertView.findViewById(R.id.timestamp);
		
		if (task.getTimeStamp() != null) {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
			String timeString = df.format(task.getTimeStamp());
			timeStamp.setText(timeString);
		}
		
		name.setText(t.getName());
		description.setText(t.getDescription());

		completion.setMax(100);
		completion.setProgress(t.completion());
		percent.setText(t.completion() + "%");

		if (t.subTaskCount() > 1) {
			subcount.setText(t.subTaskCount() + " " + convertView.getResources().getString(R.string.subtasks));
		} else {
			subcount.setText(t.subTaskCount() + " " + convertView.getResources().getString(R.string.subtask));
		}
		

		if (t.completion() == 100) {
			name.setTextColor(Color.parseColor("#505050"));
			description.setTextColor(Color.parseColor("#505050"));
			timeStamp.setTextColor(Color.parseColor("#505050"));
		}

	}

	private void layoutNoChildren(View convertView, Task t) {
		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		check = (ImageView) convertView.findViewById(R.id.check);
		timeStamp = (TextView) convertView.findViewById(R.id.timestamp);
		
		if (task.getTimeStamp() != null) {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
			String timeString = df.format(task.getTimeStamp());
			timeStamp.setText(timeString);
		}

		name.setText(t.getName());
		description.setText(t.getDescription());

		if (t.completion() == 100) {
			check.setVisibility(View.VISIBLE);
			name.setTextColor(Color.parseColor("#505050"));
			description.setTextColor(Color.parseColor("#505050"));
			timeStamp.setTextColor(Color.parseColor("#505050"));
		}

	}

}