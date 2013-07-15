package com.ghsoft.treetask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TaskView extends Activity {

	ListView taskList;
	TaskNode task;
	TaskViewListItem adapter;
	View header;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskview);

		Object sTask = getIntent().getSerializableExtra("task");

		task = (TaskNode) sTask;

		setTitle(task.getName());

		taskList = (ListView) findViewById(R.id.taskList);

		header = header();
		View footer = footer();

		taskList.addHeaderView(header, null, false);
		taskList.addFooterView(footer, null, false);

		adapter = new TaskViewListItem(this, getApplicationContext(), task, header);

		taskList.setAdapter(adapter);

		registerForContextMenu(taskList);

		taskList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

				position--;

				Task t = task.getChild(position);

				if (t.hasChildren()) {

					Intent i = new Intent(TaskView.this, TaskView.class);
					i.putExtra("task", t);
					finish();
					startActivity(i);
					overridePendingTransition(R.anim.slidefrom, R.anim.shortzoom);

				} else {
					TaskLeaf tl = (TaskLeaf) t;
					tl.setFinished(!tl.getFinished());

					ProgressBar completion = (ProgressBar) v.findViewById(R.id.completion);

					completion.setProgress(t.completion());

					completion = (ProgressBar) findViewById(R.id.hcompletion);
					completion.setProgress(task.completion());

				}

			}
		});

	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		info.position--;
		if (task.getChild(info.position) instanceof TaskNode) {
			inflater.inflate(R.menu.taskviewmenunode, menu);
		} else {
			inflater.inflate(R.menu.taskviewmenuleaf, menu);
		}

	}

	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.addSubTask:

			TaskLeaf tl = (TaskLeaf) task.getChild(info.position);
			TaskNode tn = TaskNode.fromLeaf(tl);
			task.replaceChild(info.position, tn);

			Intent i = new Intent(TaskView.this, NewTask.class);
			i.putExtra("task", tn);
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.slideup, R.anim.shortzoom);

			return true;

		case R.id.delete:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to Delete this story?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					task.deleteChild(info.position);

					if (task.numChildren() < 1) {
						Intent i = new Intent(TaskView.this, TaskView.class);
						i.putExtra("task", task.getParent());
						finish();
						startActivity(i);
						overridePendingTransition(R.anim.backshortzoom, R.anim.slideto);
					} else {

						adapter.notifyDataSetChanged();
						ProgressBar completion = (ProgressBar) header.findViewById(R.id.hcompletion);
						completion.setProgress(task.completion());
					}

				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		if (task.isHead()) {

			Intent i = new Intent(TaskView.this, Main.class);
			i.putExtra("task", task.getParent());
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.backslide, R.anim.backzoom);

		} else {

			Intent i = new Intent(TaskView.this, TaskView.class);
			i.putExtra("task", task.getParent());
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.backshortzoom, R.anim.slideto);

		}

	}

	private View header() {
		View header = getLayoutInflater().inflate(R.layout.header, null);

		TextView name = (TextView) header.findViewById(R.id.hname);
		TextView path = (TextView) header.findViewById(R.id.path);
		TextView description = (TextView) header.findViewById(R.id.hdescription);

		ProgressBar completion = (ProgressBar) header.findViewById(R.id.hcompletion);

		name.setText(task.getName());
		path.setText(task.getPath());
		description.setText(task.getDescription());

		completion.setMax(100);
		completion.setProgress(task.completion());
		return header;
	}

	private View footer() {
		View footer = getLayoutInflater().inflate(R.layout.footer, null);

		Button newTask = (Button) footer.findViewById(R.id.newTask);

		newTask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TaskView.this, NewTask.class);
				i.putExtra("task", task);
				finish();
				startActivity(i);
				overridePendingTransition(R.anim.slideup, R.anim.shortzoom);

			}
		});

		return footer;
	}

}
