package com.ghsoft.treetask;

import java.io.Serializable;

public class TaskDummy extends Task implements Serializable {

	private static final long serialVersionUID = 1L;

	public TaskDummy(TaskNode parent) {
		super(parent);
	}

	@Override
	public int completion() {
		return 0;
	}

	@Override
	public int subTaskCount() {
		return 0;
	}

}
