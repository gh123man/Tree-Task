package com.ghsoft.treetaskapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.TaskNode;

public class TreeView extends ActionBarActivity {

	private TaskNode task;
	private WebView treeDisplay;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tree_view);

		Object sTask = getIntent().getSerializableExtra("task");
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		task = (TaskNode) sTask;

		setTitle(task.getName());

		treeDisplay = (WebView) findViewById(R.id.treeDisplay);
		treeDisplay.setBackgroundColor(Color.argb(1, 0, 0, 0));

		HtmlTreeBuilder builder = new HtmlTreeBuilder(task.getHead().getTask(), TreeView.this);

		String data = builder.getHtml();

		treeDisplay.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8", "");

		treeDisplay.setWebViewClient(new WebViewClient() {
			// Override URL
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				String[] parts = url.split(",");

				TaskNode h = (TaskNode) task.getHead().getTask();
				for (int i = 1; i < parts.length; i++) {
					h = (TaskNode) h.getChild((Integer.parseInt(parts[i])));
				}

				Intent i = new Intent(TreeView.this, TaskView.class);
				i.putExtra("task", h);
				i.putExtra("treeView", task);
				i.putExtra("parentCount", parts.length - 1);
				finish();
				startActivity(i);
				overridePendingTransition(R.anim.slide_from, R.anim.short_zoom);

				return true;
			}
		});

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed() {

		Intent i = new Intent(TreeView.this, TaskView.class);
		i.putExtra("task", task);
		finish();
		startActivity(i);
		overridePendingTransition(R.anim.back_short_zoom, R.anim.slide_up_from);

	}

}
