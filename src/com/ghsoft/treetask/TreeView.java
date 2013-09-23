package com.ghsoft.treetask;

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
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TreeView extends Activity {

	TaskNode task;
	WebView treeDisplay;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.treeview);

		Object sTask = getIntent().getSerializableExtra("task");

		task = (TaskNode) sTask;

		setTitle(task.getName());
		
		treeDisplay = (WebView) findViewById(R.id.treeDisplay);
		
		HtmlTreeBuilder builder = new HtmlTreeBuilder(task);
		
		
		
		String data = builder.getHtml();
		
		treeDisplay.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "UTF-8", "");

	}
	
	@Override
	public void onBackPressed() {

		Intent i = new Intent(TreeView.this, TaskView.class);
		i.putExtra("task", task);
		finish();
		startActivity(i);
		overridePendingTransition(R.anim.backshortzoom, R.anim.slideto);


	}

}
