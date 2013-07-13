package com.ghsoft.treetask;

import java.io.Serializable;

public class TaskLeaf extends Task implements Serializable {
	
	private boolean complete;

	public TaskLeaf(Task parent) {
		super(parent);
		this.complete = false;
	}

	@Override
	public int completion() {
		return complete ? 100 : 0;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}
	
	public void setFinished(boolean set) {
		this.complete = set;
	}
	
	public boolean getFinished() {
		return complete;
	}

}
