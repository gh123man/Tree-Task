package com.ghsoft.treetaskapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.TaskNode;
import com.ghsoft.treetask.TextTreeBuilder;

public class ExportView extends ActionBarActivity {

	private TaskNode task;
	private Button share;
	private TextView preview;
	private volatile TextTreeBuilder ttb;
	private RadioGroup nodeOptions;
	private volatile String previewText;
	private CheckBox nums, des, prog, tabs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.export_view);

		Object sTask = getIntent().getSerializableExtra("task");
		task = (TaskNode) sTask;
		setTitle(R.string.preview);

		share = (Button) findViewById(R.id.share);
		preview = (TextView) findViewById(R.id.preview);
		nodeOptions = (RadioGroup) findViewById(R.id.nodeOptions);
		nums = (CheckBox) findViewById(R.id.showNumbers);
		des = (CheckBox) findViewById(R.id.showDescription);
		prog = (CheckBox) findViewById(R.id.showProgress);
		tabs = (CheckBox) findViewById(R.id.useTabs);

		ttb = new TextTreeBuilder(task);
		preview.setText(ttb.getText());

		if (task.isHead()) {
			RadioButton rb = (RadioButton) findViewById(R.id.curTask);
			rb.setEnabled(false);
			rb = (RadioButton) findViewById(R.id.whoTree);
			rb.setChecked(true);

		}

		setListeners();

	}

	private void updatePreview() {

		new Thread(new Runnable() {
			public void run() {
				previewText = ttb.getText();
				handler.sendEmptyMessage(0);
			}

			private Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					preview.setText(previewText);
				}
			};
		}).start();

	}

	private void setListeners() {

		nodeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
				RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(checkedId);

				if (checkedRadioButton.isChecked()) {
					if (checkedRadioButton.getId() == R.id.curTask) {
						ttb.setHead(false);
					} else if (checkedRadioButton.getId() == R.id.whoTree) {
						ttb.setHead(true);
					}

				} else {
					if (checkedRadioButton.getId() == R.id.curTask) {
						ttb.setHead(true);
					} else if (checkedRadioButton.getId() == R.id.whoTree) {
						ttb.setHead(false);
					}
				}
				updatePreview();
			}
		});

		nums.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					ttb.setNums(true);
				} else {
					ttb.setNums(false);
				}
				updatePreview();
			}
		});

		prog.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					ttb.setUseProgress(true);
				} else {
					ttb.setUseProgress(false);
				}
				updatePreview();
			}
		});

		tabs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					ttb.setTabs(true);
				} else {
					ttb.setTabs(false);
				}
				updatePreview();
			}
		});

		des.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					ttb.setUseDescription(true);
				} else {
					ttb.setUseDescription(false);
				}
				updatePreview();
			}
		});

		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, ttb.getText());
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});
	}
}
