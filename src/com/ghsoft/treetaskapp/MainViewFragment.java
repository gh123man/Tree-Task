package com.ghsoft.treetaskapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.Task;
import com.ghsoft.treetask.TaskDummy;
import com.ghsoft.treetask.TaskHead;
import com.ghsoft.treetask.TaskManager;
import com.ghsoft.treetask.TaskNode;
import com.mobeta.android.dslv.DragSortListView;

public class MainViewFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private static final int TASKS = 1;
	// private static final int ARCHIVE = 2;
	private DragSortListView taskList;
	public TaskManager tm;
	private ArrayList<TaskHead> toDisplay;
	private MainListItem adapter;
	public int id;

	private DragSortListView.DragScrollProfile ssProfile = new DragSortListView.DragScrollProfile() {
		@Override
		public float getSpeed(float w, long t) {
			if (w > 0.8f) {
				// Traverse all views in a millisecond
				return ((float) adapter.getCount()) / 0.001f;
			} else {
				return 10.0f * w;
			}
		}
	};

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			TaskHead th = adapter.getData().get(from);
			
			adapter.getData().remove(th);
			adapter.getData().add(to, th);
			adapter.notifyDataSetChanged();
			
			if (adapter.getType().equals("tasks")) {
				tm.getMetadata().buildTasksOrder(adapter.getData());
				TaskManager.saveMetaData();
			} else {
				tm.getMetadata().buildArchiveOrder(adapter.getData());
				TaskManager.saveMetaData();
			}
			
		}
	};
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView;

		tm = (TaskManager) getArguments().get("tm");

		toDisplay = new ArrayList<TaskHead>();
		if (getArguments().getInt(ARG_SECTION_NUMBER) == TASKS) {
			rootView = inflater.inflate(R.layout.mainviewfragment, container, false);
			toDisplay = tm.getTasks();
			taskList = (DragSortListView) rootView.findViewById(R.id.taskList);
			adapter = new MainListItem(getActivity().getApplicationContext(), toDisplay, "tasks");
		} else {
			rootView = inflater.inflate(R.layout.mainviewfragmentarchive, container, false);
			toDisplay = tm.getArchive();
			taskList = (DragSortListView) rootView.findViewById(R.id.taskListArchive);
			adapter = new MainListItem(getActivity().getApplicationContext(), toDisplay, "archive");
		}
		
		taskList.setDropListener(onDrop);
		taskList.setDragScrollProfile(ssProfile);

		if (toDisplay.size() > 0) {
			

			taskList.setAdapter(adapter);
		}

		registerForContextMenu(taskList);

		taskList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				ViewPager a = (ViewPager) arg1.getParent().getParent().getParent().getParent(); //lol
				
				TaskNode t = toDisplay.get(position).getTask();
				Task child = t.getChild(0);
				
				Intent i = null;
				
				if (child instanceof TaskDummy) {
					i = new Intent(getActivity(), NewTreeView.class);
				} else {
					i = new Intent(getActivity(), TaskView.class);
				}
				
				i.putExtra("task", toDisplay.get(position).getTask());
				i.putExtra("page", a.getCurrentItem());
				getActivity().finish();
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.zoom, R.anim.slide);

			}
		});

		return rootView;
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ViewPager a = (ViewPager) v.getParent().getParent().getParent();

		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();

		if (a.getCurrentItem() == 0)
			inflater.inflate(R.menu.mainviewmenu, menu);
		else
			inflater.inflate(R.menu.mainviewmenuarchive, menu);

	}

	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		AlertDialog.Builder builder;
		AlertDialog alert;
		switch (item.getItemId()) {

		case R.id.deleteTask:

			builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Are you sure you want to Delete this Task?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

					TaskManager.delete(tm.getTasks().get(info.position).taskID);

					DragSortListView list = (DragSortListView) getActivity().findViewById(R.id.taskList);
					MainListItem adapter = ((MainListItem) list.getInputAdapter());
					adapter.getData().remove(info.position);
					
					tm.getMetadata().buildTasksOrder(adapter.getData());
					TaskManager.saveMetaData();
					
					adapter.notifyDataSetChanged();

				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			alert = builder.create();
			alert.show();

			return true;

		case R.id.deleteArchive:

			builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Are you sure you want to Delete this Task?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
					
					TaskManager.delete(tm.getArchive().get(info.position).taskID);

					DragSortListView list = (DragSortListView) getActivity().findViewById(R.id.taskListArchive);
					MainListItem adapter = ((MainListItem) list.getInputAdapter());
					adapter.getData().remove(info.position);
					
					tm.getMetadata().buildArchiveOrder(adapter.getData());
					TaskManager.saveMetaData();
					
					adapter.notifyDataSetChanged();
				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			alert = builder.create();
			alert.show();

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}
