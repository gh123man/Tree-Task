package com.ghsoft.treetaskapp;

import java.io.FileNotFoundException;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ghsoft.treetask.R;
import com.ghsoft.treetask.TaskManager;

public class Main extends ActionBarActivity {

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private int page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		page = 0;

		if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("page")) {
			page = getIntent().getExtras().getInt("page");
		}

		PagerTabStrip pts = (PagerTabStrip) findViewById(R.id.pager_title_strip);
		pts.setDrawFullUnderline(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);

		mViewPager.setAdapter(mSectionsPagerAdapter); // issue

		mViewPager.setCurrentItem(page);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageScrollStateChanged(int state) {
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			public void onPageSelected(int position) {
				page = position;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// case R.id.action_settings:
		// Toast.makeText(this, "Menu Item 1 selected",
		// Toast.LENGTH_SHORT).show();
		// break;

		case R.id.newTask:
			Intent i = new Intent(Main.this, NewTreeTask.class);
			finish();
			startActivity(i);
			overridePendingTransition(R.anim.slideup, R.anim.shortzoom);

			break;

		/*
		 * case R.id.about: Intent aboutIntent = new Intent(Main.this,
		 * NewTreeTask.class); finish(); startActivity(aboutIntent);
		 * overridePendingTransition(R.anim.slideup, R.anim.shortzoom);
		 * 
		 * break;
		 */

		default:
			break;
		}

		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Context c;

		public SectionsPagerAdapter(FragmentManager fm, Context c) {
			super(fm);
			this.c = c;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			TaskManager tm = new TaskManager();

			try {
				tm.load();
			} catch (FileNotFoundException e) {
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				builder.setMessage("Cannot read tasks from SD card. Do you have an SD card inserted?").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

			Fragment fragment = new MainViewFragment();
			Bundle args = new Bundle();

			args.putSerializable("tm", tm);

			args.putInt(MainViewFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		if (mViewPager.getCurrentItem() == 1) {
			mViewPager.setCurrentItem(0, true);
		} else {
			finish();
		}
	}

}
