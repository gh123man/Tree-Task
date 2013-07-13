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

import com.example.treetask.R;

public class MainViewFragment extends Fragment {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private ListView taskList;
	public TaskNode t;

	public MainViewFragment() {
		t = new TaskNode();
		t.setDescription("blah blah blah blha");
		t.setName("test Task");
		t.addSubTask(new TaskLeaf());
		t.addSubTask(new TaskLeaf());
		t.addSubTask(new TaskLeaf());
		
		TaskLeaf a = new TaskLeaf();
		a.setFinished(true);
		
		t.addSubTask(a);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.mainviewfragment, container,
				false);

		taskList = (ListView) rootView.findViewById(R.id.taskList);

		MainListItem adapter = new MainListItem(getActivity(), getActivity()
				.getApplicationContext(), t);

		taskList.setAdapter(adapter);
		
		taskList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent i = new Intent(getActivity(), TaskView.class);
				i.putExtra("task", t);
				startActivity(i);
				

			}
		});

		// dummyTextView.setText(Integer.toString(getArguments().getInt(
		// ARG_SECTION_NUMBER)));

		return rootView;
	}

}
