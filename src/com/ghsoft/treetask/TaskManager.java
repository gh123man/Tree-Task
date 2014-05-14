package com.ghsoft.treetask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

public class TaskManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<TaskHead> tasks, archive;
	private static MetaData metaData;
	private static File sdcard = Environment.getExternalStorageDirectory();
	private static final int TASK = 0;
	private static final int ARCHIVE = 0;

	public TaskManager() {
		tasks = new ArrayList<TaskHead>();
		archive = new ArrayList<TaskHead>();

	}

	public static void delete(final String id) {

		new Thread(new Runnable() {
			public void run() {
				File dir = new File(sdcard.getAbsolutePath() + "/TaskTree/" + id);
				dir.delete();
			}

		}).start();

	}

	public void load() {
		File dir = new File(sdcard.getAbsolutePath() + "/TaskTree/");
		metaData = null;
		if (!dir.exists()) {
			try {
				
				boolean success = dir.mkdir();
				if (success) {
				} else {
					Log.e("err", "failed to create DIR");
				}
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}

		for (File child : dir.listFiles()) {

			FileInputStream fis;

			try {
				fis = new FileInputStream(child);
				ObjectInputStream is = new ObjectInputStream(fis);
				if (child.getName().equals("meta.dat")) {

					metaData = (MetaData) is.readObject();

				} else {

					TaskHead th = (TaskHead) is.readObject();
					is.close();
					if (th.archived()) {
						archive.add(th);
					} else {
						tasks.add(th);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (metaData == null) {

			metaData = new MetaData();

		}
		sortTasks(metaData.getTasks());
		sortArchive(metaData.getArchive());

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
						e.printStackTrace();
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

	public static void saveMetaData() {
		new Thread(new Runnable() {
			public void run() {

				File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TaskTree");
				if (!dir.exists()) {
					try {
						dir.mkdir();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				try {
					FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TaskTree/meta.dat");
					ObjectOutputStream os = new ObjectOutputStream(fos);
					os.writeObject(metaData);
					os.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	private void sortTasks(ArrayList<String> ids) {

		tasks = genericSort(ids, tasks, TASK);
	}

	private void sortArchive(ArrayList<String> ids) {

		archive = genericSort(ids, archive, ARCHIVE);
	}
	
	private ArrayList<TaskHead> genericSort(ArrayList<String> ids, ArrayList<TaskHead> taskList, int type) {
		
		if (taskList.size() != 0) {

			ArrayList<TaskHead> out = new ArrayList<TaskHead>(taskList);

			ArrayList<String> curIds = new ArrayList<String>();

			for (int i = 0; i < taskList.size(); i++) {
				curIds.add(taskList.get(i).taskID);
			}

			ArrayList<String> tmpIds = new ArrayList<String>(ids);

			for (int i = 0; i < tmpIds.size(); i++) {
				if (!curIds.contains(tmpIds.get(i))) {
					ids.remove(tmpIds.get(i));
				}
			}

			for (int i = 0; i < curIds.size(); i++) {
				if (!ids.contains(curIds.get(i))) {
					ids.add(curIds.get(i));
				}
			}
			
			for (TaskHead th : taskList) {
				int pos = ids.indexOf(th.taskID);
				out.remove(pos);
				out.add(pos, th);
			}

			if (type == TASK) {
				metaData.setTasks(ids);
			} else {
				metaData.setArchive(ids);
			}
			
			return out;
		}
		
		return taskList;
	}
	

	public MetaData getMetadata() {
		return metaData;
	}

}
