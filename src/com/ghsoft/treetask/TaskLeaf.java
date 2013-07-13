package com.ghsoft.treetask;

public class TaskLeaf extends Task {
	
	private boolean complete;

	public TaskLeaf() {
		
		this.complete = false;
		
	}

	@Override
	public int completion() {
		return complete ? 1 : 0;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

}
