package com.ghsoft.treetask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ghsoft.treetask.R;

public class MainViewFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private ListView taskList;
	public TaskManager tm;

	public MainViewFragment() {
		
		tm = new TaskManager();
		
		tm.load();
		
		
		

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.mainviewfragment, container,
				false);

		taskList = (ListView) rootView.findViewById(R.id.taskList);

		MainListItem adapter = new MainListItem(getActivity(), getActivity()
				.getApplicationContext(), tm.getTasks());

		taskList.setAdapter(adapter);

		taskList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent i = new Intent(getActivity(), TaskView.class);
				i.putExtra("task", tm.getTasks().get(position).getTask());
				getActivity().finish();
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.zoom,
						R.anim.slide);

			}
		});

		// dummyTextView.setText(Integer.toString(getArguments().getInt(
		// ARG_SECTION_NUMBER)));

		return rootView;
	}

}
