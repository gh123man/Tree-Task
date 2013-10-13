package com.ghsoft.treetaskapp;

import java.util.Dictionary;
import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskLeaf;
import com.ghsoft.treetask.TaskManager;
import com.ghsoft.treetask.TaskNode;

public class TaskView extends Activity {

	ListView taskList;
	TaskNode task;
	TaskViewListItem adapter;
	View header;
	Task treeView;
	int parentCount, lastY, currentScroll, headerHeight, baseScrollHeight, triangleHeight;
	LinearLayout floatingProgBarHeader;
	private Dictionary<Integer, Integer> listViewItemHeights;
	boolean titleDefualt, setScrollHeight, offsetSet;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taskview);
		setScrollHeight = false;
		offsetSet = false;
		treeView = null;
		listViewItemHeights = new Hashtable<Integer, Integer>();

		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("treeView")) {
			treeView = (Task) getIntent().getExtras().getSerializable("treeView");
			parentCount = getIntent().getExtras().getInt("parentCount");
		}

		Object sTask = getIntent().getSerializableExtra("task");

		task = (TaskNode) sTask;

		setTitleCheck(true);

		taskList = (ListView) findViewById(R.id.taskList);

		header = header();

		taskList.addHeaderView(header, null, false);

		adapter = new TaskViewListItem(this, getApplicationContext(), task, header);

		taskList.setAdapter(adapter);

		taskList.setOverScrollMode(View.OVER_SCROLL_NEVER);

		registerForContextMenu(taskList);

		floatingProgBarHeader = (LinearLayout) findViewById(R.id.progBarFloat);

		setOffset();

		ViewTreeObserver vto = taskList.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!setScrollHeight) {
					baseScrollHeight = getScroll();
					setScrollHeight = true;
				}

			}
		});

		taskList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				placeFloatingView();
			}
		});

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

					TaskManager.save(tl.getHead());

					ProgressBar completion = (ProgressBar) findViewById(R.id.hcompletion);
					final TextView percent = (TextView) findViewById(R.id.hpercent);

					ImageView check = (ImageView) v.findViewById(R.id.check);

					completion.setProgress(task.completion());

					percent.setText(task.completion() + "%");

					TextView name = (TextView) v.findViewById(R.id.name);
					TextView description = (TextView) v.findViewById(R.id.description);

					if (t.completion() == 100) {
						check.setVisibility(View.VISIBLE);
						name.setTextColor(Color.parseColor("#505050"));
						description.setTextColor(Color.parseColor("#505050"));
					} else {
						check.setVisibility(View.INVISIBLE);
						name.setTextColor(Color.parseColor("#ffffff"));
						description.setTextColor(Color.parseColor("#bbbbbb"));
					}
				}

			}
		});

	}

	private void placeFloatingView() {
		if (getScroll() < headerHeight + baseScrollHeight) {
			setTitleCheck(true);
			floatingProgBarHeader.setTop(-1 * getScroll() + baseScrollHeight);
		} else {
			setTitleCheck(false);
			floatingProgBarHeader.setTop(-1 * headerHeight);
		}
	}

	private void setTitleCheck(boolean setDefault) {
		if (setDefault) {
			if (!titleDefualt) {
				setTitle("Tree Task");
				titleDefualt = true;
			}
		} else {
			if (titleDefualt) {
				setTitle(task.getName());
				titleDefualt = false;
			}
		}
	}

	private int getScroll() {
		int scrollY = 0;
		View c = taskList.getChildAt(0); // this is the first visible row
		if (c != null) {
			scrollY = -c.getTop();
			listViewItemHeights.put(taskList.getFirstVisiblePosition(), c.getHeight());
			for (int i = 0; i < taskList.getFirstVisiblePosition(); ++i) {
				if (listViewItemHeights.get(i) != null) // (this is a sanity
														// check)
					scrollY += listViewItemHeights.get(i); // add all heights of
															// the views that
															// are gone
			}
		}
		return scrollY;
	}

	@Override
	public void onResume() {
		super.onResume();
		taskList.setSelection(0);
		listViewItemHeights = new Hashtable<Integer, Integer>();

	}

	@Override
	public void onRestart() {
		super.onRestart();
		Intent intent = getIntent();
		finish();
		startActivity(intent);
		overridePendingTransition(0, 0);

	}

	private void setOffset() {
		final View v = findViewById(R.id.triangle);
		ViewTreeObserver vto = v.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				triangleHeight = v.getMeasuredHeight();

				ViewTreeObserver vto1 = header.getViewTreeObserver();
				vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (!offsetSet) {
							headerHeight = header.getMeasuredHeight() - triangleHeight;
							floatingProgBarHeader.setY(headerHeight);
							offsetSet = true;
						}
					}
				});
			}
		});

	}

	private View header() {
		View header = getLayoutInflater().inflate(R.layout.header, null);

		TextView name = (TextView) header.findViewById(R.id.hname);
		TextView path = (TextView) header.findViewById(R.id.path);
		TextView description = (TextView) header.findViewById(R.id.hdescription);
		TextView percent = (TextView) findViewById(R.id.hpercent);

		ProgressBar completion = (ProgressBar) findViewById(R.id.hcompletion);

		name.setText(task.getName());
		path.setText(task.getPath());
		description.setText(task.getDescription());

		completion.setMax(100);
		completion.setProgress(task.completion());
		percent.setText(task.completion() + "%");

		return header;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.taskview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
		switch (item.getItemId()) {
		case R.id.edit:

			i = new Intent(TaskView.this, EditTask.class);
			i.putExtra("task", task);
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.slideup, R.anim.shortzoom);
			break;

		case R.id.newTask:
			i = new Intent(TaskView.this, NewTask.class);
			i.putExtra("task", task);
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.slideup, R.anim.shortzoom);

			break;

		case R.id.treeView:
			i = new Intent(TaskView.this, TreeView.class);
			i.putExtra("task", task);
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.slidedownto, R.anim.shortzoom);

			break;

		case R.id.share:

			i = new Intent(TaskView.this, ExportView.class);
			i.putExtra("task", task);
			startActivity(i);
			overridePendingTransition(R.anim.slidedownto, R.anim.shortzoom);
			break;

		default:
			break;
		}

		return true;
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

		case R.id.editSubTask:

			Task taskleaf = task.getChild(info.position);

			Intent i1 = new Intent(TaskView.this, EditTask.class);
			i1.putExtra("task", taskleaf);
			i1.putExtra("fromListView", true);
			finish();
			startActivity(i1);
			overridePendingTransition(R.anim.slideup, R.anim.shortzoom);

			return true;

		case R.id.delete:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to Delete this Task?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					task.deleteChild(info.position);
					TaskManager.save(task.getHead());

					if (task.numChildren() < 1) {

						if (task.getParent() != null) {

							Intent i = new Intent(TaskView.this, TaskView.class);
							i.putExtra("task", task.getParent());
							finish();
							startActivity(i);
							overridePendingTransition(R.anim.backshortzoom, R.anim.slideto);

						} else {

							TaskManager.delete(task.getHead().taskID);
							Intent i = new Intent(TaskView.this, Main.class);
							finish();
							startActivity(i);
							overridePendingTransition(R.anim.backshortzoom, R.anim.slideto);

						}

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

		if (treeView != null) {
			TaskManager.save(task.getHead());
			Intent i = new Intent(TaskView.this, TreeView.class);

			Task t = task;
			while (parentCount > 0) {
				t = task.getParent();
				parentCount--;
			}

			i.putExtra("task", t);
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.backshortzoom, R.anim.slideto);

		} else {
			if (task.isHead()) {

				Intent i = new Intent(TaskView.this, Main.class);
				if (task.completion() == 100)
					i.putExtra("page", 1);
				else
					i.putExtra("page", 0);
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

	}

}
