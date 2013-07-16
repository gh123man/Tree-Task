package com.ghsoft.treetask;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainViewFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private static final int TASKS = 1;
	private static final int ARCHIVE = 2;
	private ListView taskList;
	public TaskManager tm;
	private ArrayList<TaskHead> toDisplay;
	private MainListItem adapter;
	public int id;
	private int selected;

	public MainViewFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.mainviewfragment, container, false);

		tm = (TaskManager) getArguments().get("tm");

		toDisplay = new ArrayList<TaskHead>();
		if (getArguments().getInt(ARG_SECTION_NUMBER) == TASKS) {
			toDisplay = tm.getTasks();
		} else if (getArguments().getInt(ARG_SECTION_NUMBER) == ARCHIVE) {
			toDisplay = tm.getArchive();
		}

		taskList = (ListView) rootView.findViewById(R.id.taskList);

		if (toDisplay.size() > 0) {
			adapter = new MainListItem(getActivity(), getActivity().getApplicationContext(), toDisplay);

			taskList.setAdapter(adapter);
		}

		registerForContextMenu(taskList);

		taskList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Intent i = new Intent(getActivity(), TaskView.class);
				i.putExtra("task", toDisplay.get(position).getTask());
				getActivity().finish();
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.zoom, R.anim.slide);

			}
		});

		// dummyTextView.setText(Integer.toString(getArguments().getInt(
		// ARG_SECTION_NUMBER)));

		return rootView;
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		inflater.inflate(R.menu.mainviewmenu, menu);

	}

	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {

		case R.id.delete:

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Are you sure you want to Delete this story?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
					
					ProgressBar completion = (ProgressBar) info.targetView.findViewById(R.id.completion);
					
					if (completion.getProgress() == 100) {
						
						TaskManager.delete(tm.getArchive().get(info.position).taskID);
						
						
					} else {

						TaskManager.delete(tm.getTasks().get(info.position).taskID);

					}
					
					Intent i = new Intent(getActivity(), Main.class);
					getActivity().finish();
					startActivity(i);
					getActivity().overridePendingTransition(0, 0);
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
}
