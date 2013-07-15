package com.ghsoft.treetask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.os.Environment;

public class TaskManager {

	private ArrayList<TaskHead> tasks, archive;
	File sdcard = Environment.getExternalStorageDirectory();

	public TaskManager() {
		tasks = new ArrayList<TaskHead>();
		archive = new ArrayList<TaskHead>();
		
		TaskHead th = new TaskHead();

		TaskNode t = new TaskNode(th);
		t.setName("test");
		t.setDescription("test123");

		TaskLeaf tl = new TaskLeaf(t);
		tl.setName("asdfasfd");
		tl.setDescription("fdsa");

		// tasks.add(th);

	}

	public void load() {
		File dir = new File(sdcard.getAbsolutePath() + "/TaskTree/");
		for (File child : dir.listFiles()) {

			FileInputStream fis;

			try {
				System.err.println(child.toString());
				fis = new FileInputStream(child);
				ObjectInputStream is = new ObjectInputStream(fis);
				TaskHead th = (TaskHead) is.readObject();
				is.close();
				if (th.archived()) {
					archive.add(th);
				} else {
					tasks.add(th);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public ArrayList<TaskHead> getTasks() {
		return this.tasks;
	}

	public ArrayList<TaskHead> getArchive() {
		return this.archive;
	}

	public static void save(final TaskHead th) {
		new Thread(new Runnable() {
			public void run() {

				File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TaskTree");
				if (!dir.exists()) {
					try {
						dir.mkdir();
					} catch (Exception e) {
						System.err.println("Error: " + e.getMessage());
					}
				}

				try {
					FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TaskTree/" + th.taskID);
					ObjectOutputStream os = new ObjectOutputStream(fos);
					os.writeObject(th);
					os.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

}
