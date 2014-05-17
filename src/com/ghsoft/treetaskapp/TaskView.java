package com.ghsoft.treetaskapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskDummy;
import com.ghsoft.treetask.TaskLeaf;
import com.ghsoft.treetask.TaskManager;
import com.ghsoft.treetask.TaskNode;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.FloatViewManager;

public class TaskView extends ActionBarActivity {

	private DragSortListView taskList;
	private TaskNode task;
	private TaskViewListItem adapter;
	private View header;
	private Task treeView;
	private int parentCount, headerHeight, baseScrollHeight, triangleHeight, setSompletion;
	private LinearLayout floatingProgBarHeader, headerBase;
	private RelativeLayout floatingViewBase;
	private Dictionary<Integer, Integer> listViewItemHeights;
	private boolean titleDefualt, setScrollHeight, offsetSet;
	private TextView percent;
	private ProgressBar completion;
	private RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

	private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			if (w > 0.8f) {
				return ((float) adapter.getCount()) / 0.001f;
			} else {
				return 10.0f * w;
			}
		}
	};

	private DragSortListView.DragSortListener dragSort = new DragSortListView.DragSortListener() {

		@Override
		public void drag(int from, int to) {
		}

		@Override
		public void drop(int from, int to) {
			adapter.move(from, to);
			adapter.notifyDataSetChanged();
			TaskManager.save(task.getHead());
		}

		@Override
		public void remove(int which) {
		}
	};
	
	@SuppressLint("InlinedApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_view);
		setScrollHeight = false;
		offsetSet = false;
		treeView = null;

		listViewItemHeights = new Hashtable<Integer, Integer>();

		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("treeView")) {
			treeView = (Task) getIntent().getExtras().getSerializable("treeView");
			parentCount = getIntent().getExtras().getInt("parentCount");
		}
		
		//htimestamp
		Object sTask = getIntent().getSerializableExtra("task");

		task = (TaskNode) sTask;
		
		

		setTitleCheck(false);

		taskList = (DragSortListView) findViewById(R.id.taskList);
		taskList.setDragSortListener(dragSort);
		taskList.setDragScrollProfile(ssProfile);

		header = header();

		setUpFloatingHead();

		taskList.addHeaderView(header, null, false);

		adapter = new TaskViewListItem(getApplicationContext(), task, header);

		taskList.setAdapter(adapter);
		
		try {
			taskList.setOverScrollMode(View.OVER_SCROLL_NEVER);
		} catch (NoSuchMethodError e) {
		}

		registerForContextMenu(taskList);

		floatingProgBarHeader = (LinearLayout) findViewById(R.id.progBarFloat);

		setOffset();
		
		if (task.getUseColor()) {
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(task.getColor()));
			headerBase.setBackgroundColor(task.getColor());
			floatingViewBase.setBackgroundColor(task.getColor());
		}
		
		

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

					updateFloatingHead();

					if (t.completion() == 100)
						setGrey(v, true);
					else
						setGrey(v, false);
				}

			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();

	}

	public void setGrey(View v, boolean set) {
		ImageView check = (ImageView) v.findViewById(R.id.check);
		TextView name = (TextView) v.findViewById(R.id.name);
		TextView description = (TextView) v.findViewById(R.id.description);
		TextView timeStamp = (TextView) v.findViewById(R.id.timestamp);

		if (set) {
			check.setVisibility(View.VISIBLE);
			name.setTextColor(Color.parseColor("#505050"));
			description.setTextColor(Color.parseColor("#505050"));
			timeStamp.setTextColor(Color.parseColor("#505050"));
		} else {
			check.setVisibility(View.INVISIBLE);
			name.setTextColor(Color.parseColor("#ffffff"));
			description.setTextColor(Color.parseColor("#bbbbbb"));
			timeStamp.setTextColor(Color.parseColor("#bbbbbb"));
		}
	}

	private void placeFloatingView() {
		
		if (getScroll() < (headerHeight + baseScrollHeight)) {
			setTitleCheck(true);
			
			
			params.topMargin = (-1 * getScroll() + headerHeight + baseScrollHeight); //Your Y coordinate
			floatingProgBarHeader.setLayoutParams(params);
			
		} else {
			setTitleCheck(false);
			
			params.topMargin = 0; //Your Y coordinate
			floatingProgBarHeader.setLayoutParams(params);
		}
		
		
	}

	private void setTitleCheck(boolean setDefault) {
		if (setDefault) {
			if (!titleDefualt) {
				getSupportActionBar().setTitle("Tree Task");
				titleDefualt = true;
			}
		} else {
			if (titleDefualt) {
				getSupportActionBar().setTitle(task.getName());
				titleDefualt = false;
			}
		}
	}

	private int getScroll() {
		int scrollY = 0;
		View c = taskList.getChildAt(0);
		if (c != null) {
			scrollY = -c.getTop();
			listViewItemHeights.put(taskList.getFirstVisiblePosition(), c.getHeight());
			for (int i = 0; i < taskList.getFirstVisiblePosition(); ++i) {
				if (listViewItemHeights.get(i) != null)
					scrollY += listViewItemHeights.get(i);
			}
		}
		return scrollY;
	}
	

	@Override
	public void onResume() {
		super.onResume();
		taskList.setSelection(0);
		listViewItemHeights = new Hashtable<Integer, Integer>();
		if (task.getUseColor()) {
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(task.getColor()));
			headerBase.setBackgroundColor(task.getColor());
			floatingViewBase.setBackgroundColor(task.getColor());
		}

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
				
				if (!offsetSet) {
					headerHeight = header.getMeasuredHeight() - triangleHeight;
					
					params.topMargin = headerHeight; //Your Y coordinate
					floatingProgBarHeader.setLayoutParams(params);
					
					offsetSet = true;
				}

			}
		});

	}

	private void setUpFloatingHead() {

		percent = (TextView) findViewById(R.id.hpercent);
		completion = (ProgressBar) findViewById(R.id.hcompletion);
		completion.setMax(100);

		updateFloatingHead();
	}

	private void updateFloatingHead() {

		new Thread(new Runnable() {
			public void run() {
				setSompletion = task.completion();
				handler.sendEmptyMessage(0);
			}

			@SuppressLint("HandlerLeak")
			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					completion.setProgress(setSompletion);
					percent.setText(setSompletion + "%");
				}
			};
		}).start();

	}

	@SuppressLint("SimpleDateFormat")
	private View header() {
		View header = getLayoutInflater().inflate(R.layout.header, null);

		TextView name = (TextView) header.findViewById(R.id.hname);
		TextView path = (TextView) header.findViewById(R.id.path);
		TextView description = (TextView) header.findViewById(R.id.hdescription);
		TextView timeStamp = (TextView) header.findViewById(R.id.htimestamp);
		headerBase = (LinearLayout) header.findViewById(R.id.header_base);
		floatingViewBase = (RelativeLayout) findViewById(R.id.floatingViewBase);
		
		if (task.getTimeStamp() != null) {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
			String timeString = df.format(task.getTimeStamp());
			timeStamp.setText(timeString);
		}

		name.setText(task.getName());
		path.setText(task.getPath());
		description.setText(task.getDescription());

		return header;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
			Log.e("test", "" + (null == taskleaf.getParent()));
			i1.putExtra("fromListView", true);
			finish();
			startActivity(i1);
			overridePendingTransition(R.anim.slideup, R.anim.shortzoom);

			return true;

		case R.id.delete:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.sure_to_delete).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
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

							TaskDummy t = new TaskDummy(task);
							task.addSubTask(t);

							Intent i = new Intent(TaskView.this, NewTreeView.class);
							i.putExtra("task", task);
							finish();
							startActivity(i);
							overridePendingTransition(R.anim.backshortzoom, R.anim.slidedown);
						}

					} else {

						adapter.notifyDataSetChanged();
						updateFloatingHead();

					}
				}

			}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
				i.putExtra("page", 0);	//let always go to tasks for now
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
