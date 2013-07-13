package com.ghsoft.treetask;

public class TaskLeaf extends Task {
	
	private boolean complete;

	public TaskLeaf() {
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

}
