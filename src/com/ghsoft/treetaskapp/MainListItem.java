package com.ghsoft.treetaskapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.TaskHead;

public class MainListItem extends BaseAdapter {
	private LayoutInflater inflater;
	private TextView name, description, percent, subcount;
	private TreeTaskProgressBar completion;
	private ArrayList<TaskHead> tasks;
	private String type;
	private LinearLayout listItemBase;

	public MainListItem(Context context, ArrayList<TaskHead> tasks, String type) {
		//this.inflater = LayoutInflater.from(context);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.tasks = tasks;
		this.type = type;
	}

	public String getType() {
		return type;
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
		return tasks.size();
	}

	public ArrayList<TaskHead> getData() {
		return tasks;
	}

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = this.inflater.inflate(R.layout.main_list_item, null);

		name = (TextView) convertView.findViewById(R.id.name);
		description = (TextView) convertView.findViewById(R.id.description);
		completion = (TreeTaskProgressBar) convertView.findViewById(R.id.completion);
		percent = (TextView) convertView.findViewById(R.id.percent);
		subcount = (TextView) convertView.findViewById(R.id.subcount);
		listItemBase = (LinearLayout) convertView.findViewById(R.id.list_item_base);
		TextView timeStamp = (TextView) convertView.findViewById(R.id.timestamp);

		if (tasks.get(position).getTask().getTimeStamp() != null) {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
			String timeString = df.format(tasks.get(position).getTask().getTimeStamp());
			timeStamp.setText(timeString);
		}

		name.setText(tasks.get(position).getTask().getName());
		description.setText(tasks.get(position).getTask().getDescription());
		completion.setMax(100);
		completion.setProgress(tasks.get(position).getTask().completion());
		percent.setText(tasks.get(position).getTask().completion() + "%");

		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] { android.R.attr.state_pressed }, convertView.getResources().getDrawable(R.color.select));
		states.addState(new int[] { android.R.attr.state_focused }, convertView.getResources().getDrawable(R.color.select));
		if (tasks.get(position).getTask().getUseColor()) {
			states.addState(new int[] {}, new ColorDrawable(tasks.get(position).getTask().getColor()));
		} else {
			states.addState(new int[] {}, convertView.getResources().getDrawable(R.color.nselect));
		}
		listItemBase.setBackgroundDrawable(states);

		if (tasks.get(position).getTask().subTaskCount() > 1) {
			subcount.setText(String.valueOf(tasks.get(position).getTask().subTaskCount()) + " " + convertView.getResources().getString(R.string.subtasks));
		} else {
			subcount.setText(String.valueOf(tasks.get(position).getTask().subTaskCount()) + " " + convertView.getResources().getString(R.string.subtask));
		}

		return convertView;
	}

}