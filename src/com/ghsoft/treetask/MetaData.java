package com.ghsoft.treetask;

import java.io.Serializable;
import java.util.ArrayList;

public class MetaData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String> taskIds, archiveIds;
	
	public MetaData() {
		taskIds = new ArrayList<String>();
		archiveIds = new ArrayList<String>();
	}
	
	public ArrayList<String> getTasks() {
		return taskIds;
	}
	
	public ArrayList<String> getArchive() {
		return archiveIds;
	}
	
	public void moveTask(TaskHead th, int index) {
		taskIds.remove(th.taskID);
		taskIds.add(index, th.taskID);
	}
	
	public void moveArchiveTask(TaskHead th, int index) {
		archiveIds.remove(th.taskID);
		archiveIds.add(index, th.taskID);
	}
	
	public void setTasks(ArrayList<String> tasks) {
		taskIds = tasks;
	}
	
	public void setArchive(ArrayList<String> tasks) {
		taskIds = tasks;
	}
	
	public void buildTasksOrder(ArrayList<TaskHead> tasks) {
		this.taskIds = new ArrayList<String>();
		
		for (TaskHead th : tasks) {
			this.taskIds.add(th.taskID);
		}
		
	}
	
	public void buildArchiveOrder(ArrayList<TaskHead> tasks) {
		this.archiveIds = new ArrayList<String>();
		
		for (TaskHead th : tasks) {
			this.archiveIds.add(th.taskID);
		}
		
	}
	
}
