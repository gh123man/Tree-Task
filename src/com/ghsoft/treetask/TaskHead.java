package com.ghsoft.treetask;

import java.io.Serializable;
import java.util.UUID;

public class TaskHead implements Serializable {

	private static final long serialVersionUID = 1L;
	private TaskNode task;
	public final String taskID;

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
