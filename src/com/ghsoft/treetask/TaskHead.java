package com.ghsoft.treetask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

import android.content.Context;
import android.os.Environment;

public class TaskHead implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TaskNode task;
	final String taskID;

	public TaskHead() {
		taskID = UUID.randomUUID().toString();
	}

	public void setTreeHead(TaskNode t) {
		this.task = t;
	}
	
	public TaskNode getTask() {
		return task;
	}
	
	public boolean archived() {
		return task.completion() == 100;
	}

	

}
